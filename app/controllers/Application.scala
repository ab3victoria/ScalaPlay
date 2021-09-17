package controllers

import javax.inject._


import play.api.mvc._
import play.api.i18n._

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Your Expenses List"))
  }

  def product(prodName: String, prodCategory: String, prodPrice: Double)= Action{
    Ok(s"Product name is: $prodName, Category: $prodCategory, Price:$prodPrice")
  }

}