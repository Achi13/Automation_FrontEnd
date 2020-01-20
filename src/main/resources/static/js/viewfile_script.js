

$(document).ready(function(){
	
	$("#myForm").submit(function(){
		var counter = 0;
		console.log("clicked");
		var dateInput = document.querySelectorAll("input[type='datetime-local']");
		var testcase = document.querySelectorAll("input[name='testcase']");
		var jsonData = {};
		
		for(let i=0; i<dateInput.length; i++){
			counter++;
			jsonData[i] = { "testcase" : testcase[i].value, "date" : dateInput[i].value};
		}
		console.log(jsonData);
		$.ajax({
			url: "/updateschedule/" + JSON.stringify(jsonData) + "/" + counter,
			success: function(){
				alert("success");
			}
		});
	});
	
});
function getData(){
	var dateInput = document.querySelectorAll("input[type='datetime-local']").value;
	var testcase = document.querySelectorAll("input[name='testcase']").value;
	var jsonData = {};
	for(let i=0; i<dateInput.length; i++){
		jsonData[testcase[i]] = { "testcase" : testcase[i], "date" : dateInput[i]};
	}
	//AJAX PART
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function(){
		if(this.readyState == 4 && this.status == 200){
			console.log("success");
		}
	};
	xhttp.open("GET","/updateschedule/" + jsonData, true);
	xhttp.send();
	
	
}


//Data Table
$(document).ready(function() {
    $('#datatable').dataTable({
    	
    	//removing of sort btn
    
    	"ordering":false
    	
    });    
});



//Data Table
$(document).ready(function(){
    $('[rel="tooltip"]').tooltip({
    	placement:'top'
    });
    
});


//Scroll btn

$(document).ready(function(){
	$(window).scroll(function () {
			if ($(this).scrollTop() > 50) {
				$('#back-to-top').fadeIn();
			} else {
				$('#back-to-top').fadeOut();
			}
		});
		// scroll body to 0px on click
		$('#back-to-top').click(function () {
			$('body,html').animate({
				scrollTop: 0
			}, 400);
			return false;
		});
});

$(document).ready(function(){
	var date = new Date();
	
	var month;
	var day;
	if(date.getDate() < 10){
		day = "0" + (date.getDate().toString());
	}else{
		day = date.getDate().toString();
	}
	
	
	if((date.getMonth()+1)<10){
		month = "0" + (date.getMonth() + 1).toString();
	}else{
		month = (date.getMonth()+1).toString();
	}
	
	var hours;
	if((date.getHours() + 1) < 10){
		hours = "0" + (date.getHours() + 1).toString();
	}else{
		hours = (date.getHours() + 1).toString();
	}
	
	
	var minute;
	if((date.getMinutes() + 1) < 10){
		minute = "0" + (date.getMinutes() + 1).toString();
	}else{
		minute = (date.getMinutes() + 1).toString();
	}
	
	var seconds;
	if((date.getSeconds() + 1) < 10){
		seconds = "0" + (date.getSeconds() + 1).toString();
	}else{
		seconds = (date.getSeconds() + 1).toString();
	}
	
	
	var current = month + "-" + day + "-" + date.getFullYear().toString(); + "T" + hours + ":" + minute + ":" + seconds;
	
	var dateControl = document.getElementById('samples');
	
	//dateControl.value = date.toJSON().slice(0,16).toString();
	//console.log(dateControl.value);
	
	$("input[type='datetime-local']").val(current);
});


