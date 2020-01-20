var searchBox = document.getElementById("searchBox");
var countries = document.getElementById("countries");
var when = "keyup"; //You can change this to keydown, keypress or change
console.log(countries.options);

	
searchBox.onkeyup = function(e){
	var text = e.target.value; //searchBox value
    var options = countries.options; //select options
    for (var i = 0; i < options.length; i++) {
        var option = options[i]; //current option
        var optionText = option.text; //option text ("Somalia")
        var lowerOptionText = optionText.toLowerCase(); //option text lowercased for case insensitive testing
        var lowerText = text.toLowerCase(); //searchBox value lowercased for case insensitive testing
        var regex = new RegExp("^" + text, "i"); //regExp, explained in post
        var match = optionText.match(regex); //test if regExp is true
        var contains = lowerOptionText.indexOf(lowerText) != -1; //test if searchBox value is contained by the option text
        if (match || contains) { //if one or the other goes through
            option.selected = true; //select that option
            return; //prevent other code inside this event from executing
        }
        searchBox.selectedIndex = 0; //if nothing matches it selects the default option
    }
};
/*
	searchBox.addEventListener("keyup", function (e) {
	    var text = e.target.value; //searchBox value
	    var options = countries.options; //select options
	    for (var i = 0; i < options.length; i++) {
	        var option = options[i]; //current option
	        var optionText = option.text; //option text ("Somalia")
	        var lowerOptionText = optionText.toLowerCase(); //option text lowercased for case insensitive testing
	        var lowerText = text.toLowerCase(); //searchBox value lowercased for case insensitive testing
	        var regex = new RegExp("^" + text, "i"); //regExp, explained in post
	        var match = optionText.match(regex); //test if regExp is true
	        var contains = lowerOptionText.indexOf(lowerText) != -1; //test if searchBox value is contained by the option text
	        if (match || contains) { //if one or the other goes through
	            option.selected = true; //select that option
	            return; //prevent other code inside this event from executing
	        }
	        searchBox.selectedIndex = 0; //if nothing matches it selects the default option
	    }
	});
*/

$(document).ready(function(){

		$(function() {
		    $('#select-name').selectize({    plugins: ['remove_button']
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
