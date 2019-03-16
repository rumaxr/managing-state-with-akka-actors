package com.maxr

import akka.actor.{ActorSystem, Props}

import scala.concurrent.ExecutionContextExecutor

object StatsApp extends App {
  implicit val system: ActorSystem = ActorSystem("StatsApp")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val statsActor = system.actorOf(Props[StatsActor])
  val generatorActor = system.actorOf(GeneratorActor.props(statsActor))

  generatorActor ! StartProducer
}
