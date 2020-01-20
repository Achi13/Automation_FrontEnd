jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
	});
 
   
});



var counter=2;
 	


var app = angular.module('mainBody', []);
app.controller('mainController', function($scope, $http){
	
	
	$scope.uploadTemplate = function(){
		
		console.log("Sample");
		
		var fileData = $("#templateFile").prop("files")[0];
		var formData = new FormData();
		formData.append("textFile", fileData);
		console.log(fileData);
		$http({
			method: "POST",
			url: "http://moadonea220rywm:2001/templates/uploadtemplate",
			data: formData,
			headers: {'content-type' : undefined}
		}).then(function(response){
			
			alert("Template Succesfully Uploaded");
			$("#ViewImportModal").modal('hide');
			$scope.templateDataList = response.data;
			console.log($scope.templateDataList.templateDataList);
		});
		
	}
	$scope.changeInput = function(num){
		var action = document.getElementById("natureOfAction"+num);
		var input = document.getElementById("inputOutput"+num);
		
		var date = new Date();
		
		
		var day;
		if(date.getDate() < 10){
			day = "0" + (date.getDate().toString());
		}else{
			day = date.getDate().toString();
		}
		
		var month;
		if((date.getMonth()+1)<10){
			month = "0" + (date.getMonth() + 1).toString();
		}else{
			month = (date.getMonth()+1).toString();
		}
		
		if(action.value == "input" || action.value == "compare" || action.value == "select"){
			input.readOnly = false;
			input.required = true;
			input.type = "text";
			$(input).removeAttr('data-date');
			$(input).removeAttr('data-date-format');
			$(input).val(null);
			
		}else if(action.value == "datepicker"){
			console.log("hello");
			input.readOnly = false;
			input.required = true;
			input.type = "date";
			input.classList.add('datepicker');
			$(input).attr("data-date", " ");
			$(input).attr("data-date-format","MMM DD, YYYY");
			$(input).val(date.getFullYear().toString() + "-" + month + "-" + date.getDate().toString());
			$(input).on("change", function() {
			    this.setAttribute(
			        "data-date",
			        moment(this.value, "YYYY-M-DD")
			        .format( this.getAttribute("data-date-format") )
			    )
			}).trigger("change")
			
		}else{
			input.readOnly = true;
			input.required = false;
			input.type = "text";
			$(input).removeAttr('data-date');
			$(input).removeAttr('data-date-format');
			$(input).val(null);
			
		}
	}
	
	$scope.toggleWebElementName = function(num){
		var showWebElementName = document.getElementById("showWebElementName"+num);
		var webElementName = document.getElementById("webElementName"+num);
		
		if(webElementName.hidden == true){
			webElementName.hidden = false;
		}else{
			
			webElementName.hidden = true;
		}
	}
	
	$scope.changeScreenCapture = function(num){
		var screenCapture = document.getElementById("screenCapture"+num);
		var screenCaptureValue = document.getElementById("screenCaptureValue"+num);
		
		if(screenCapture.checked == true){
			screenCaptureValue.value = "true";
			console.log(screenCaptureValue.value);
		}else{
			screenCaptureValue.value = "false";
			console.log(screenCaptureValue.value);
		}
	}
	
	$scope.changeTriggerEnter = function(num){
		var triggerEnter = document.getElementById("triggerEnter"+num);
		var triggerEnterValue = document.getElementById("triggerEnterValue"+num);
		
		if(triggerEnter.checked == true){
			triggerEnterValue.value = "true";
			console.log(triggerEnterValue.value);
		}else{
			triggerEnterValue.value = "false";
			console.log(triggerEnterValue.value);
		}
	}
	
});

 	
var tbody = $("#displayTable").children('tbody');

