package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
   def javascriptRoutes = Action { implicit request =>
    
    Ok(
        Routes.javascriptRouter("jsRoutes")(
        routes.javascript.AdminController.calInfo,
        routes.javascript.AdminController.studStat,
        routes.javascript.AdminController.verifyUser,
         routes.javascript.AdminController.signOut
      )).as("text/javascript")
  }
  
  def index = Action {implicit request =>
     val user =session.get("user")
     user match{
       case None=>Ok(views.html.index("none"))
       case Some(user)=>
         Ok(views.html.index("exists"))
     }
    
  }
  
  def demo = Action {
    Ok(views.html.demo("welcome to iit ropar security information"))
  }
  
  def signin = Action {
    Ok(views.html.sign.signIn("welcome to iit ropar security information"))
  }
  
  def signup = Action {
    Ok(views.html.sign.signup("welcome to iit ropar security information"))
  }
  
}