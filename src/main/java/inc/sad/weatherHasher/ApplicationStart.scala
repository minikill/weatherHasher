package inc.sad.weatherHasher

import java.util.Properties

import ch.hsr.geohash.GeoHash
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{Consumed, Produced}
import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsBuilder, StreamsConfig}
import org.apache.log4j.Logger
import pureconfig.generic.auto._

/**
 * It's only method for streaming processing weather data
 * This method calculates geohash bases on longitude and latitude
 * gets rid of doubled columns and sends it into kafka output topic
 */

object ApplicationStart extends App {

  val LOG = Logger.getLogger(this.getClass)

  // Config loading by path from environment variable
  val conf = ConfigService.loadConfiguration(scala.util.Properties.envOrNone("WEATHERHASHER_CONFIG").get)

  // Properties for kafka
  val props = new Properties
  props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, conf.kafkaConfig.applicationId)
  props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, conf.kafkaConfig.bootstrapServers.mkString(","))
  props.setProperty(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, conf.kafkaConfig.commitInterval)
  props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, conf.kafkaConfig.autoOffsetReset)
  props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, conf.kafkaConfig.maxPoolRecords)

  val builder = new StreamsBuilder

  // Record transforming processing
  builder
    .stream(conf.kafkaConfig.sourceTopic,
      Consumed.`with`(Serdes.String(), Serdes.String())
    )
    .map[String, String] {
      (_: String, value: String) => {
        var nKey = ""
        var nValue = ""
        val record = List.from(value.split(","))
        try {
          val geoHash = GeoHash.geoHashStringWithCharacterPrecision(
            record(1).toDouble,
            record(0).toDouble,
            conf.applicationConfig.geohashPrecision
          )
          nKey = geoHash + "-" + record(4)
          nValue = record.slice(2, 4).mkString(",")
        } catch {
          case e: Exception =>
            LOG.error(e)
        }
        KeyValue.pair(nKey, nValue)
      }
    }
    .to(conf.kafkaConfig.targetTopic,
      Produced.`with`(Serdes.String(), Serdes.String())
    )

  // Stream processing start
  val streams = new KafkaStreams(builder.build, props)
  streams.start()
  LOG.info("Stream started")

  Runtime.getRuntime.addShutdownHook(new Thread(() => {
    try {
      streams.close
      LOG.info("Stream closed")
    } catch {
      case e: Exception =>
        LOG.error(e)
    }
  }))

}