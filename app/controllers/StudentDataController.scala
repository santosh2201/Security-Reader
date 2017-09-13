package controllers

import play.api._
import play.api.mvc._
import models.StudentData._
import models.StudentInOutTime
import models.StudentInfo
import play.api.libs.json.Json
import models.AllStudentID._
object StudentDataController extends Controller {

  def index = Action {
    Ok(views.html.index("welcome to iit ropar security information"))
  }
  def storeData = Action {
    implicit request =>
      val postData = request.body.asFormUrlEncoded
      val content = postData.get.get("content").get(0)
      val time = postData.get.get("time").get(0)
      val status = postData.get.get("status")

      checkIfStudentExists(content) match {
        case true =>
          updateStatus(content)
          updateStudentInfo(content, time)
          
          Ok(getStudentInOutList(content))
        case false =>
          if (status == None) {
	            val jsonObject = Json.toJson(Map("content" -> content, "time" -> time, "status" -> "none"))
	            Ok(jsonObject.toString)
          } else {
        	  	var studentInOut=StudentInOutTime("","");
        	  	if((Integer.parseInt(status.get(0)))==0)	
	             studentInOut = StudentInOutTime(time, "none");
        	  	else
        	  	 studentInOut = StudentInOutTime("none",time); 
	            
        	  	val studentInfo = StudentInfo(content, Integer.parseInt(status.get(0)), List(studentInOut))
        	  	addStudentIDInfoDataBase(content,  Integer.parseInt(status.get(0)))
        	  	addStudentInfoDataBase(studentInfo)

	          
	            Ok(getStudentInOutList(content))
          }
      }
   
  }

}