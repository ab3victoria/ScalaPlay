package models

object CodeGen extends App {
  slick.codegen.SourceCodeGenerator.run (
    "slick.jdbc.PostgresProfile",
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/expensesdb?user=shlomi&password=@WSXcde32pg",
    "C:\\Users\\Shlomi\\Desktop\\ScalaPlay\\app\\",
    "models", None, None, true, false
  )
}