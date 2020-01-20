jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
	});
 
   
});




function addRowTrue(el){
	var element = '<tr>' + 
			'<p>' +
				'<input type="checkbox" checked="checked" hidden="hidden" value="true" name="tapConnectionFlag" style="width:25px;height:25px; display: block; margin-left: auto; margin-right: auto;"/>' + 
				'<input type="checkbox" checked="checked" hidden="hidden" value="true" name="loginRestrictionFlag" style="width:25px;height:25px; display: block; margin-left: auto; margin-right: auto;"/>' +
			'</p>' +
		'<td style="width:25%"><input type="text" name="name" class="form-control" required="required"/></td>' +
		'<td style="width:30%"><input name="url" type="text" class="form-control" required="required" /></td>' +
		'<td style="width:35%"><input type="text" name="description" class="form-control" required="required"/></td>' +
		'<td>' + 
			'<div class="btn btn-primary" onclick="addRowTrue(this)"><i class="fas fa-plus"></i></div>'+ '  ' + '<div class="btn btn-danger" onclick="removeRow(this)"><i class="fas fa-trash-alt"></i></div>' +
			
		'</td>' +
	'</tr>';
	$(el).closest("tbody").append(element);
}

function addRowFalse(el){
	var element = '<tr>' + 
		'<td>' +
			'<input type="checkbox" onchange="changeRestriction(this)" style="width:25px;height:25px; display: block; margin-left: auto; margin-right: auto;"/>' +
			'<input type="text" hidden="hidden" name="loginRestrictionFlag" value="false"/>' + 
		'</td>' +
		'<td style="width:25%"><input type="text" class="form-control" required="required"/></td>' +
		'<td style="width:30%"><input name="url" type="text" class="form-control" required="required" /></td>' +
		'<td style="width:35%"><input type="text" class="form-control" required="required"/></td>' +
		'<td>' +
			'<div class="btn btn-primary" onclick="addRowTrue(this)"><i class="fas fa-plus"></div>' +
			'<div class="btn btn-danger" onclick="removeRow(this)"><i class="fas fa-trash-alt"></i></div>' +
		'</td>' +
		'</tr>';
		$(el).closest("tbody").append(element);
}

function removeRow(el){
	
	var element = el.parentNode.parentNode;
	element.parentNode.removeChild(element);
	
}

function changeRestriction(el){
	
	var checkValue = $(el).closest("td").find("input[name=loginRestrictionFlag]");
	
	console.log(checkValue);
	
	if(el.checked == true){
		checkValue.val(true);
		console.log(checkValue.value);
	}else{
		checkValue.val(false);
		console.log(checkValue.value);
	}
	
	
	
}

