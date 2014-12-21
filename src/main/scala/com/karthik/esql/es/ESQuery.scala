package com.karthik.esql.es

import scala.util.Try._
import com.karthik.esql.sql.Query
import com.karthik.esql.sql._
import org.elasticsearch.client._
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.action.search._
import org.elasticsearch.search.sort.SortOrder._

class ESQuery(esClient: Client) {

  def generateSearchRequest(query: Query): SearchRequestBuilder = {

    //SET INDEX NAME
    val srBuilder: SearchRequestBuilder = esClient.prepareSearch(query.from.table)

    //SET RESULT SIZE
    srBuilder.setSize(query.limit.map(_.count).getOrElse(75))

    //ADD ORDER
    query.order.map { f =>
      f match {
        case d: Desc => d.field.toList foreach { srBuilder.addSort(_, DESC) }
        case a: Asc => a.field.toList foreach { srBuilder.addSort(_, ASC) }
      }
    }

    //ADD FIELDS
    getFields(query) foreach { srBuilder.addField(_) }

    //ADD FILTER QUERY
    srBuilder.setQuery(QueryBuilders.queryString(expandWhere(query).getOrElse("")))

    srBuilder
  }

  private def getFields(query: Query): List[String] = query.operation match {
    case d: Select => d.fields.toList
    case x => throw new Exception("Unknown operation: " + x)
  }

  private def expandWhere(query: Query): Option[String] = {
    if (query.where.isEmpty || query.where.get.clauses.isEmpty)
      None
    else
      Option("%s".format(query.where.get.clauses.map(expandClause(_)).mkString(" ")))
  }

  private def expandClause(clause: Clause): String = clause match {
    case StringEquals(field, value) => "%s:(%s)".format(field, quote(value))
    case BooleanEquals(field, value) => "%s:%s".format(field, value)
    case NumberEquals(field, value) => "%s:%s".format(field, value)
    case in: In => "%s in (%s)".format(in.field, in.values.map(quote(_)).mkString(","))
    case and: And => "(%s AND %s)".format(expandClause(and.lClause), expandClause(and.rClause))
    case or: Or => "(%s OR %s)".format(expandClause(or.lClause), expandClause(or.rClause))
    case _ => throw new IllegalArgumentException("Clause %s not implemented".format(clause))
  }

  private def quote(value: String) = "\"%s\"".format(value)
}