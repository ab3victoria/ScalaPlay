package models

import java.time.{LocalDate, LocalTime}
import scala.xml.Elem

/**
 * TODO: fill
 * @param name
 * @param category
 * @param cost
 * @param date
 * @param time
 */
class Expense (var name: String, var category: String, var cost: Double, var date: LocalDate, var time: LocalTime) {

  /**
   * TODO: fill
   */
  override def toString: String = {
    super.toString
    s"Name: ${name}, category: ${category}, cost: ${cost}, date: ${date}, time: ${time}"
  }

  /**
   * TODO: fill
   * @return
   */
  def toXML: Elem = {
    <expense>
      <name>{name}</name>
      <category>{category}</category>
      <cost>{cost}</cost>
      <date>{date}</date>
      <time>{time}</time>
    </expense>
  }
}

/**
 * TODO: fill
 */
object Expense {
  def fromXML(node: scala.xml.NodeSeq): Expense =
    new Expense(
      name = (node \ "name").text,
      category = (node \ "category").text,
      cost = (node \ "cost").text.toDouble,
      date = LocalDate.parse((node \ "date").text),
      time = LocalTime.parse((node \ "time").text)
    )
}
