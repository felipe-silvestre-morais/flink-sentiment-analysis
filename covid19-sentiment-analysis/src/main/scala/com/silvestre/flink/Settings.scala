package com.silvestre.flink

import com.typesafe.config.Config
import scala.collection.JavaConverters._
import scala.concurrent.duration.{Duration, FiniteDuration}

object Settings {
  implicit class RichConfig(config: Config) {
    def getScalaDuration(path: String): FiniteDuration = Duration.fromNanos(config.getDuration(path).toNanos)
  }
}

class Settings(config: Config) extends Serializable {
  import Settings._

  val env: String = sys.env.getOrElse("SCALA_ENV", sys.env.getOrElse("APP_ENV", "laptop"))

  val isDevelopmentEnvironment = env == "laptop"

  val twitterCredentials = TwitterCredentials(
    config.getString("twitter.consumer-key"),
    config.getString("twitter.consumer-secret"),
    config.getString("twitter.token"),
    config.getString("twitter.token-secret"),
  )

  val flinkSettings = FlinkSettings(config.getScalaDuration("flink.window-minutes"))

  val sampleSettings = Sample(
    config.getStringList("sample.post-must-contains").asScala.toList,
    config.getStringList("sample.positive-sentiment-must-contains").asScala.toList,
    config.getStringList("sample.negative-sentiment-must-contains").asScala.toList,
  )
}

case class TwitterCredentials(consumerKey:String, consumerSecret: String, token: String, tokenSecret: String)
case class FlinkSettings(windowInMinutes: FiniteDuration)
case class Sample(postMustContains: List[String], positiveSentimentMustContain: List[String], negativeSentimentMustContain: List[String])

