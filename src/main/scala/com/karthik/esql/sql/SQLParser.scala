/*
 * Original Author to SQL Parser
 * https://github.com/p3t0r/scala-sql-dsl
 * This is a custom version of the above source
 */

package com.karthik.esql.sql

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.syntactical._
import com.karthik.esql._

class SQLParser extends JavaTokenParsers {

  def query: Parser[Query] = (selectAll | count | defaultSelect) ~ from ~ opt(where) ~ opt(order) ~ opt(limit) ^^ {
    case operation ~ from ~ where ~ order ~ limit => Query(operation, from, where, order, limit)
  }

  def selectAll: Parser[Operation] = "select" ~ "*" ^^ (f => Select("_all"))

  def defaultSelect: Parser[Operation] = "select" ~ repsep(ident, ",") ^^ {
    case "select" ~ f => Select(f: _*)
    case _ => throw new IllegalArgumentException("Operation not implemented")
  }

  def limit: Parser[Limit] = "limit" ~> wholeNumber ^^ (f => Limit(Integer.parseInt(f)))

  def count: Parser[Count] = "select" ~ "count" ~> "(" ~> ident <~ ")" ^^ { case exp => Count(exp) }

  def from: Parser[From] = "from" ~> ident ^^ (From(_))

  def where: Parser[Where] = "where" ~> rep(clause) ^^ (Where(_: _*))

  def clause: Parser[Clause] = (predicate | parens) * (
    "and" ^^^ { (a: Clause, b: Clause) => And(a, b) } |
    "or" ^^^ { (a: Clause, b: Clause) => Or(a, b) })

  def parens: Parser[Clause] = "(" ~> clause <~ ")"

  def predicate = (
    ident ~ "=" ~ boolean ^^ { case f ~ "=" ~ b => BooleanEquals(f, b) }
    | ident ~ "=" ~ stringLiteral ^^ { case f ~ "=" ~ v => StringEquals(f, stripQuotes(v)) }
    | ident ~ ("like" | "LIKE") ~ stringLiteral ^^ { case f ~ ("like" | "LIKE") ~ v => Like(f, stripQuotes(v)) }
    | ident ~ "=" ~ wholeNumber ^^ { case f ~ "=" ~ i => NumberEquals(f, i.toInt) }
    | ident ~ "<" ~ wholeNumber ^^ { case f ~ "<" ~ i => LessThan(f, i.toInt) }
    | ident ~ "<=" ~ wholeNumber ^^ { case f ~ "<=" ~ i => LessThanEquals(f, i.toInt) }
    | ident ~ ">" ~ wholeNumber ^^ { case f ~ ">" ~ i => GreaterThan(f, i.toInt) }
    | ident ~ ">=" ~ wholeNumber ^^ { case f ~ ">=" ~ i => GreaterThanEquals(f, i.toInt) })

  def boolean = ("true" ^^^ (true) | "false" ^^^ (false))

  def order: Parser[Direction] = {
    "order" ~> "by" ~> ident ~ ("asc" | "desc") ^^ {
      case f ~ "asc" => Asc(f)
      case f ~ "desc" => Desc(f)
    }
  }

  def stripQuotes(s: String) = s.substring(1, s.length - 1)

  def parse(sql: String): Option[Query] = {
    parseAll(query, sql) match {
      case Success(r, q) => Option(r)
      case x => println(x); None
    }
  }
}