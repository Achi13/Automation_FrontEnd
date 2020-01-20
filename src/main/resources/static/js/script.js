function uploadShFile(el){
	var file_data = $(el).prop("files")[0]; // Getting the properties of file from file field
	var form_data = new FormData(); // Creating object of FormData class
	form_data.append("shFile", file_data) // Appending parameter named file with properties of file_field to form_data
	$.ajax({
	    url: "http://moadonea220rywm:2001/script/uploadshfile", // Upload Script
	    dataType: 'json',
	    cache: false,
	    contentType: false,
	    processData: false,
	    data: form_data, // Setting the data attribute of ajax with file_data
	    type: 'post',
	    success: function(data) {
	      if(data.Error != null){
	    	  $(el).val(null);
	    	  alert(data.Error);
	      }else if(data.Info != null){
	    	  alert(data.Info);
	      }
	    }
	});
}