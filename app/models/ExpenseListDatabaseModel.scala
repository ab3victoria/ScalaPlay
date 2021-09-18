package models

import models.Tables._

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.PostgresProfile.api._
import org.mindrot.jbcrypt.BCrypt

class ExpenseListDatabaseModel(db: Database)(implicit ec: ExecutionContext) {

  def validateUser(username: String, password: String): Future[Option[Int]] = {
    val matches = db.run(Users.filter(userRow => userRow.username === username) .result)
    matches.map(userRows => userRows.headOption.flatMap {
      userRow => if (BCrypt.checkpw(password, userRow.password)) Some(userRow.id) else None
    })
  }

  def createUser(username: String, password: String): Future[Option[Int]] = {
    val matches = db.run(Users.filter(userRow => userRow.username === username).result)
    matches.flatMap { userRows =>
      if (userRows.isEmpty) {
        db.run(Users += UsersRow(-1, username, BCrypt.hashpw(password, BCrypt.gensalt())))
          .flatMap { addCount =>
            if (addCount > 0) db.run(Users.filter(userRow => userRow.username === username).result)
              .map(_.headOption.map(_.id))
            else Future.successful(None)
          }
      } else Future.successful(None)
    }
  }

  def getExpenses(username: String): Future[Seq[ExpenseData]] = {
    db.run(
      (for {
        user <- Users if user.username === username
        expense <- Expenses if expense.userId === user.id
      } yield  {
        expense
      }).result
    ).map(expenses => expenses.map(expense => ExpenseData(expense.expenseId, expense.title, expense.category, expense.cost, expense.date, expense.time)))
  }

  def addExpense(userId: Int, expenseData: ExpenseData): Future[Int] = {
    db.run(Expenses += ExpensesRow(-1, userId, expenseData.title, expenseData.category, expenseData.cost, expenseData.date, expenseData.time))
  }

  def removeExpense(expenseId: Int): Future[Boolean] = {
    db.run(Expenses.filter(_.expenseId === expenseId).delete).map(count => count > 0)
  }
}
