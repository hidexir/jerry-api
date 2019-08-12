package app.repository

import app.domain.Column
import app.infra.dbConnect
import org.mongodb.scala.{Completed, Document, Observable, Observer}
import org.mongodb.scala.bson.ObjectId

import scala.concurrent.{ExecutionContextExecutor, Future}

trait ColumnRepository {
  def createColumn(column: Column) {
    val insertObservable: Observable[Completed] = dbConnect.columnCollection.insertOne(column)
    insertObservable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")

      override def onError(e: Throwable): Unit = println(s"onError: $e")

      override def onComplete(): Unit = println("onComplete")
    })
  }

  def columnFindByID(id: String)(implicit ak: ExecutionContextExecutor): Future[Column] = {
    val objid: ObjectId = new ObjectId(id)
    val where = Document("_id" -> objid)
    val future = dbConnect.columnCollection.find(where).first().toFuture()
    return future
  }
}


