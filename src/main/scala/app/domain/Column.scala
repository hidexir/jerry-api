package app.domain

import org.mongodb.scala.bson.ObjectId


object Column {
  def apply(title: String, thumbnail: String, body: String, createdAt: String, updateAt: String): Column =
    Column(new ObjectId(), title, thumbnail, body, createdAt, updateAt)
}

case class Column(_id: ObjectId, title: String, thumbnail: String, body: String, createdAt: String, updateAt: String) {

}
