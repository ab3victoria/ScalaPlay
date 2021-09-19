package controllers

import models.{ExpenseData, UserData, ExpenseListDatabaseModel}

import javax.inject._

import scala.concurrent.{ExecutionContext, Future}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import play.api.libs.json._
import slick.jdbc.JdbcProfile

@Singleton
class ExpensesList @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  // Variable to hold database model methods
  private val model = new ExpenseListDatabaseModel(db)

  // Helpers to serialize json objects
  // TODO: Check if all Reads/Writes needed
  implicit val userDataReads = Json.reads[UserData]
  implicit val userDataWrites = Json.writes[UserData]
  implicit val expenseDataReads = Json.reads[ExpenseData]
  implicit val expenseDataWrites = Json.writes[ExpenseData]

  def withJsonBody[A](f: A => Future[Result])(implicit request: Request[AnyContent], reads: Reads[A]): Future[Result] = {
    request.body.asJson.map { body =>
      Json.fromJson[A](body) match {
        case JsSuccess(a, path) => f(a)
        case e @ JsError(_) => Future.successful(Redirect(routes.ExpensesList.login))
      }
    }.getOrElse(Future.successful(Redirect(routes.ExpensesList.login)))
  }

  def withSessionUsername(f: String => Future[Result])(implicit request: Request[AnyContent]): Future[Result] = {
    request.session.get("username").map(f).getOrElse(Future.successful(Ok(Json.toJson(Seq.empty[String]))))
  }

  def withSessionUserid(f: Int => Future[Result])(implicit request: Request[AnyContent]): Future[Result] = {
    request.session.get("userid").map(userid => f(userid.toInt)).getOrElse(Future.successful(Ok(Json.toJson(Seq.empty[String]))))
  }

  def load = Action { implicit request =>
    println("--- load ---")
    Ok(views.html.home())
  }

  def login = Action { implicit request =>
    println("--- login ---")
    Ok(views.html.login())
  }

  def validate = Action.async { implicit request =>
    println("--- validate ---")
    withJsonBody[UserData] { ud =>
      model.validateUser(ud.username, ud.password).map {
        case Some(userid) =>
          Ok(Json.toJson(true))
            .withSession("username" -> ud.username, "userid" -> userid.toString, "csrfToken" -> play.filters.csrf.CSRF.getToken.map(_.value).getOrElse(""))
        case None =>
          Ok(Json.toJson(false))
      }
    }
  }

  def createUser = Action.async { implicit request =>
    println("--- createUser ---")
    withJsonBody[UserData] { ud =>
      model.createUser(ud.username, ud.password).map { ouserId =>
        ouserId match {
          case Some(userid) => {
            println(userid)
            Ok(Json.toJson(true))
              .withSession("username" -> ud.username, "userid" -> userid.toString, "csrfToken" -> play.filters.csrf.CSRF.getToken.map(_.value).getOrElse(""))
          }
          case None =>
            Ok(Json.toJson(false))
        }
      }
    }
  }

  def logout = Action { implicit request =>
    println("--- logout ---")
    Ok(Json.toJson(true)).withSession(request.session - "username")
  }

  def expenseList = Action.async { implicit request =>
    println("--- expenseList ---")
    withSessionUsername { username =>
      println("--- Getting expenses ---")
      model.getExpenses(username).map(expenses => {
        println(s"-------- Expenses: ${expenses}")
        Ok(Json.toJson(expenses))
      })
    }
  }

  def addExpense = Action.async { implicit request =>
    println("--- addExpense ---")
    withSessionUserid { userid =>
      println(s"*** userId: ${userid}")
      withJsonBody[ExpenseData] { expense =>
        println(s"*** expenseData: ${expense}")
        model.addExpense(userid, expense).map(count => Ok(Json.toJson(count > 0)))
      }
    }
  }

  def deleteExpense = Action.async { implicit request =>
    println("--- deleteExpense ---")
    withSessionUsername { username =>
      withJsonBody[Int] { expenseId =>
        model.removeExpense(expenseId).map(removed => Ok(Json.toJson(removed)))
      }
    }
  }

}