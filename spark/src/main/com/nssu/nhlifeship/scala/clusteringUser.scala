package com.nssu.nhlifeship.scala

import breeze.linalg.DenseVector
import breeze.numerics.pow
import com.nssu.nhlifeship.scala.Constants._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.recommendation.{ALS, Rating}

/**
  * @project NHLifeShip
  * @author wshid
  * @date 2018. 6. 2. PM 1:17
  */
object clusteringUser {

  def main(args : Array[String]) : Unit = {
    val conf = new SparkConf().setAppName(SPARK_APPNAME).setMaster(SPARK_MASTER)
    val sc = new SparkContext(conf)

    val rawData = sc.textFile(FILE_USERSTATUS)
    val rdd = rawData.map(line => line.split("\t"))
    val rawDataUser = sc.textFile(FILE_USER_INFO)

    val brandMap = rdd.map(line => line(3)).distinct().zipWithIndex() // 브랜드 인덱스 번호
    val locationMap = rdd.map(line => line(5)).distinct().zipWithIndex() // 지역별 인덱스 번호

    println(rawDataUser.count())
    val userMap = rawDataUser.map(line => line.split(DELIMITER_TAB)).map(x => {

      var value : Int = 0
      if(x(4).toInt != 0){
        value = x(4).toInt
      }else{
        value = x(6).toInt
      }
      x(0) -> value

    }).collect().toMap

    //userMap.take(100).foreach(println)




    val lineMap = rdd.map(line => {

      val label = line(0)
      val linevector = Vectors.dense(Array(line(1).toDouble, line(2).toDouble,
        line(4).toDouble, line(5).toDouble,
        line(7).toDouble, line(8).toDouble, line(9).toDouble))

      (userMap(label), linevector)

    })


    val valuesVector = lineMap.values.cache()


    lineMap.take(30).foreach(println)


    val numClusters  = 10
    val numIterations = 30
    val numRuns = 3

    val model = KMeans.train(valuesVector,numClusters,numIterations, numRuns)
    //brandMap.take(3).foreach(println)
    //locationMap.collect().foreach(println)



    val predictions = model.predict(valuesVector)
    println(predictions.take(100).mkString(","))

    println("END POINT")


    val resultAssign = lineMap.map(line => {
      val pred = model.predict(line._2)
      val clusterCentre = model.clusterCenters(pred)
      val dist = computeDistance(DenseVector(clusterCentre.toArray), DenseVector(line._2.toArray))

      (line._1, pred, dist)
    })

    resultAssign.take(10).foreach(println)


    val clusterAssignments = resultAssign.groupBy{case(label, pred, dist) => pred}.collectAsMap()




    //val menuMap = rdd.map




  }

  def computeDistance(v1 : DenseVector[Double], v2: DenseVector[Double]) : Double = pow(v1-v2, 2).sum

}
