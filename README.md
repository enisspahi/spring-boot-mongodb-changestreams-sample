# spring-boot-mongodb-changestreams-sample

# To try out change stream on mongoShell
````
myCursor = db.orders.watch()
while (true){
   if (myCursor.hasNext()){
      print(myCursor.next());
   }
}
````