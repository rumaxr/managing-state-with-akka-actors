package com.maxr

import scala.util.Random

object DataSource {

  val random: Random = Random

  val lineList: List[String] = List(
    "the cow jumped over the moon",
    "an apple a day keeps the doctor away",
    "four score and seven years ago",
    "snow white and the seven dwarfs",
    "i am at two with nature",
  )

  def getMessage: String = {
    lineList(random.nextInt(lineList.length))
  }

}
