package com.maxr

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

case object StartProducer
case class Message(line:String)

object GeneratorActor {
  def props(statsActor: ActorRef): Props = Props(new GeneratorActor(statsActor))
}

class GeneratorActor(statsActor: ActorRef) extends Actor with ActorLogging {

  import DataSource._

  case object Tick

  override def receive: Receive = {
    case StartProducer =>
      log.info("Producer started")
      context.system.scheduler.schedule(
        initialDelay = 0 milliseconds,
        interval = 750 milliseconds,
        self,
        Tick
      )

      context.system.scheduler.schedule(
        initialDelay = 0 milliseconds,
        interval = 5 seconds,
        statsActor,
        GetStats
      )

    case Tick =>
      statsActor ! Message(getMessage)

    case StatsResult(stats) =>
      log.info(stats.toString)
  }

}
