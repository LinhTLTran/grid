import scala.collection.JavaConverters._
import com.typesafe.config.ConfigValue

import play.api.{Logger, Application, GlobalSettings}
import play.api.mvc.WithFilters
import play.api.libs.concurrent.Akka

import controllers.Application
import lib.{Config, ForceHTTPSFilter}

object Global extends WithFilters(ForceHTTPSFilter) with GlobalSettings {

  override def beforeStart(app: Application) {

    val allAppConfig: Seq[(String, ConfigValue)] =
      Config.appConfig.underlying.entrySet.asScala.toSeq.map(entry => (entry.getKey, entry.getValue))

    Logger.info("Play app config: \n" + allAppConfig.mkString("\n"))
  }

  override def onStart(app: Application) {
    Application.keyStore.scheduleUpdates(Akka.system(app).scheduler)
  }

}