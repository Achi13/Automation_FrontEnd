jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
	});
 
   
});




$(document).on("change", "#ppkFile", function() {
  var file_data = $("#ppkFile").prop("files")[0]; // Getting the properties of file from file field
  var form_data = new FormData(); // Creating object of FormData class
  form_data.append("ppk", file_data) // Appending parameter named file with properties of file_field to form_data
  $.ajax({
    url: "http://moadonea220rywm:2001/clientloginaccount/uploadppk", // Upload Script
    dataType: 'json',
    cache: false,
    contentType: false,
    processData: false,
    data: form_data, // Setting the data attribute of ajax with file_data
    type: 'post',
    success: function(data) {
      if(data.Error != null){
    	  $("#ppkFile").val(null);
    	  alert(data.Error);
      }else if(data.Info != null){
    	  alert(data.Info);
      }
    }
  });
});

function uploadShFile(el){
	var file_data = $(el).prop("files")[0]; // Getting the properties of file from file field
	var form_data = new FormData(); // Creating object of FormData class
	form_data.append("shFile", file_data) // Appending parameter named file with properties of file_field to form_data
	$.ajax({
	    url: "http://moadonea220rywm:2001/script/uploadshfile", // Upload Script
	    dataType: 'json',
	    cache: false,
	    contentType: false,
	    processData: false,
	    data: form_data, // Setting the data attribute of ajax with file_data
	    type: 'post',
	    success: function(data) {
	      if(data.Error != null){
	    	  $(el).val(null);
	    	  alert(data.Error);
	      }else if(data.Info != null){
	    	  alert(data.Info);
	      }
	    }
	});
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


var btn= document.getElementById("enter");
btn.addEventListener("keyup",function(event){
	if(event.keycode === "13"){
		event.preventDefault();
		document.getElementById("enter").click();
	}
});