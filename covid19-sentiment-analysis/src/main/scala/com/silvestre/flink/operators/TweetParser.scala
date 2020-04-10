package com.silvestre.flink.operators

import com.silvestre.flink.Tweet
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

import scala.util.Try

class TweetParser extends MapFunction[String, Tweet] {
  override def map(value: String): Tweet = {
    val jsonParser = new ObjectMapper()
    val node = jsonParser.readValue(value, classOf[JsonNode])

    val lang = Try { node.get("user").get("lang").asText().toLowerCase }
    val text = Try { node.get("text").asText().toLowerCase }

    Tweet(text.getOrElse(""), lang.getOrElse("null"))
  }
}