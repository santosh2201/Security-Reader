package controllers

import models.AllStudentID._
import models.UserModel._
import models.StudentData._
import play.api._
import play.api.mvc._
object AdminController extends Controller {
  
  def index = Action {implicit request =>
    val masterAdminPassword = Play.current.configuration.getString("adminPassword").get
    addMasterAdmin(masterAdminPassword)
    addModerator(masterAdminPassword)
    addUser(masterAdminPassword)
    
     val user =session.get("user")
     user match{
       case None=>Ok(views.html.admin("none","none"))
       case Some(user)=>
           if(user=="admin")
        	   Ok(views.html.admin("exist","admin"))
           else
             Ok(views.html.admin("exist",user))
         
     }
     
  }
  def calInfo(id:String) = Action {
    
    Ok(getStudentInOutList(id))
  //  Ok(views.html.demo("welcome to iit ropar security information"))
  }
   def studStat = Action {
	   val studStat=getAllStudent
	   val totalStud=getAllStudent.size
	   var in=0;
	   for(i<- 0 to getAllStudent.size-1)
	   { 
	     val stud=studStat.next
	     if(stud.status==0)
	       in=in+1
	   } 
	  // println(in)
	   val out = totalStud-in
	   Ok(views.html.studStat(List(totalStud,in,out)))
  }
  
  def verifyUser(user:String,password:String) = Action {
    val result=verifyUserDB(user, password)
   
    result match {
      case None =>Ok("no_user")
      case Some(user_type)=>if(user_type=="admin")
    	  				Ok("sucess").withSession( "user" -> "admin")
    	  				else if(user_type=="moderator")
    	  				  Ok("sucess").withSession( "user" -> "moderator")
    	  				else if(user_type=="student")  
    	  				  Ok("sucess").withSession( "user" -> user)
    	  				else  
    	  				Ok("no_pass")  
    }
    
  //  Ok(views.html.demo("welcome to iit ropar security information"))
  }
    def signOut = Action {implicit request =>
      Ok("sucess").withNewSession
  }
  
  
}