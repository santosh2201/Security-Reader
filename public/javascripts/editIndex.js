var i=0;
function f(student){
	
		var id="";
	if(student=="null"){
      		id=$('#year').val()+$('#branch').val()+$('#roll').val();
      		location.reload();
      	}
	else{
		 id=student;
		}
		setCookie("username",id,365);
		
	
	
}
function cacheAjaxCall(){
	
	var id=getCookie("username");
	if (id!=null && id!="")
	{
		var year = id.substring(0,4);
		var branch = id.substring(4,6);
		var roll = id.substring(6,10);
		$('#year').val(year);
		$('#branch').val(branch);
		$('#roll').val(roll);
		ajaxCallForStudentData(id);
	}
	
	
}
function ajaxCallForStudentData(id){
		
		ajaxGenerateFeedCall = {
		success : function(data) {
			//document.getElementById("main_div").innerHTML = datcacheAjaxCall()a;
            var obj = eval ("(" + data + ")");
             listparser(obj)
			var events=new Array(); 
			var i=0;var dayChanged=0;
			var j=0;var k=0;
			var title="";
			for(i=0;i<obj.time_in_out.length;i++)
			{
				for(k=0;k<2;k++)
				{
					var msec;
					if(k==0)
					{
						msec =obj.time_in_out[i].intime;
						title=title+"   intime :"+getDayTime(msec)+",\n";
					}	
					else
					{
						msec=obj.time_in_out[i].outtime;
						title=title+"\n outtime :"+getDayTime(msec);
					}	
					//alert(getDayTime(msec))
					var day=getDay(msec);
					//alert(day)	
					if(day!=0)
					{
						if(day!=dayChanged)
						{
							var date=parser(msec);
							events[j]={"date":date,"title":title};
							j++;
							title="";
						}
						
						
					}
					dayChanged=day;
				}
			}
			datamanager(events);
			//parser(obj.time_in_out[0].outtime);

		}
	}
	jsRoutes.controllers.AdminController.calInfo(id).ajax(ajaxGenerateFeedCall);
	


}

function getDay(time){
if(time!="none"){
	var date = new Date(parseInt(time));
	var day = date.getDate();
		return day;
	}
else return 0;	
	
}
function getDayTime(time){
if(time!="none"){
	var date = new Date(parseInt(time));
	var hours = date.getHours();
	var minutes = date.getMinutes()
		return hours+":"+minutes;
	}
else return "NA";	
	
}




function parser(time){
	if(time!="none"){
		var date = new Date(parseInt(time));
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var hours = date.getHours();
		var minutes = date.getMinutes();
		var seconds = date.getSeconds();
		var milliseconds = date.getMilliseconds();
		if(day<10){
			var day="0"+day;
			}
		if(month<10){
			var month ="0"+month;
		}	
		var obj = month+"/"+day+"/"+year;
     	return obj;

	}
}



function datamanager(data){

			
          var evnts = function(){
              return {
                      "event":data
                         
                      }
          };

			$( "#current" ).on( "click", function() {
			
			});
			$( "#current" ).trigger( "click" );

         $('#calendar').Calendar({ 'events': evnts, 'weekStart': 1 })
         .on('changeDay', function(event){ //alert(event.day.valueOf() +'-'+ event.month.valueOf() +'-'+ event.year.valueOf() );
          })
         .on('onEvent', function(event){ //alert(event.day.valueOf() +'-'+ event.month.valueOf() +'-'+ event.year.valueOf() ); 
         })
         .on('onNext', function(event){// alert("Next"); 
         })
         .on('onPrev', function(event){ //alert("Prev");
          })
          .on('onCurrent', function(event){ //alert("Current");
          });
          i++;
}




function listparser(obj){

	var stri="";
	var node = document.getElementById("list");
        
	for(var l=obj.time_in_out.length-1;l>=0;l--){
		if(obj.time_in_out[l].intime!="none"){
				var m="In:";
				stri = stri+"    "+li(m,obj.time_in_out[l].intime)+"<br>";
		}else{
			stri = stri+"In:None<br>"
		}
		if(obj.time_in_out[l].outtime!="none"){
				var m="Out:";
				stri = stri+"      "+li(m,obj.time_in_out[l].outtime)+"<br><hr>";
				//alert(li(obj.time_in_out[l].outtime));
		}else{
			stri = stri+"Out:None<br><hr>"
		}
	}
	node.innerHTML = "<p>" + stri + "</p>";
}

function li(m,x){

	var date = new Date(parseInt(x));
	//alert(date.toString("MM/dd/yyyy hh:mm:ss"));
return m+date.toString("MM/dd/yyyy hh:mm:ss");
}


function getInOutStat(){
	ajaxGenerateFeedCall = {
		success : function(data) {
			document.getElementById("stat").innerHTML = data;
			//alert(data);
		}
	}
	jsRoutes.controllers.AdminController.studStat().ajax(ajaxGenerateFeedCall);
}
function signin(){
	var user =document.getElementById("inemail").value;
	var password =document.getElementById("password").value;
	document.getElementById("status").innerHTML="loggin in please wait ..." ;
	ajaxGenerateFeedCall = {
		success : function(data) {
			//document.getElementById("stat").innerHTML = data;
			//alert(data);
			if(data=="sucess"){
				$("#signinModal").modal('hide');
				location.reload();
			}
			else if(data == "no_pass"){
				document.getElementById("status").innerHTML="wrong password" ;
			}
			else{
					document.getElementById("status").innerHTML="user does not exist"; 
			}
		}
	}
	jsRoutes.controllers.AdminController.verifyUser(user,password).ajax(ajaxGenerateFeedCall);
}
function signout(){
		ajaxGenerateFeedCall = {
		success : function(data) {
			location.reload();			
			
		}
	}
	
	
	
	jsRoutes.controllers.AdminController.signOut().ajax(ajaxGenerateFeedCall);
}





function getCookie(c_name)
{
		var c_value = document.cookie;
		var c_start = c_value.indexOf(" " + c_name + "=");
		if (c_start == -1)
		{
		c_start = c_value.indexOf(c_name + "=");
		}
		if (c_start == -1)
		{
		c_value = null;
		}
		else
		{
		c_start = c_value.indexOf("=", c_start) + 1;
		var c_end = c_value.indexOf(";", c_start);
		if (c_end == -1)
		{
		c_end = c_value.length;
		}
		c_value = unescape(c_value.substring(c_start,c_end));
		}
		return c_value;
}

function setCookie(c_name,value,exdays)
{
	var exdate=new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
	document.cookie=c_name + "=" + c_value;
}

function checkCookie()
{
	var username=getCookie("username");
	if (username!=null && username!="")
	  			return 1;
	else 
	 		return 0;
}


