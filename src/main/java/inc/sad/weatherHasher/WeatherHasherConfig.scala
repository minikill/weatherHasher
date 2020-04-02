package inc.sad.weatherHasher

/**
 *  A few case classes for describing config structure for PureConfig
 */

case class WeatherHasherConfig(applicationConfig: ApplicationConfig, kafkaConfig: KafkaConfig)

case class ApplicationConfig(geohashPrecision: Int,
                             consumerPoolSize: Int)

case class KafkaConfig(sourceTopic: String,
                       targetTopic: String,
                       applicationId: String,
                       commitInterval: String,
                       autoOffsetReset: String,
                       maxPoolRecords: String,
                       bootstrapServers: List[String])
