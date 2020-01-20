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
  
  window.setTimeout(function() {
		
		$(".alert").fadeTo(400,0).slideUp(400,
				function(){
			$(this).remove();
		});
	}, 2000);

  
  $("#checkAll").click(function () {
  	console.log("sample");
      $('input:checkbox').prop('checked', 'true');
  });
  
  $("tbody").sortable({
      appendTo: "parent",
      helper: "original",
      update: function(event,ui){
    	console.log(ui.item[0]);
        if(ui.item[0].previousElementSibling == null || ui.item[0].previousElementSibling.id == ""){
        	console.log("case 1");
        	var jsonData = {"testcaseNumber1":ui.item[0].id.toString(),
          				"testcaseNumber2":"0",
          				"testcaseNumber3":ui.item[0].nextElementSibling.id.toString()};
        	$.ajax({
          	url: "http://moadonea220rywm:2001/dependenttestcase/swapdependenttestcase",
            type: "POST",
            data: JSON.stringify(jsonData),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success:function(data){
            	alert(data);
            }
          });
        }else if(ui.item[0].nextElementSibling == null || ui.item[0].nextElementSibling.id == ""){
        	console.log("case 2");
        	var jsonData = {"testcaseNumber1":ui.item[0].id.toString(),
          				"testcaseNumber2":ui.item[0].previousElementSibling.id.toString(),
          				"testcaseNumber3":"0"};
        	$.ajax({
          	url: "http://moadonea220rywm:2001/dependenttestcase/swapdependenttestcase",
            type: "POST",
            data: JSON.stringify(jsonData),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success:function(data){
            	alert(data);
            }
          });
        }else{
        	console.log("case 3");
        	var jsonData = {"testcaseNumber1":ui.item[0].id.toString(),
          				"testcaseNumber2":ui.item[0].previousElementSibling.id.toString(),
          				"testcaseNumber3":ui.item[0].nextElementSibling.id.toString()};
        	$.ajax({
          	url: "http://moadonea220rywm:2001/dependenttestcase/swapdependenttestcase",
            type: "POST",
            data: JSON.stringify(jsonData),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success:function(data){
            	alert(data);
            }
          });
        	//console.log(ui.item[0].previousElementSibling.id);
        }
        
      }
      
  }).disableSelection();
  
  

  
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

var app = angular.module('mainBody', []);
app.controller('mainController', function($scope, $http){
	
	$http.get("http://moadonea220rywm:2001/clientloginaccount/getclientloginaccountscript")
	.then(function(response){
		$scope.scriptCredential = response.data;
		console.log($scope.scriptCredential.clientLoginAccountList)
	});
	
	
});




