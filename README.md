# Flink-sentiment-analysis

A Flink application for sentiment analysis on a specific topic, in this case, Covid19.
The application consumes Twitter as the source, aggregates the positive and negative sentiments based on words present in a tweet.

The data flow:
Twitter -> Flink application -> TweetParser -> TweetFilter -> Window aggregation -> Sink

The aggreation of a positive or negative sentiment is based on the presence of one of the words previously defined.
In our case, a tweet is counted as a negative sentiment if it contains words like: death, new cases, fatalities, ...
In the same way, for a positive sentiment, a tweet must contains words like: recovery, healed, survived, ...

This project is just an example how we can analyze social media sentiment based on simple rules.

The data currently is just printted on the terminal, if anyone wants to contribute to this project, specially building a front end, I would be more than help to sink the data in a datastore like Redis or MySQL.

Thanks, enjoy.