function changeInput(num){
	var action = document.getElementById("natureOfAction"+num);
	var input = document.getElementById("inputOutput"+num);
	
	var date = new Date();
	
	var month;
	if(date.getDate() < 10){
		day = "0" + (date.getDate().toString());
	}else{
		day = date.getDate().toString();
	}
	
	var day;
	if((date.getMonth()+1)<10){
		month = "0" + (date.getMonth() + 1).toString();
	}else{
		month = (date.getMonth()+1).toString();
	}
	
	
	if(action.value == "input" || action.value == "compare" || action.value == "select"){
		input.readOnly = false;
		input.required = true;
		input.type = "text";
		$(input).removeAttr('data-date');
		$(input).removeAttr('data-date-format');
		$(input).val(null);
	}else if(action.value == "datepicker"){
		input.readOnly = false;
		input.required = true;
		input.type = "date";
		input.classList.add('datepicker');
		$(input).attr("data-date", " ");
		$(input).attr("data-date-format","MMM DD, YYYY");
		$(input).val(date.getFullYear().toString() + "-" + month + "-" + day);
		$(input).on("change", function() {
		    this.setAttribute(
		        "data-date",
		        moment(this.value, "YYYY-M-DD")
		        .format( this.getAttribute("data-date-format") )
		    )
		}).trigger("change")
	}else{
		input.readOnly = true;
		input.required = false;
		input.type = "text";
		$(input).removeAttr('data-date');
		$(input).removeAttr('data-date-format');
		$(input).val(null);
	}
}

function changeTriggerEnter(num){
	var triggerEnter = document.getElementById("triggerEnter"+num);
	var triggerEnterValue = document.getElementById("triggerEnterValue"+num);
	
	if(triggerEnter.checked == true){
		triggerEnterValue.value = "true";
		console.log(triggerEnterValue.value);
	}else{
		triggerEnterValue.value = "false";
		console.log(triggerEnterValue.value);
	}
}

function changeScreenCapture(num){
	var screenCapture = document.getElementById("screenCapture"+num);
	var screenCaptureValue = document.getElementById("screenCaptureValue"+num);
	
	if(screenCapture.checked == true){
		screenCaptureValue.value = "true";
		console.log(screenCaptureValue.value);
	}else{
		screenCaptureValue.value = "false";
		console.log(screenCaptureValue.value);
	}
}

function removeRow(el){
	
	var element = el.parentNode.parentNode;
	element.parentNode.removeChild(element);
	
}


function addRow(el){
	
	
	
	var element = '<tr id="row' + counter + '">' + 
		'<td><input required="required" type="text" name="label" class="form-control"/></td>' +
		'<td>' + 
			'<select name="webElementNature" class="custom-select">' + 
				'<option value="name" selected="selected">name</option>' +
				'<option value="class">class</option>' +
				'<option value="id">id</option>' + 
				'<option value="xpath">xpath</option>' +
			'</select>' +
		'</td>' +
		'<td>' +
			'<select name="natureOfAction" class="custom-select" id="natureOfAction' + counter +'" onchange="changeInput(' + counter +')">' +
				'<option value="click">click</option>' +
				'<option value="input" selected="selected">input</option>' +
				'<option value="hover">hover</option>' +
				'<option value="compare">compare</option>' +
				'<option value="select">select</option>' +
				'<option value="datepicker">Date picker</option>' +
			'</select>' +
		'</td>' +
		'<td>' +
			'<input type="checkbox" value="false" name="triggerEnter" id="triggerEnter' + counter + '" onchange="changeTriggerEnter(' + counter + ')"style="width:25px;height:25px;"/>' +
			'<input type="text" name="triggerEnterValue" hidden="hidden" id="triggerEnterValue' + counter +'" value="false"/>' +
		'</td>' +
		'<td>' +
			'<input type="checkbox" value="false" name="screenCapture" id="screenCapture' + counter + '" onchange="changeScreenCapture(' + counter + ')"style="width:25px;height:25px;"/>' +
			'<input type="text" name="screenCaptureValue" hidden="hidden" id="screenCaptureValue' + counter +'" value="false"/>' +
		'</td>' +
		'<td>' +
			'<input required="required" type="text" name="webElementName" class="form-control" id="webElementName' + counter + '"/>' +
			'<div class="switchToggle1" align="center"><input type="checkbox" checked="checked" required="required" onchange="toggleWebElementName(' + counter + ')" id="showWebElementName' + counter + '"/><label for ="showWebElementName' + counter + '"></label></div>' +
		'</td>' + 
		'<td>' + 
			'<div onclick="addRow(this)" class="addButton btn btn-primary" "><i class="fas fa-plus"></i></div>' +
			'<div onclick="removeRow(this)" class="removeButton btn btn-danger" "><i class="fas fa-trash-alt"></i></div>' +
		'</td>'
	'</tr>';
	$(element).insertAfter(el.closest("tr"));
}


