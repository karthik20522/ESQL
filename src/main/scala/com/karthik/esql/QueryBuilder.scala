/*
 * Original Author to SQL Parser
 * https://github.com/p3t0r/scala-sql-dsl
 * This is a custom version of the above source
 */
package com.karthik.esql

case class Query(val operation: Operation, val from: From, val where: Option[Where], val order: Option[Direction] = None, val limit: Option[Limit] = None) {
  def order(dir: Direction): Query = this.copy(order = Option(dir))
}

abstract class Operation {
  def from(table: String) = From(table, Option(this))
}
case class Select(val fields: String*) extends Operation
case class Count(val field: String) extends Operation
case class From(val table: String, val operation: Option[Operation] = None) {
  def where(clauses: Clause*): Query = Query(operation.get, this, Option(Where(clauses: _*)))
}

case class Where(val clauses: Clause*)

abstract class Clause {
  def and(otherField: Clause): Clause = And(this, otherField)
  def or(otherField: Clause): Clause = Or(this, otherField)
}

case class StringEquals(val f: String, val value: String) extends Clause
case class NumberEquals(val f: String, val value: Number) extends Clause
case class BooleanEquals(val f: String, val value: Boolean) extends Clause
case class GreaterThan(val f: String, val value: Number) extends Clause
case class LessThan(val f: String, val value: Number) extends Clause
case class GreaterThanEquals(val f: String, val value: Number) extends Clause
case class LessThanEquals(val f: String, val value: Number) extends Clause
case class Like(val f: String, val value: String) extends Clause

case class In(val field: String, val values: String*) extends Clause
case class And(val lClause: Clause, val rClause: Clause) extends Clause
case class Or(val lClause: Clause, val rClause: Clause) extends Clause

abstract class Direction
case class Asc(field: String) extends Direction
case class Desc(field: String) extends Direction

case class Limit(count: Integer)