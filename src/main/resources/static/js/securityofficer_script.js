jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
	});
 
   
});




window.setTimeout(function() {
	
	$(".alert").fadeTo(400,0).slideUp(400,
			function(){
		$(this).remove();
	});
}, 2000);



//Data Table
$(document).ready(function() {
    $('#datatable').dataTable({
    	
    	//removing of sort btn
    	"ordering":false
    	
    });    
});

$(document).ready(function(){

	$(function() {
	    $('.select-name').selectize({plugins: ['remove_button']
	});

  $('#contacts').submit(function(e){
    e.preventDefault();

    alert('You have chosen the following users: '+ JSON.stringify($('#select-name').val()));
  });
});
});



document.getElementById("select-name").onchange = function(){

console.log(this.value);

}



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




