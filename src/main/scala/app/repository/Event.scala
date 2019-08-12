package app.repository

import app.domain.Event
import app.infra.dbConnect
import org.mongodb.scala.{Completed, Document, Observable, Observer}
import org.mongodb.scala.bson.ObjectId

import scala.concurrent.{ExecutionContextExecutor, Future}

trait EventRepository {
  def createEvent(event: Event) {
    val insertObservable: Observable[Completed] = dbConnect.eventCollection.insertOne(event)
    insertObservable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = println("onComplete")
    })
  }

  def eventFindByID(id: String)(implicit ak: ExecutionContextExecutor): Future[Event] = {
    val objid: ObjectId = new ObjectId(id)
    val where = Document("_id" -> objid)
    val future = dbConnect.eventCollection.find(where).first().toFuture()
    return future
  }
}


