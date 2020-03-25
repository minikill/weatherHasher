package inc.sad.weatherHasher

case class Config(applicationConfig: ApplicationConfig, kafkaConfig: KafkaConfig)

case class ApplicationConfig(consumerPoolSize: Int)

case class KafkaConfig(sourceTopic: String,
                       targetTopic: String,
                       applicationId: String,
                       commitInterval: String,
                       autoOffsetReset: String,
                       maxPoolRecords: String,
                       bootstrapServers: List[String])
