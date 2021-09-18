package models

import play.api.libs.json.{Json, OWrites, Reads}

import java.time.{LocalDate, LocalTime}

case class UserData(username: String, password: String)
case class ExpenseData(expenseId: Int, title: String, category: String, cost: BigDecimal, date: String, time: String)
// TODO: Check if need to add userId to ExpenseData
// TODO: Check how to change cost type from BigDecimal to Double (in sql)


// TODO: Delete?
//object ReadsAndWrites {
//  implicit val userDataReads = Json.reads[UserData]
//  implicit val userDataWrites = Json.writes[UserData]
//
//  implicit val taskItemReads = Json.reads[ExpenseData]
//  implicit val taskItemWrites = Json.writes[ExpenseData]
//}
