$(".dropdown-toggle").on("mouseenter", function () {
    // make sure it is not shown:
    if (!$(this).parent().hasClass("show")) {
        $(this).click();
    }
});

window.setTimeout(function() {
	
	$(".alert").fadeTo(400,0).slideUp(400,
			function(){
		$(this).remove();
	});
}, 2000);

jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
	});
 
   
});
