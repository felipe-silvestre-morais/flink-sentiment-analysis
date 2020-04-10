package com.silvestre.flink.operators

import java.time.Instant

import com.silvestre.flink.{SentimentOnTwitter, Settings, Tweet}
import org.apache.flink.streaming.api.scala.function
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class SentimentAnalysisWindowFunction(implicit settings: Settings) extends function.ProcessAllWindowFunction[Tweet, SentimentOnTwitter, TimeWindow] {

  override def process(context: Context, elements: Iterable[Tweet], out: Collector[SentimentOnTwitter]): Unit = {
    val windowSize = elements.size
    var positives = 0L
    var negatives = 0L

    elements.foreach((tweet: Tweet) => {
      val isPositive = settings.sampleSettings.positiveSentimentMustContain.exists(tweet.text.contains)
      val isNegative = settings.sampleSettings.negativeSentimentMustContain.exists(tweet.text.contains)

      if (isPositive ^ isNegative) {
        if (isPositive) positives += 1
        if (isNegative) negatives += 1
      }
    })

    val neutrals = windowSize - (positives + negatives)

    val metric = SentimentOnTwitter(windowSize, positives, negatives, neutrals, Instant.now())
    out.collect(metric)
  }
}