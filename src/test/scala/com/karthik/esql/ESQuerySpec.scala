package com.karthik.esql

import org.scalatest.matchers.ShouldMatchers
import org.scalatest._
import com.karthik.esql.es._
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
  }
}