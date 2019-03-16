package com.maxr

import akka.actor.{Actor, ActorLogging}

case object GetStats
case class  StatsResult(stats:Map[String,Int])

class StatsActor extends Actor with ActorLogging {
  private def wordsToMapCount(line: String): Map[String, Int] = {
    line
      .split("\\W+")
      .foldLeft(Map.empty[String, Int])((map: Map[String, Int], next: String) => map + (next -> (map.getOrElse(next, 0) + 1)))
  }

  var statsMap: Map[String, Int] = Map.empty.withDefaultValue(0)

  override def receive: Receive = {
    case Message(line: String) =>

      log.info(s"New message: $line")

      val currentMap: Map[String, Int] = wordsToMapCount(line)

      statsMap ++= currentMap.map {
        case (k, v) => k -> (v + statsMap(k))
      }

    case GetStats =>
      sender ! StatsResult(statsMap)
  }
}
