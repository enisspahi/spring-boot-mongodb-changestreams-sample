package de.openvalue.resilience.adapter.repository

import org.bson.BsonTimestamp
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface StreamOffsetRepository : ReactiveMongoRepository<StreamOffset, String>

@Document("stream-offsets")
@TypeAlias("StreamOffset")
data class StreamOffset(@Id val name: String,
                        val value: BsonTimestamp)
