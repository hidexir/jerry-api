package app.domain

import org.mongodb.scala.bson.ObjectId


object User {
  def apply(firstName: String,
            lastName: String,
            age: String,
            sex: String,
            mail: String,
            tel: String,
            subUserFirstName: String,
            subUserLastName: String,
            subUserAge: String,
            eventID: String
           )
  : User =
    User(new ObjectId(), firstName, lastName, age, sex, mail, tel,subUserFirstName,subUserLastName,subUserAge,eventID)
}

case class User(_id: ObjectId,
                firstName: String,
                lastName: String,
                age: String,
                sex: String,
                mail: String,
                tel: String,
                subUserFirstName: String,
                subUserLastName: String,
                subUserAge: String,
                eventID: String
               ) {
  require(firstName.nonEmpty, "firstName cannot be empty")
  require(lastName.nonEmpty, "lastName cannot be empty")
  require(age.nonEmpty, "age cannot be empty")
  require(sex.nonEmpty, "sex cannot be empty")
  require(mail.nonEmpty, "mail cannot be empty")
  require(tel.nonEmpty, "tel cannot be empty")
}

