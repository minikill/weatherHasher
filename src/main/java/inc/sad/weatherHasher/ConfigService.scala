package inc.sad.weatherHasher

import java.io.File

import javax.naming.ConfigurationException
import pureconfig.ConfigSource
import pureconfig.generic.auto._

/**
 *
 * Object for loading config files by purgeConfig
 *
 */

object ConfigService {

  def loadConfiguration(configFilePath: String): WeatherHasherConfig = {

    val configFile = new File(configFilePath)

    if (!configFile.exists()) {
      throw new ConfigurationException(s"Config file doesn't exists `${configFile.getCanonicalPath}`")
    }
    ConfigSource.file(configFile).loadOrThrow[WeatherHasherConfig]
  }
}
