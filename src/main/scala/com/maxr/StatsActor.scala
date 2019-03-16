package com.maxr

import akka.actor.{Actor, ActorLogging, Props}

case object GetStats
case class  StatsResult(stats:Map[String,Int])

object StatsActor {
  case class StatsMaps(wordCountMap: Map[String, Int] = Map.empty.withDefaultValue(0))

  def props(): Props = Props[StatsActor]

  def wordsToMapCount(line: String): Map[String, Int] = {
    line
      .split("\\W+")
      .foldLeft(Map.empty[String, Int])((map: Map[String, Int], next: String) => map + (next -> (map.getOrElse(next, 0) + 1)))
  }
}

class StatsActor extends Actor with ActorLogging {

  import StatsActor._

  override def receive: Receive = {
    updated(StatsMaps())
  }

  private def updated(statsMap: StatsMaps): Receive = {
    case Message(line: String) =>

      log.info(s"New message: $line")

      val currentMap: Map[String, Int] = wordsToMapCount(line)
      val wordCountMap: Map[String, Int] = statsMap.wordCountMap ++ currentMap.map {
        case (k, v) => k -> (v + statsMap.wordCountMap(k))
      }
      context.become(updated(statsMap.copy(wordCountMap = wordCountMap)))

    case GetStats =>
      sender ! StatsResult(statsMap.wordCountMap)

    case _ => throw new IllegalStateException("Unknown event was sent to StatsActor")

  }
}
