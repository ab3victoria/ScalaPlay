package controllers

import play.api.mvc.{AbstractController, ControllerComponents}
import models.ExpenseListInMemoryModel
import javax.inject.{Inject, Singleton}

@Singleton
class ExpensesList @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def login = Action{
    Ok(views.html.login1())
  }

  def validate1() = Action{ request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map{args =>
      val username = args("username").head
      val password = args("password").head
      if(ExpenseListInMemoryModel.validateUser(username,password)){
        Redirect(routes.ExpensesList.expensesList).withSession("username" -> username)
      } else {
        Redirect(routes.ExpensesList.login)
      }
    }.getOrElse(Redirect(routes.ExpensesList.login))

  }

  def createUser =Action { request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map{args =>
      val username = args("username").head
      val password = args("password").head
      if(ExpenseListInMemoryModel.createUser(username,password)){
        Redirect(routes.ExpensesList.expensesList).withSession("username" -> username)
      } else {
        Redirect(routes.ExpensesList.login)
      }
    }.getOrElse(Redirect(routes.ExpensesList.login))

  }

  def expensesList = Action{ request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val expenses = ExpenseListInMemoryModel.getExpenses(username)
      Ok(views.html.ExpensesList(expenses))
    }.getOrElse(Redirect(routes.ExpensesList.login))
  }

  def logout = Action{
    Redirect(routes.ExpensesList.login).withNewSession
  }

  def addExpense = Action { request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val expense = args("newExpense").head
        ExpenseListInMemoryModel.addExpense(username, expense);
        Redirect(routes.ExpensesList.expensesList)
      }.getOrElse(Redirect(routes.ExpensesList.expensesList))
    }.getOrElse(Redirect(routes.ExpensesList.login))
  }

  def deleteExpense = Action {  request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args =>
        val index = args("index").head.toInt
        ExpenseListInMemoryModel.removeExpense(username, index);
        Redirect(routes.ExpensesList.expensesList)
      }.getOrElse(Redirect(routes.ExpensesList.expensesList))
    }.getOrElse(Redirect(routes.ExpensesList.login))
  }

}
