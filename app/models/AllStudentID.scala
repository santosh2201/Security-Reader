package models

import org.bson.types.ObjectId

import com.mongodb.casbah.Imports._
import com.novus.salat.Context
import com.novus.salat.dao._
import com.novus.salat.dao.SalatDAO

import MongoSetUp._
import models.mongoContext._
import play.api.Play.current

case class StudentIdInfo(
  id: String,
  status: Int
  )

object AllStudentID {

    
   def addStudentIDInfoDataBase( id:String,status:Int) = {
	   val studentIdStatus=StudentIdInfo(id,status);
	   AllStudentIDDAO.insert(studentIdStatus)
  }
   def updateStatus(id:String)={
     val userObject = AllStudentIDDAO.findOne(MongoDBObject("_id" -> id))
     userObject  match {
       case None=>
       case Some(uObject)=>  
         val status=uObject.status
         var newStatus=0
         if(status==0)
           newStatus=1
          AllStudentIDDAO.update(MongoDBObject("_id" -> id), userObject.get.copy(status=newStatus), false, false, new WriteConcern)  
     }
   }
   def getAllStudent():SalatMongoCursor[StudentIdInfo]={
	   		findAll
   }
   def findAll(): SalatMongoCursor[StudentIdInfo] = AllStudentIDDAO.find(MongoDBObject.empty)
}

object AllStudentIDDAO extends SalatDAO[StudentIdInfo, ObjectId](collection = MongoSetUp.mongoDB("student_id_database"))