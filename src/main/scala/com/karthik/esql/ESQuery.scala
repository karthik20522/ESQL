/*package com.karthik.esql

import scala.util.Try._
import org.elasticsearch.node.NodeBuilder._
import org.elasticsearch.client.Client
import org.elasticsearch.index.query._
import org.elasticsearch.action.search._
import com.karthik.esql.sql.SQLParser
import com.karthik.esql.sql.Query
import org.elasticsearch.index.query.QueryBuilders._

class ESQuery(esClient: Client) {

  val sqlParser = new SQLParser()

  def parseSQL(q: String): Query = sqlParser.parse(q).get

  def buildRequest(sql: String): String = {
    val sqlQuery: Query = parseSQL(sql)
    val srBuilder: SearchRequestBuilder = esClient.prepareSearch(sqlQuery.from.table)
    val cc = QueryBuilders.queryString("").defaultOperator(QueryStringQueryBuilder.Operator.AND)
    val dd = srBuilder.addFields("")

    ""
  }
}*/ 