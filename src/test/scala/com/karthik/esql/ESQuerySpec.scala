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
    describe("select * from assets") {
      val sql = "select * from assets"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString() should include(""""size" : 75""")      
    }

    describe("select masterId, primaryLanguage from assets") {
      val sql = "select masterId, primaryLanguage from assets"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString() should include(""""fields" : [ "masterId", "primaryLanguage" ]""")      
    }

    describe("select primaryLanguage from assets order by version asc") {
      val sql = "select primaryLanguage from assets order by version asc"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString() should include(""""fields" : "primaryLanguage""")
      esQueryFields.toString() should include(""""order" : "asc""")      
    }

    describe("select primaryLanguage from assets where primaryLanguage = \"en-US\"") {
      val sql = "select masterId from assets where primaryLanguage = \"en-US\""
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString() should include(""""fields" : "masterId""")
      esQueryFields.toString should include("""primaryLanguage:(\"en-US\")""")
    }

    describe("select * from assets where version = 2") {
      val sql = "select * from assets where version = 2"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString should include("version:2")
    }

    describe("""select * from assets where primaryLanguage = "en-US" and version = 3""") {
      val sql = """select * from assets where primaryLanguage = "en-US" and version = 3"""
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)      
      esQueryFields.toString should include("""(primaryLanguage:(\"en-US\") AND version:3)""")
    }

    describe("select * from assets where version = 3 or version = 4") {
      val sql = "select * from assets where version = 19 or version = 3"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)      
      esQueryFields.toString should include("""(version:19 OR version:3)""")
    }

    describe("""select * from assets where primaryLanguage = "en-US" and version = 19 or version = 4""") {
      val sql = """select * from assets where primaryLanguage = "en-US" and version = 19 or version = 4"""
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)      
      esQueryFields.toString should include("""((primaryLanguage:(\"en-US\") AND version:19) OR version:4)""")
    }

    describe("""select * from assets where (primaryLanguage = "en-US" and version = 19) or version = 4""") {
      val sql = """select * from assets where (primaryLanguage = "en-US" and version = 19) or version = 4"""
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)      
      esQueryFields.toString should include("""((primaryLanguage:(\"en-US\") AND version:19) OR version:4)""")
    }

    describe("""select * from assets where primaryLanguage = "en-US" and (version = 19 or version = 20)""") {
      val sql = """select * from assets where primaryLanguage = "en-US" and (version = 19 or version = 20)"""
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString should include("""(primaryLanguage:(\"en-US\") AND (version:19 OR version:20))""")
    }

    describe("select * from assets where version > 19") {
      val sql = "select * from assets where version > 19"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)      
      esQueryFields.toString should include("version:[19 TO *]")
    }

    describe("select * from assets where version < 19") {
      val sql = "select * from assets where version < 19"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString should include("version:[* TO 19]")
    }

    describe("select * from assets where version >= 19") {
      val sql = "select * from assets where version >= 19"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)      
      esQueryFields.toString should include("version:[19 TO *]")
    }

    describe("select * from assets where version <= 19") {
      val sql = "select * from assets where version <= 19"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString should include("version:[* TO 19]")
    }

    describe("""select * from assets where version > 22 and primaryLanguage = "en-US"""") {
      val sql = """select * from assets where version > 22 and primaryLanguage = "en-US""""
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString should include("""(version:[22 TO *] AND primaryLanguage:(\"en-US\"))""")
    }

    describe("select masterId, version from assets where primaryLanguage = \"en-US\" and (assetManagement.readyForSale = true or version = 3)") {
      val sql = "select masterId, version from assets where primaryLanguage = \"en-US\" and (assetManagement.readyForSale = true or version = 3)"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString should include("""primaryLanguage:(\"en-US\") AND (assetManagement__readyForSale:true OR version:3)""")
    }

    describe("select * from users limit 22") {
      val sql = "select * from users limit 22"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)      
      esQueryFields.toString should include(""""size" : 22""")    
    }

    describe("select masterId from assets where version > 21 order by masterId desc limit 22") {
      val sql = "select masterId from assets where version > 21 order by masterId desc limit 22"
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString should include(""""size" : 22""")    
      esQueryFields.toString() should include(""""fields" : "masterId""")
      esQueryFields.toString() should include(""""order" : "desc""")
    }

    describe("select * from assets where assetMetadata.data.description like \"RESTRICTED\"") {
      val sql = "select * from assets where assetMetadata.data.description like \"RESTRICTED\""
      val sqlQuery = sqlQueryParser.parse(sql).get
      val esQueryFields = esQuery.generateSearchRequest(sqlQuery)
      esQueryFields.toString should include (""""query" : "assetMetadata__data__description:(RESTRICTED*)""")
    }

  }
  node.close()
}