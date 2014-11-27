package com.karthik.esql

import org.scalatest.matchers.ShouldMatchers
import org.scalatest._
import com.log4p.sqldsl._
import com.karthik.esql._
import org.elasticsearch.client.Client
import org.scalatest.mock.MockitoSugar

class ESQuerySpec extends FunSpec with ShouldMatchers with MockitoSugar {

  val esClientMock = mock[Client]

  val esQuery = new ESQuery(esClientMock)

  describe("given a sql string") {
    val sql = "select id from assets"
    it("should be parsed into an Query object") {
      val query: Query = esQuery.parseSQL(sql)
      query.operation should be(Select("id"))
      query.from should be(From("assets"))
    }
  }

  describe("given a sql string with order clause") {
    val sql = "select id from assets order by id desc"
    it("should be parsed into an Query object with order field") {
      val query: Query = esQuery.parseSQL(sql)
      query.operation should be(Select("id"))
      query.from should be(From("assets"))
      query.order should be(Option(Desc("id")))
    }
  }

  describe("given a sql string with limit clause") {
    val sql = "select id from assets order by id desc limit 22"
    it("should be parsed into an Query object and have limit constraint") {
      val query: Query = esQuery.parseSQL(sql)
      query.operation should be(Select("id"))
      query.from should be(From("assets"))
      query.order should be(Option(Desc("id")))
      query.limit should be(Option(Limit(22)))

      println(query)
    }
  }

  describe("given a sql string with duplicate white spaces") {
    val sql = "select    id from assets order by id       desc"
    it("should be parsed into an Query object with order field") {
      val query: Query = esQuery.parseSQL(sql)
      query.operation should be(Select("id"))
      query.from should be(From("assets"))
      query.order should be(Option(Desc("id")))
    }
  }

  describe("given a bad sql string ") {
    val sql = "select from assets order by id desc"
    it("throw an exception") {
      intercept[Exception] {
        esQuery.parseSQL(sql)
      }
    }
  }
}