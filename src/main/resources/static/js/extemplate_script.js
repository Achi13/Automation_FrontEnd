jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
	});
 
   
});

function changeInputValue(){
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

$(document).ready(function(){
	
	$("#row1").css("visibility","collapse");
	$("#row2").css("visibility","collapse");
	$("#row3").css("visibility","collapse");
	
	
	var date = new Date();
	
	var month;
	
	if((date.getMonth()+1)<10){
		month = "0" + (date.getMonth() + 1).toString();
	}else{
		month = (date.getMonth()+1).toString();
	}
	$("input[name='inputOutput']").attr("readonly", true);
	$("tr").each(function (index){
		index++;
		if($("#natureOfAction"+index).val() == "input" || $("#natureOfAction"+(index)).val() == "compare" || $("#natureOfAction"+(index)).val() == "select"){
			$("#inputOutput"+index).attr("readonly", false);
			$("#inputOutput"+index).attr("required", true);
		}else if($("#natureOfAction"+index).val() == "datepicker"){
			$("#inputOutput"+index).attr("readonly", false);
			$("#inputOutput"+index).attr("required", true);
			$("#inputOutput"+index).addClass('datepicker');
			$("#inputOutput"+index).val(date.getFullYear().toString() + "/" + month + "/" + date.getDate().toString());
			$("#inputOutput"+index).attr("data-date", " ");
			$("#inputOutput"+index).attr("data-date-format", "MMM DD, YYYY");
			$("#inputOutput"+index).prop("type", "date");
			$("#inputOutput"+index).on("change", function() {
			    this.setAttribute(
			        "data-date",
			        moment(this.value, "YYYY-M-DD").format( this.getAttribute("data-date-format"))
			    )
			}).trigger("change")
		}
	});
	$("#infoText").delay(5000).fadeOut();	
});
//Make inputoutput field readonly and required and vice versa

function changeInput(num){
	
}

//change value of triggerEnterValue to true or false

function changeTriggerEnter(num){
	
}


//change value of screenCaptureValue to true or false
function changeScreenCapture(num){
	
	
}
//Toggle for show web element
function toggleWebElementName(num){
	console.log("Working");
	var webElementName = document.getElementById("webElementName"+num);
	
	if(webElementName.hidden == true){
		webElementName.hidden = false;
	}else{
		
		webElementName.hidden = true;
	}
	console.log(webElementName.hidden);
	
}

//FUNCTION For Angular

var app = angular.module('mainBody', []);

app.controller('mainController', function($scope, $http){
	
	
	
	$http.get("http://moadonea220rywm:2001/clientloginaccount/getclientloginaccountscript")
	.then(function(response){
		$scope.scriptCredential = response.data;
		console.log($scope.scriptCredential.clientLoginAccountList)
	});
	
	$scope.filterScripts = function(){
		var scriptCredential = document.getElementById("scriptCredential").value;
		$http.get("http://moadonea220rywm:2001/script/getscriptbyloginaccountid?loginAccountId=" + scriptCredential)
		.then(function(response){
			
			$scope.scriptList = response.data;
			console.log($scope.scriptList.scriptList);
		});
	}
	
	$scope.filterWebPath = function(){
		var clientValue = document.getElementById("clientFilter").value;
		var userId = document.getElementById("userId").value;
		$http.get("http://moadonea220rywm:2001/webaddress/getwebaddress?clientId=" + clientValue)
		.then(function(response){
			$scope.webAddressData = response.data;
			console.log($scope.webAddressData.webAddressData[0]);
		});
		$http.get("http://moadonea220rywm:2001/templates/gettemplates?clientId=" + clientValue +
				"&userId=" + userId)
		.then(function(response){
			$scope.templatesData = response.data;
			console.log($scope.templatesData.templatesList);
		});
	}
	
	$scope.filterAssignedAccount = function(){
		var webPathFilter = document.getElementById("webPathFilter").value;
		$http.get("http://moadonea220rywm:2001/clientloginaccount/getloginaccount?url=" + webPathFilter)
		.then(function(response){
			$scope.clientLoginAccountData = response.data;
			console.log($scope.clientLoginAccountData);
		});
	}
	
	$scope.getTemplateData = function(){
		var templateFilter = document.getElementById("templateFilter").value;
		console.log(templateFilter);
		$http.get("http://moadonea220rywm:2001/templatedata/gettemplatedata?templateId=" + templateFilter)
		.then(function(response){
			$scope.templateData = response.data;
			console.log($scope.templateData);
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

function changeDate(el){
	 el.setAttribute(
        "data-date",
        moment(el.value, "YYYY-M-DD")
        .format( el.getAttribute("data-date-format") ))
}

//Data Table
$(document).ready(function() {
	

    $('#datatable').dataTable({
    	
    	//removing of sort btn
    	"searching":false,
    	"ordering":false
    	
    });    
});


$(document).ready(function(){

	
});

function changeIsPublic(){
	var isPublic = document.getElementById("isPublic");
	var isPublicVal = document.getElementById("isPublicVal");
	
	if(isPublic.checked == true){
		isPublicVal.value = "true";
		console.log(isPublicVal.value);
	}else{
		isPublicVal.value = "false";
		console.log(isPublicVal.value);
	}
}

function removeRow(el){
	
	var element = el.parentNode.parentNode;
	element.parentNode.removeChild(element);
	
}

function addRow(el){
	
	var counter = document.getElementById("elementTable").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;
	
	console.log(counter);
	
	var element = '<tr id="row' + counter + '">' + 
		'<td>' +
			'<input type="text" hidden="hidden" name="templateDataId" value="0" readonly="readonly"/>' +
			'<input type="text" name="label" class="form-control"/></td>' +
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
			'<input type="text" name="webElementName" class="form-control" id="webElementName' + counter + '" hidden="hidden"/>' +
			'<div align="center" class="switchToggle1"><input type="checkbox" onchange="toggleWebElementName(' + counter + ')" id="showWebElementName' + counter + '"/><label for ="showWebElementName' + counter + '"></label></div>' +
		'</td>' + 
		'<td>' + 
			'<div onclick="addRow(this)" class="addButton btn btn-primary" "><i class="fas fa-plus"></i></div>' +
			'<div onclick="removeRow(this)" class="removeButton btn btn-danger" "><i class="fas fa-trash-alt"></i></div>' +
		'</td>'
	'</tr>';
	$(element).insertAfter(el.closest("tr"));
	counter++;
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

