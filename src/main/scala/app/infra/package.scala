package app.infra

import app.domain.{Column, Event, User}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

object dbConnect {
  val mongoClient: MongoClient = MongoClient("mongodb://root:example@127.0.0.1:27017/?authSource=admin");
  val codecRegistry = fromRegistries(fromProviders(classOf[User]), fromProviders(classOf[Event]),fromProviders(classOf[Column]),DEFAULT_CODEC_REGISTRY)
  val database: MongoDatabase = mongoClient.getDatabase("test").withCodecRegistry(codecRegistry)
  val userCollection: MongoCollection[User] = database.getCollection("user")
  val eventCollection: MongoCollection[Event] = database.getCollection("event")
  val columnCollection: MongoCollection[Column] = database.getCollection("column")
}