$(document).ready(function(){
	
	
	$("#row1").css("visibility","collapse");
	$("#row2").css("visibility","collapse");
	$("#row3").css("visibility","collapse");
	
	$("input[name='inputOutput']").attr("readonly", true);
	$("tr").each(function (index){
		index++;
		if($("#natureOfAction"+(index)).val() == "input" || $("#natureOfAction"+(index)).val() == "compare"){
			$("#inputOutput"+index).attr("readonly", false);
			$("#inputOutput"+index).attr("required", true);
		}
	});
	/*
	$(".addButton").click(function(){
		var element = '<tr id="row' + counter + '">' + 
			'<td><input type="text" name="notes" class="form-control"/></td>' +
			'<td>' + 
				'<select name="webElementNature" class="custom-select">' + 
					'<option value="name" selected="selected">name</option>' +
					'<option value="class">class</option>' +
					'<option value="id">id</option>' + 
					'<option value="xpath">xpath</option>' +
				'</select>' +
			'</td>' +
			'<td>' +
				'<select name="natureOfAction" class="custom-select" id="natureOfAction' + counter +'" onchange="changeInput(' + counter +')">' +
					'<option value="click">click</option>' +
					'<option value="input" selected="selected">input</option>' +
					'<option value="hover">hover</option>' +
					'<option value="compare">compare</option>' +
					'<option value="select">select</option>' +
					'<option value="datepicker">Date picker</option>' +
				'</select>' +
			'</td>' +
			'<td>' +
				'<input type="checkbox" value="false" name="triggerEnter" id="triggerEnter' + counter + '" onchange="changeTriggerEnter(' + counter + ')"style="width:25px;height:25px;"/>' +
				'<input type="text" name="triggerEnterValue" hidden="hidden" id="triggerEnterValue' + counter +'" value="false"/>' +
			'</td>' +
			'<td>' +
				'<input type="checkbox" value="false" name="screenCapture" id="screenCapture' + counter + '" onchange="changeScreenCapture(' + counter + ')"style="width:25px;height:25px;"/>' +
				'<input type="text" name="screenCaptureValue" hidden="hidden" id="screenCaptureValue' + counter +'" value="false"/>' +
			'</td>' +
			'<td><input type="text" name="inputOutput" class="form-control" id="inputOutput' + counter +'"/></td>' +
			'<td>' +
				'<input type="text" name="webElementName" class="form-control" id="webElementName' + counter + '" hidden="hidden"/>' +
				'<div class="switchToggle1"><input type="checkbox" onchange="toggleWebElementName(' + counter + ')" id="showWebElementName' + counter + '"/><label for ="showWebElementName' + counter + '"></label></div>' +
			'</td>' + 
			'<td style= "vertical-align: middle; padding-left:20px;"><div class="btn-primary" onclick="addRow()" style ="width: 30px; height:25px;"><i class="fas fa-plus"></i></p></div>' + 
			
			'<td style= "vertical-align: middle; padding-left:28px;"><div onclick="removeRow(this)" class="btn-danger removeButton" style ="width: 30px; height:25px;"><i class="fas fa-trash-alt"></i></div></td>' + 
		'</tr>';
		$("#tbodyMain").append(element);
		counter++;
	});*/
	
	$(".removeButton").click(function(){
		$(this).closest("tr").remove();
		counter--;
		console.log(counter);
	});
	
	$("#infoText").delay(5000).fadeOut();
	
	$("#orderType").change(function(){
		if($(this).val() == "bond"){
			$("#natureType").empty();
			$("#natureType").append($('<option value="buy">Buy</option>'));
			$("#natureType").append($('<option value="sell">Sell</option>'));
		}else if($(this).val() == "cash"){
			$("#natureType").empty();
			$("#natureType").append($('<option value="injection">Injection</option>'));
			$("#natureType").append($('<option value="withdrawal">Withdrawal</option>'));
		}else if($(this).val() == "equities"){
			$("#natureType").empty();
			$("#natureType").append($('<option value="buy">Buy</option>'));
			$("#natureType").append($('<option value="sell">Sell</option>'));
		}else if($(this).val() == "funds"){
			$("#natureType").empty();
			$("#natureType").append($('<option value="buy">Buy</option>'));
			$("#natureType").append($('<option value="sell">Sell</option>'));
		}else if($(this).val() == "cashwithdrawal"){
			$("#natureType").empty();
			$("#natureType").append($('<option value="cashier">Cashier</option>'));
			$("#natureType").append($('<option value="outwards">Outwards</option>'));
		}else if($(this).val() == "depositplacement"){
			$("#natureType").empty();
			$("#natureType").append($('<option value=" ">N/A</option>'));
		}else if($(this).val() == "fxforward"){
			$("#natureType").empty();
			$("#natureType").append($('<option value="forward">Forward</option>'));
			$("#natureType").append($('<option value="nds">NDS</option>'));
		}else if($(this).val() == "fxspot"){
			$("#natureType").empty();
			$("#natureType").append($('<option value=" ">N/A</option>'));
		}else if($(this).val() == "fxswap"){
			$("#natureType").empty();
			$("#natureType").append($('<option value=" ">N/A</option>'));
		}else if($(this).val() == "cashtransfer"){
			$("#natureType").empty();
			$("#natureType").append($('<option value="topb">To pb</option>'));
			$("#natureType").append($('<option value="toretail">To retail</option>'));
		}
	});
});


