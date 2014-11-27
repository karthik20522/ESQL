package com.karthik.esql

import com.log4p.sqldsl._
import scala.util.Try._
import org.elasticsearch.node.NodeBuilder._
import org.elasticsearch.client.Client
import org.elasticsearch.index.query._
import org.elasticsearch.action.search._

class ESQuery(esClient: Client) {

  val sqlParser = new SQLParser()

  def parseSQL(q: String): Query = sqlParser.parse(q).get

  def buildRequest(sql: String): String = {
    val sqlQuery: Query = parseSQL(sql)
    val srBuilder: SearchRequestBuilder = esClient.prepareSearch(sqlQuery.from.table)

    ""
  }
}