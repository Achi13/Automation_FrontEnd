jQuery(function ($) {

	$("#close-sidebar").click(function() {
	  $(".page-wrapper").removeClass("toggled");
	});
	$("#show-sidebar").click(function() {
	  $(".page-wrapper").addClass("toggled");
	});
 
   
});



var app = angular.module('mainBody', []);
	
app.controller('testcaseController', function($scope, $http){
	
	$index = 0;
	
	$http.get("/getrecords").then(function(response){
		$scope.data = response.data;
		console.log($scope.data);
	});
	
	
	$scope.getTestcaseData = function(testcase){
		$http.get("/gettestcasedata/" + testcase).then(function(response){
			$scope.testcasedata = response.data;
			console.log($scope.testcasedata);
		});
	};
	
	$scope.showPicture = function(path, index){
		$index = index;
		var encodedPath = path.toString().replace(new RegExp('/','g'), '%5C').replace(':', '%3A');
		console.log(encodedPath);
		$http.get("/showpicture?file=" + encodedPath).then(function(response){
			console.log(response.data[0]);
			$scope.imgSrc = response.data;
			var img = document.getElementById("img"+index);
			var modal = document.getElementById("myModal" + index);
			modal.style.display = "block";
			
		});
	};
	
	$scope.closePicture = function(index){
		var modal = document.getElementById("myModal" + index);
		modal.style.display = "none";
	}
	
});

function searchFunction() {
    var input, filter, ul, li, a, i, txtValue;
    input = document.getElementById("myInput");
    filter = input.value;
    ul = document.getElementById("searchList");
    li = ul.getElementsByTagName("li");
    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByTagName("label")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
    }
}




