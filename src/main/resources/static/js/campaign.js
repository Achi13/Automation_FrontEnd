
function checkCampaign(isSuccess, testcaseList){
	
	var testcaseList
	
	if(!isSuccess){
		return confirm("The templates used for testcase " + testcaseList + " has been modified. Do you still want to proceed?");
		
	}
	
}


$(document).ready(function(){
    $('rel="tooltip"]').tooltip({
    
    });
});


//Data Table
$(document).ready(function() {
    $('#datatable').dataTable({
    	
    	//removing of sort btn
    
    	"ordering":false
    	
    });    
});

jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
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

window.setTimeout(function() {
	
	$(".alert").fadeTo(400,0).slideUp(400,
			function(){
		$(this).remove();
	});
}, 2000);

function checkValue(el){
	console.log(el.value);
	
	var json = {"campaignId":$(el).prev()[0].value.toString(),
			"executionSchedule":el.value.toString()};
	
	$.ajax({
	    url: "http://moadonea220rywm:2001/campaign/addexecutionschedule", // Upload Script
	    dataType: 'json',
	    contentType: "application/json; charset=utf-8",
	    data: JSON.stringify(json), // Setting the data attribute of ajax with file_data
	    type: 'POST',
	    success: function(data) {
	    }
	});
	
}

var btn= document.getElementById("enter");
btn.addEventListener("keyup",function(event){
	if(event.keycode === "13"){
		event.preventDefault();
		document.getElementById("enter").click();
	}
});