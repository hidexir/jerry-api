package app.domain

import org.mongodb.scala.bson.ObjectId


object Event {
  def apply(title: String,
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
  : Event =
    Event(new ObjectId(), title, openDay, numEntryPeople, entryConditions, manFee, womanFee, joinQA, dayOfFlow, other, status)
}

case class Event(_id: ObjectId,
                 title: String,
                 openDay: String,
                 numEntryPeople: String,
                 entryConditions: String,
                 manFee: String,
                 womanFee: String,
                 joinQA: String,
                 dayOfFlow: String,
                 other: String,
                 status: String
                ) {}
