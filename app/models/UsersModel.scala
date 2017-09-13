package models

import org.bson.types.ObjectId
import com.novus.salat.dao.SalatDAO
import models.mongoContext.context
import com.mongodb.casbah.commons.MongoDBObject

case class User(
  id: String,
  password: String,
  role:Int,
  status:Int
  )
  

object UserModel {
	 val ROLE_ADMIN=0
	 val ROLE_MODERATOR=1
	 val ROLE_STUDENT=2
	 val STATUS_UNCONFERMED=0
	 val STATUS_CONFERMED=1 
	 def addMasterAdmin(password:String) = {
	   val adminInfo=User("admin",password,ROLE_ADMIN,STATUS_CONFERMED);
	   UserModelDAO.insert(adminInfo)
	 }
	  def addModerator(password:String) = {
	   val adminInfo=User("moderator",password,ROLE_MODERATOR,STATUS_UNCONFERMED);
	   UserModelDAO.insert(adminInfo)
	 }
    
    def addUser(password:String) = {
	   val adminInfo=User("2010CS1023",password,ROLE_STUDENT,STATUS_UNCONFERMED);
	   UserModelDAO.insert(adminInfo)
    }
   
   def verifyUserDB(user:String, pass:String):Option[String]={
      val userObject = UserModelDAO.findOne(MongoDBObject("_id" -> user))
      userObject match {
        case None => None
        case Some(user)=> if(user.password==pass)
        				{
        					if(user.role==ROLE_ADMIN)
        					Option("admin")
        					else if(user.role==ROLE_MODERATOR)
        					Option("moderator") 
        					else
        					  Option("student") 
        				}					
        					else
        					  Option("false")
        case _=>None					  
      }
   }
   
   
}

object UserModelDAO extends SalatDAO[User, ObjectId](collection = MongoSetUp.mongoDB("user_db" ))