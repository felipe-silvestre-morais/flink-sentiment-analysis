package com.silvestre

import java.time.Instant

package object flink {
  case class Tweet(text: String, lang: String)
  case class SentimentOnTwitter(totalTweets: Long, positives: Long, negatives: Long, neutral: Long, time: Instant)
}
