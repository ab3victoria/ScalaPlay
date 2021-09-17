package models

import scala.collection.mutable

object ExpenseListInMemoryModel {

  private val users = mutable.Map[String,String]("Mark" ->"pass")

  private val expenses = mutable.Map[String, List[String]]("Mark" -> List("Make videos", "eat","sleep","code"))

  def validateUser(username: String, password: String): Boolean = {
    users.get(username).map(_ == password).getOrElse(false)
  }

  def createUser(username: String, password: String): Boolean = {

    if (users.contains(username)) false else {
      users(username) = password
      true
    }
  }

  def getExpenses(username: String):Seq[String]= {
    expenses.get(username).getOrElse(Nil)
  }

  def addExpense(username: String, expense: String):Unit= {
    expenses(username) = expense :: expenses.get(username).getOrElse(Nil)
  }

  def removeExpense(username: String, index: Int):Boolean= {
    if (index < 0 || expenses.get(username).isEmpty || index >= expenses(username).length) false
    else {
      expenses(username) = expenses(username).patch(index, Nil, 1)
      true
    }
  }
}
