package com.silvestre.flink

import java.util.Properties

import com.silvestre.flink.operators.{FilterTweet, SentimentAnalysisWindowFunction, TweetParser}
import com.typesafe.config.ConfigFactory
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.sink.{PrintSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.twitter.TwitterSource

object ApplicationTwitter {

  def main(args: Array[String]): Unit = {
    implicit val settings: Settings = new Settings(ConfigFactory.load())
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

    import settings.twitterCredentials._

    val props = new Properties
    props.setProperty(TwitterSource.CONSUMER_KEY, consumerKey)
    props.setProperty(TwitterSource.CONSUMER_SECRET, consumerSecret)
    props.setProperty(TwitterSource.TOKEN, token)
    props.setProperty(TwitterSource.TOKEN_SECRET, tokenSecret)
    val streamSource = env.addSource(new TwitterSource(props))

    val app = new ApplicationTwitter()
    app.startPipeline(streamSource, new PrintSinkFunction[SentimentOnTwitter]())

    env.execute("covid19-sentiment-analysis")
  }
}

class ApplicationTwitter(implicit settings: Settings) {

  def startPipeline(stream: DataStream[String], sink: SinkFunction[SentimentOnTwitter]): Unit = {
    stream
      .map(new TweetParser)
      .filter(new FilterTweet) // Filter tweets that contain must contains one of the words
      .windowAll(TumblingProcessingTimeWindows.of(Time.minutes(settings.flinkSettings.windowInMinutes.toMinutes)))
      .process(new SentimentAnalysisWindowFunction) // Aggregate based on the sentiment
      .addSink(sink)
  }
}

