package app

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.io.StdIn
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import spray.json._
import app.repository._
import app.domain._
import org.mongodb.scala.bson.ObjectId


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  case class UserJson(firstName: String,
                      lastName: String,
                      age: String,
                      sex: String,
                      mail: String,
                      tel: String,
                      subUserFirstName: String,
                      subUserLastName: String,
                      subUserAge: String,
                      eventID: String)

  case class EventJson(title: String,
                       openDay: String,
                       numEntryPeople: String,
                       entryConditions: String,
                       manFee: String,
                       womanFee: String,
                       joinQA: String,
                       dayOfFlow: String,
                       other: String,
                       status: String
                      )

  case class ColumnJson(title: String,
                        thumbnail: String,
                        body: String,
                        createdAt: String,
                        updateAt: String)


  implicit object ObjectIdSerializer extends RootJsonFormat[ObjectId] {
    override def write(obj: ObjectId): JsValue = {
      JsString(obj.toHexString)
    }

    override def read(json: JsValue): ObjectId = {
      val ob = new ObjectId(json.toString())
      ob
    }
  }

  implicit val userFormat = jsonFormat10(UserJson)
  implicit val eventFormat = jsonFormat10(EventJson)
  implicit val columnFormat = jsonFormat5(ColumnJson)
}

trait Repository extends UserRepository with EventRepository with ColumnRepository {
}

object WebServer extends Directives with JsonSupport with Repository {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val route =
      pathPrefix("user") {
        concat(
          post {
            entity(as[UserJson]) { o => // will unmarshal JSON to Order
              val user = User.apply(
                o.firstName,
                o.lastName,
                o.age,
                o.sex,
                o.mail,
                o.tel,
                o.subUserFirstName,
                o.subUserLastName,
                o.subUserAge,
                o.eventID
              )
              createUser(user)
              complete(s"$user")
            }
          },
          path(Remaining) { id =>
            get {
              onComplete(userFindByID(id)) {
                case util.Success(v) => complete(
                  UserJson(v.firstName, v.lastName, v.age, v.sex, v.mail, v.tel, v.subUserFirstName, v.subUserLastName, v.subUserAge, v.eventID)
                )
                case util.Failure(ex) => complete(StatusCodes.InternalServerError)
              }
            }
          }
        )
      } ~
        pathPrefix("event") {
          concat(
            post {
              entity(as[EventJson]) { e =>
                val event = Event.apply(
                    e.title,
                    e.openDay,
                    e.numEntryPeople,
                    e.entryConditions,
                    e.manFee,
                    e.womanFee,
                    e.joinQA,
                    e.dayOfFlow,
                    e.other,
                    e.status
                )
                createEvent(event)
                complete(s"$event")
              }
            },
            path(Remaining) { id =>
              get {
                onComplete(eventFindByID(id)) {
                  case util.Success(e) => {
                    complete(EventJson(
                      e.title,
                      e.openDay,
                      e.numEntryPeople,
                      e.entryConditions,
                      e.manFee,
                      e.womanFee,
                      e.joinQA,
                      e.dayOfFlow,
                      e.other,
                      e.status
                    ))
                  }
                  case util.Failure(t) => complete(StatusCodes.InternalServerError, t.getMessage)
                }
              }
            }
          )
        } ~
        pathPrefix("column") {
          concat(
            post {
              entity(as[ColumnJson]) { c =>
                val column = Column.apply(c.title, c.thumbnail, c.body, c.createdAt, c.updateAt)
                createColumn(column)
                complete(s"$column")
              }
            },
            path(Remaining) { id =>
              get {
                onComplete(columnFindByID(id)) {
                  case util.Success(c) => complete(
                    ColumnJson(c.title, c.thumbnail, c.body, c.createdAt, c.updateAt)
                  )
                  case util.Failure(_) => complete(StatusCodes.InternalServerError)
                }
              }
            }
          )
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}


