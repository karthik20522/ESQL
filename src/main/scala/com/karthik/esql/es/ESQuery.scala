package com.karthik.esql.es

import scala.util.Try._
import com.karthik.esql.sql.Query
import com.karthik.esql.sql._
import org.elasticsearch.client._
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.action.search._

class ESQuery(esClient: Client) {

  def generateSearchRequest(query: Query): SearchRequestBuilder = {
    val srBuilder: SearchRequestBuilder = esClient.prepareSearch(query.from.table)
    fields(query) foreach { srBuilder.addField(_) }
    srBuilder
  }

  private def fields(query: Query): List[String] = {
    query.operation match {
      case d: Select => d.fields.toList
      case x => throw new Exception("Unknown operation: " + x)
    }
  }
}