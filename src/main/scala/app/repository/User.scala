package app.repository

import app.domain.User
import app.infra.dbConnect
import org.mongodb.scala.{Completed, Document, Observable, Observer}
import org.mongodb.scala.bson.ObjectId

import scala.concurrent.{ExecutionContextExecutor, Future}

trait UserRepository {
  def createUser(user: User) {
    val insertObservable: Observable[Completed] = dbConnect.userCollection.insertOne(user)
    insertObservable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = println("onComplete")
    })
  }

  def userFindByID(id: String)(implicit ak: ExecutionContextExecutor): Future[User] = {
    val objid: ObjectId = new ObjectId(id)
    val where = Document("_id" -> objid)
    val future = dbConnect.userCollection.find(where).first().toFuture()
    return future
  }
}


