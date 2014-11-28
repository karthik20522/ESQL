package com.log4p.sqldsl

import org.scalatest.matchers.ShouldMatchers
import org.scalatest._

class SQLParserSpec extends FunSpec with ShouldMatchers {
  val p = new SQLParser
  /*describe("given a sql string with an order clause") {
    describe("(when direction is asc)") {
      val sql = "select name from users order by name asc"
      it("should be parsed into an Asc object containing the given field") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.order should be(Option(Asc("name")))
      }
    }
  }
  describe("given a sql string with a single where clause") {
    describe("(when equals predicate with string literal)") {
      val sql = "select name from users where name = \"peter\""
      it("should be parsed into an StringEquals object containing the given field and value") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(StringEquals("name", "peter"))
      }
    }
    describe("(when equals predicate with numeric literal)") {
      val sql = """select age from users where age = 30"""
      it("should be parsed into an NumberEquals object containing the given field and value") {
        val query = p.parse(sql).get
        query.operation should be(Select("age"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(NumberEquals("age", 30))
      }
    }
  }*/
  describe("given a sql string with a combined where clause") {
    /*describe("(when equals predicate contains and)") {
      val sql = """select name from users where name = "peter" and age = 30"""
      it("should be parsed into an And object containing to correct subclauses") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(And(StringEquals("name", "peter"), NumberEquals("age", 30)))
      }
    }
    describe("(when equals predicate contains or)") {
      val sql = """select name from users where age = 20 or age = 30"""
      it("should be parsed into an Or object containing to correct subclauses") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(Or(NumberEquals("age", 20), NumberEquals("age", 30)))
      }
    }
    describe("(when equals predicate contains multiple combined clauses)") {
      val sql = """select name from users where name = "peter" and age = 20 or age = 30"""
      it("should be parsed into an Or object containing and And object and and Equals predicate") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(Or(And(StringEquals("name", "peter"), NumberEquals("age", 20)), NumberEquals("age", 30)))
        println(AnsiSqlRenderer.sql(query))
      }
    }
    describe("(when greater than predicate is provided)") {
      val sql = """select name from users where age > 30"""
      it("should be parsed into an GreaterThan object") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(GreaterThan("age", 30))
      }
    }
    describe("(when predicate contains equals and greatherThan clauses)") {
      val sql = """select name from users where age > 30 and name = "peter""""
      it("should be parsed into an AND object with correct equal and greaterThan clauses") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(And(GreaterThan("age", 30), StringEquals("name", "peter")))
      }
    }
    describe("(when equals predicate contains multiple combined clauses where the presedence is dictated by parens)") {
      val sql = """select name,age from users where name = "peter" and (active = true or age = 30)"""
      it("should be parsed into an Or object containing and And object and and Equals predicate") {
        val query = p.parse(sql).get
        query.operation should be(Select(List("name", "age"): _*))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(And(StringEquals("name", "peter"), Or(BooleanEquals("active", true), NumberEquals("age", 30))))
        println(AnsiSqlRenderer.sql(query))
      }
    }
    describe("(when just limit filter is provided)") {
      val sql = "select name from users limit 22"
      it("should be parsed into an limit object") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.limit should be(Option(Limit(22)))
      }
    }
    describe("(when limit filter along with order and where clause)") {
      val sql = "select name from users where age > 30 order by age desc limit 22"
      it("should be parsed into an limit object") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(GreaterThan("age", 30))
        query.order should be(Option(Desc("age")))
        query.limit should be(Option(Limit(22)))
      }
    }
    describe("(when predicate contains like clauses)") {
      val sql = """select name from users where age > 30 and name like "peter""""
      it("should be parsed into an AND object with correct like and greaterThan clauses") {
        val query = p.parse(sql).get
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(And(GreaterThan("age", 30), Like("name", "peter")))
      }
    }*/
    describe("(when predicate contains count clauses)") {
      val sql = """select count(name) from users where age > 30 and name like "peter""""
      it("should be parsed into an AND object with correct like and greaterThan clauses") {
        val query = p.parse(sql).get
        println(query)
        query.operation should be(Select("name"))
        query.from should be(From("users"))
        query.where.get.clauses.head should be(And(GreaterThan("age", 30), Like("name", "peter")))
      }
    }
  }

  /*  describe("give invalid sql string") {
    describe("(when missing select statement is provided)") {
      val sql = "select from users"
      it("should throw an exception") {
        intercept[Exception] {
          val query = p.parse(sql).get
        }
      }
    }*/
  /*describe("(when query provided is in all caps)") {
      val sql = "SELECT AGE FROM USERS"
      it("should parse") {
        val query = p.parse(sql).get
        query.operation should be(Select("AGE"))
        query.from should be(From("USERS"))
      }
    }*/
  //}
}