function changeServerImport(){
	var serverImport = document.getElementById("serverImport");
	var serverImportVal = document.getElementById("serverImportVal");
	if(serverImport.checked == true){
		serverImportVal.value = "on";
		console.log(serverImportVal.value);
	}else{
		serverImportVal.value = "off";
		console.log(serverImportVal.value);
	}
}
function changeSeverity(){
	var ignoreSeverity = document.getElementById("ignoreSeverity");
	var ignoreSeverityVal = document.getElementById("ignoreSeverityVal");
	
	if(ignoreSeverity.checked == true){
		ignoreSeverityVal.value = "on";
		console.log(ignoreSeverityVal.value);
	}else{
		ignoreSeverityVal.value = "off";
		console.log(ignoreSeverityVal.value);
	}
}

//For multiple Select Files
$(function () {
    var buttonLabel = $.ig.Upload.locale.labelUploadButton;
    if (Modernizr.input.multiple) {
        buttonLabel = "Drag and Drop Files Here <br/> or Click to Select From a Dialog";
    }
    $("#igUpload1").igUpload({
        mode: 'multiple',
        multipleFiles: true,
        maxUploadedFiles: 50,
        maxSimultaneousFilesUploads: 20,
        progressUrl: "http://moadonea612sk69:2001/clientloginaccount/uploadppk",
        controlId: "serverID1",
        labelUploadButton: buttonLabel,
        onError: function (e, args) {
            showAlert(args);
        }
    });
    if (Modernizr.input.multiple) {
        $(".ui-igstartupbrowsebutton").attr("style", "width: 300px; height: 50px;");
    }
   
});

function showAlert(args) {
    $("#error-message").html(args.errorMessage).stop(true, true).fadeIn(500).delay(3000).fadeOut(500);
}

//Toggle for show web element
function toggleWebElementName(num){
	
	var showWebElementName = document.getElementById("showWebElementName"+num);
	var webElementName = document.getElementById("webElementName"+num);
	
	if(showWebElementName.checked == true){
		webElementName.hidden = false;
	}else{
		webElementName.hidden = true;
	}
	
}

//Change serverimport Value

function changeServerImportValue(){
	var serverImport
}


//Data Table
$(document).ready(function() {
    $('#datatable').dataTable({
    	
    	//removing of sort btn
    	"searching":false,
    	"ordering":false
    
    	//"paging":false
    	
    	
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





document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('select');
    var instances = M.FormSelect.init(elems, options);
  });


//JavaScript for label effects only
$(window).load(function(){
	$(".col-3 input").val("");
	
	$(".input-effect input").focusout(function(){
		if($(this).val() != ""){
			$(this).addClass("has-content");
		}else{
			$(this).removeClass("has-content");
		}
	})
});

$(document)
.one('focus.autoExpand', 'textarea.autoExpand', function(){
    var savedValue = this.value;
    this.value = '';
    this.baseScrollHeight = this.scrollHeight;
    this.value = savedValue;
})
.on('input.autoExpand', 'textarea.autoExpand', function(){
    var minRows = this.getAttribute('data-min-rows')|0, rows;
    this.rows = minRows;
    rows = Math.ceil((this.scrollHeight - this.baseScrollHeight) / 16);
    this.rows = minRows + rows;
});


window.setTimeout(function() {
	
	$(".alert").fadeTo(400,0).slideUp(400,
			function(){
		$(this).remove();
	});
}, 2000);
