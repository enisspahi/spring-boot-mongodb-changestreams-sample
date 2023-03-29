package de.openvalue.resilience.application

import de.openvalue.resilience.adapter.repository.StreamOffset
import de.openvalue.resilience.adapter.repository.StreamOffsetRepository
import de.openvalue.resilience.domain.OrderEntity
import io.github.resilience4j.reactor.retry.RetryOperator
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import jakarta.annotation.PostConstruct
import org.bson.BsonTimestamp
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.ChangeStreamEvent
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class InvoiceProcessor(val mailSender: JavaMailSender,
                       val reactiveTemplate: ReactiveMongoTemplate,
                       streamOffsetRepository: StreamOffsetRepository) {

    private val logger = LoggerFactory.getLogger(InvoiceProcessor::class.java)

    private var retry: Retry = Retry.of("InvoiceProcessor", RETRY_CONFIG)

    val stream = Flux.from(streamOffsetRepository.findById(OFFSET_NAME).map { it.value }.switchIfEmpty(Mono.just(NO_OFFSET)))
            .flatMap {
                reactiveTemplate.changeStream(OrderEntity::class.java)
                        .watchCollection("orders")
                        .resumeAt(it)
                        .listen()
            }
            .map { onEvent(it) }
            .flatMap { streamOffsetRepository.save(StreamOffset(OFFSET_NAME, it.bsonTimestamp!!)) }
            .transformDeferred(RetryOperator.of(retry))

    @PostConstruct
    fun initialize() {
        stream.subscribe {
            logger.info("Consumed $it")
        }
    }

    fun onEvent(event: ChangeStreamEvent<OrderEntity>): ChangeStreamEvent<OrderEntity> {
        with(event.body!!) {
            logger.info("Sending email for received order $this")
            val message = SimpleMailMessage()
            message.setTo(email)
            message.subject = "Invoice of your order $id"
            message.text = "The invoice for your order $items. Ignore this email if you have already received before."
            runCatching { mailSender.send(message) }
                    .onFailure { logger.error("Failed to send email", it) }
                    .getOrThrow()
        }
        return event
    }

    companion object {
        private val NO_OFFSET: BsonTimestamp = BsonTimestamp()
        private const val OFFSET_NAME = "InvoiceProcessorOffset"

        private val RETRY_CONFIG: RetryConfig =
                RetryConfig.custom<RetryConfig>()
                        .maxAttempts(10)
                        .waitDuration(Duration.ofSeconds(3))
                        .build()
    }

}