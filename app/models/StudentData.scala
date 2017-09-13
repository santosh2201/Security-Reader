package models

import com.novus.salat.dao._
import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._
import com.novus.salat.{ TypeHintFrequency, StringTypeHintStrategy, Context }
import play.api.Play
import play.api.Play.current
import com.novus.salat.dao.SalatDAO
import org.bson.types.ObjectId
import models.mongoContext._
import MongoSetUp._

case class StudentInfo(
  id: String,
  status: Int,
  time_in_out: List[StudentInOutTime]
  )
case class StudentInOutTime(
  intime: String,
  outtime: String)

object StudentData {

  def addStudentInfoDataBase(busStopData: StudentInfo) = {
    StudentDataDAO.insert(busStopData)
  }

  def checkIfStudentExists(id: String): Boolean = {
    try {
      val userObject = StudentDataDAO.findOne(MongoDBObject("_id" -> id))
      userObject match {
        case None => false
        case _ => true
      }
    } catch {
      case t: Throwable =>
        false
    }

  }
  def  getStudentInOutList(id:String):String={
	  		val userObject = StudentDataDAO.findOne(MongoDBObject("_id" -> id))
	  		val dbObj=StudentDataDAO.toDBObject(userObject.get);
	  		dbObj.toString()
  }
  def updateStudentInfo(id:String,time:String)={
    val userObject = StudentDataDAO.findOne(MongoDBObject("_id" -> id))
    userObject match{
      case None=>
      case Some(student)=>
         student.status match{
           //student inside campus getting out
           case 0=>
             		val lastIndex=userObject.get.time_in_out.size -1 
             		val lastListObj=userObject.get.time_in_out(lastIndex)
             		val list =userObject.get.time_in_out.filter(x=>x != lastListObj)
             		val newObj=StudentInOutTime(lastListObj.intime,time)
             		val newStudentTimeObj=list:::List(newObj)
           StudentDataDAO.update(MongoDBObject("_id" -> id), userObject.get.copy(status=1,time_in_out=newStudentTimeObj), false, false, new WriteConcern)
            //student is out comming in  			
           case 1=> 
              val list =userObject.get.time_in_out	
              val newObj=StudentInOutTime(time,"none")
              val newStudentTimeObj=list:::List(newObj)
              StudentDataDAO.update(MongoDBObject("_id" -> id), userObject.get.copy(status=0,time_in_out=newStudentTimeObj), false, false, new WriteConcern)
          
         } 
    }
  }

}

object StudentDataDAO extends SalatDAO[StudentInfo, ObjectId](collection = MongoSetUp.mongoDB("student_database"))