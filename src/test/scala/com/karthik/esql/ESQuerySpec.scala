package com.karthik.esql

import org.scalatest.matchers.ShouldMatchers
import org.scalatest._
import com.karthik.esql.es._
import com.karthik.esql.sql._
import org.elasticsearch.client._

import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import com.karthik.esql.sql.SQLParser
import org.elasticsearch.node.NodeBuilder._

class ESQuerySpec extends FunSpec with ShouldMatchers with MockitoSugar {

  private lazy val node = nodeBuilder().local(true).build
  val esClient: Client = node.client
  val esQuery = new ESQuery(esClient)
  val sqlQueryParser = new SQLParser

  describe("given a sql query") {
    describe("(with multiple select fields)") {
      val sql = "select name from users order by name asc"
      it("should be able to extract the fields into Request object") {
        val sqlQuery = sqlQueryParser.parse(sql).get
        val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
        esQueryFields.toString() should include(""""fields" : "name""")
      }
    }
    /*describe("(when predicate contains equals and greatherThan clauses)") {
      val sql = """select name from users where (age = 20 or age = 30) and name = "peter""""
      it("should be parsed into an AND object with correct equal and greaterThan clauses") {
        val query = sqlQueryParser.parse(sql).get
        val sqlQ = esQuery.expandWhere(query)
        println(sqlQ)
      }
    }*/
  }
}