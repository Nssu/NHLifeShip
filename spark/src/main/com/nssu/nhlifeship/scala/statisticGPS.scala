package com.nssu.nhlifeship.scala
import org.apache.spark.{SparkConf, SparkContext}
import com.nssu.nhlifeship.scala.Constants._
/**
  * @project NHLifeShip
  * @author wshid
  * @date 2018. 5. 31. PM 2:36
  *
  */

object statisticGPS {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName(SPARK_APPNAME).setMaster(SPARK_MASTER)
    val sc = new SparkContext(conf)

    val rdd = sc.textFile(FILE_SUBWAY)

    val lineCount = rdd.count()

    val splitedRDD = rdd.map(line => line.split(","))

    val keyValueRDD = splitedRDD.map(line => {
      val key : String = line(0)
      val values = (line(4).trim.toInt, line(5).trim.toInt)
      (key, values)

    })

    val reducedRDD = keyValueRDD.reduceByKey((a,b) => (a._1 + b._1, a._2 + b._2))

    for( i <- reducedRDD.collect()){
      println(i._1, i._2)
    }

    reduced


    // 각 역사별 이용자 수 합계를 구한다.


    //subway_name

    //println(lineCount)

    sc.stop()
  }
}