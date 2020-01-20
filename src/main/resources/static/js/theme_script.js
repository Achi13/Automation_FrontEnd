

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



$(document).ready(function(){
    $('[rel="tooltip"]').tooltip({
    	placement:'top'
    });
});

window.setTimeout(function() {
	
	$(".alert").fadeTo(400,0).slideUp(400,
			function(){
		$(this).remove();
	});
}, 1000);
		
