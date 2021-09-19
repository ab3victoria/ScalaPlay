package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Expenses.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Expenses
   *  @param expenseId Database column expense_id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4)
   *  @param title Database column title SqlType(varchar), Length(200,true)
   *  @param cost Database column cost SqlType(numeric)
   *  @param date Database column date SqlType(varchar), Length(10,true) */
  case class ExpensesRow(expenseId: Int, userId: Int, title: String, cost: scala.math.BigDecimal, date: String)
  /** GetResult implicit for fetching ExpensesRow objects using plain SQL queries */
  implicit def GetResultExpensesRow(implicit e0: GR[Int], e1: GR[String], e2: GR[scala.math.BigDecimal]): GR[ExpensesRow] = GR{
    prs => import prs._
    ExpensesRow.tupled((<<[Int], <<[Int], <<[String], <<[scala.math.BigDecimal], <<[String]))
  }
  /** Table description of table expenses. Objects of this class serve as prototypes for rows in queries. */
  class Expenses(_tableTag: Tag) extends profile.api.Table[ExpensesRow](_tableTag, "expenses") {
    def * = (expenseId, userId, title, cost, date) <> (ExpensesRow.tupled, ExpensesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(expenseId), Rep.Some(userId), Rep.Some(title), Rep.Some(cost), Rep.Some(date))).shaped.<>({r=>import r._; _1.map(_=> ExpensesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column expense_id SqlType(serial), AutoInc, PrimaryKey */
    val expenseId: Rep[Int] = column[Int]("expense_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column title SqlType(varchar), Length(200,true) */
    val title: Rep[String] = column[String]("title", O.Length(200,varying=true))
    /** Database column cost SqlType(numeric) */
    val cost: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("cost")
    /** Database column date SqlType(varchar), Length(10,true) */
    val date: Rep[String] = column[String]("date", O.Length(10,varying=true))

    /** Foreign key referencing Users (database name expenses_user_id_fkey) */
    lazy val usersFk = foreignKey("expenses_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Expenses */
  lazy val Expenses = new TableQuery(tag => new Expenses(tag))

  /** Entity class storing rows of table Users
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(varchar), Length(30,true)
   *  @param password Database column password SqlType(varchar), Length(300,true) */
  case class UsersRow(id: Int, username: String, password: String)
  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[UsersRow] = GR{
    prs => import prs._
    UsersRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, "users") {
    def * = (id, username, password) <> (UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(username), Rep.Some(password))).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(varchar), Length(30,true) */
    val username: Rep[String] = column[String]("username", O.Length(30,varying=true))
    /** Database column password SqlType(varchar), Length(300,true) */
    val password: Rep[String] = column[String]("password", O.Length(300,varying=true))
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
