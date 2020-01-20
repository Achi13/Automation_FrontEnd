jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
	});
 
   
});


//Data Table
$(document).ready(function() {
    $('#datatable').dataTable({
    	
    	//removing of sort btn
    
    	"ordering":false
    	
    });    
});


window.setTimeout(function() {
	
	$(".alert").fadeTo(400,0).slideUp(400,
			function(){
		$(this).remove();
	});
}, 2000);


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
    $('[rel="tooltip"]').tooltip({
    	placement:'top'
    });
});

function changeInputValueEdit(){
	var serverImport = document.getElementById("serverImportEdit");
	var serverImportVal = document.getElementById("serverImportValEdit");
	if(serverImport.checked == true){
		serverImportVal.value = true;
	}else{
		serverImportVal.value = false;
	}
	console.log(serverImportVal.value + ":" + serverImport.checked)
}
function changeSeverityEdit(){
	let ignoreSeverity = document.getElementById("ignoreSeverityEdit");
	let ignoreSeverityVal = document.getElementById("ignoreSeverityValEdit");
	
	if(ignoreSeverity.checked == true){
		ignoreSeverityVal.value = true;
		console.log(ignoreSeverityVal.value);
	}else{
		ignoreSeverityVal.value = false;
		console.log(ignoreSeverityVal.value);
	}
}

function changeInputValue(){
	var serverImport = document.getElementById("serverImport");
	var serverImportVal = document.getElementById("serverImportVal");
	if(serverImport.checked == true){
		serverImportVal.value = true;
	}else{
		serverImportVal.value = false;
	}
	console.log(serverImportVal.value + ":" + serverImport.checked)
}
function changeSeverity(){
	let ignoreSeverity = document.getElementById("ignoreSeverity");
	let ignoreSeverityVal = document.getElementById("ignoreSeverityVal");
	
	if(ignoreSeverity.checked == true){
		ignoreSeverityVal.value = true;
		console.log(ignoreSeverityVal.value);
	}else{
		ignoreSeverityVal.value = false;
		console.log(ignoreSeverityVal.value);
	}
}
