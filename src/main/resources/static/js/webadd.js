jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
	});
 
   
});





//Scroll btn

function addRow(el){
	var element = '<tr>' + 
		'<td><input name="username" required="required" type="text" class="form-control"/></td>' +
		'<td><input name="password" required="required" type="password" class="form-control"/></td>' + 
		'<td>' +
			'<div class="btn btn-primary" onclick="addRow(this)"><i class="fas fa-plus"></i> </div>' +
			'<div class="btn btn-danger" onclick="removeRow(this)"><i class="fas fa-trash-alt"></i> </div>' +
		'</td>' +
	'</tr>';
	$(el).closest("tbody").append(element);
	var count = $(el).closest("div.row").find("input[name='counter']").val();
	$(el).closest("div.row").find("input[name='counter']").val(parseInt(count) + 1);
}

function removeRow(el){
	
	var count = $(el).closest("div.row").find("input[name='counter']").val();
	$(el).closest("div.row").find("input[name='counter']").val(parseInt(count) - 1);
	var element = el.parentNode.parentNode;
	element.parentNode.removeChild(element);
	
}


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
