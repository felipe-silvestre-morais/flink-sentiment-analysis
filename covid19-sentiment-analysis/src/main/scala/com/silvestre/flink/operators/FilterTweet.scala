package com.silvestre.flink.operators

import com.silvestre.flink.{Settings, Tweet}
import org.apache.flink.api.common.functions.FilterFunction

class FilterTweet(implicit settings: Settings) extends FilterFunction[Tweet] {
  override def filter(value: Tweet): Boolean = {
    settings.sampleSettings.postMustContains.exists(value.text.contains)
  }
}