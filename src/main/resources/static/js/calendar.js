		jQuery(function ($) {
		
		$("#close-sidebar").click(function() {
		  $(".page-wrapper").removeClass("toggled");
		});
		$("#show-sidebar").click(function() {
		  $(".page-wrapper").addClass("toggled");
			});
		 
		   
		});

		var events = [];
			
			
			$(document).ready(function(){
				var calendarEl = document.getElementById("calendar");
		        
		        
		        $.ajax({
		        	url: "http://moadonea220rywm:2001/campaign/getschedule",
		        	success: function(data){
		        		$.each(data.campaignList, function(index, value){
		        			
		        			events.push({
		        				title: value.campaignName,
		        				start: value.executionSchedule,
		        				end: value.executionSchedule,
		        				backgroundColor: value.status,
		        				textColor:"white",
		        				//EXTENDEN PROPS
		        				creator: value.userId.username,
		        				summary: value.timestamp,
		        				testcases: value.executionVersionCurrent,
		        				description: value.description
		        			});
		        		});
		        		console.log(events);
		        		generateCalendar(events);
		        	}
		        });
			});
	      function onloadBody() {
	    	
	        var calendarEl = document.getElementById("calendar");
	        
	        
	        $.ajax({
	        	url: "http://moadonea220rywm:2001/campaign/getschedule",
	        	type: 'GET',
	        	success: function(data){
	        		$.each(data.campaignList, function(index, value){
	        			
	        			events.push({
	        				title: value.campaignName,
	        				start: value.executionSchedule,
	        				end: value.executionSchedule
	        			});
	        		});
	        		
	        		generateCalendar(events);
	        	}
	        });
			
	        //calendar.render();
	      }
	      
	      function generateCalendar(events){
	    	  	var calendarEl = document.getElementById('calendar');

	    	    var calendar = new FullCalendar.Calendar(calendarEl, {
		    	      plugins: [ 'dayGrid' ], 
		    	      businessHours: true, // display business hours
		    	      contentHeight:845,
		    	      events: events,
		    	      eventClick: function(info){
		    	    	  console.log(info);
		    	    	  
		    	    	  $("#modalTitle").html(info.event.title);
		    	    	  
	    	    		  $("#creatorText").html(info.event.extendedProps.creator);
		    	    	  
		    	    	  $("#descriptionText").html(info.event.extendedProps.description);
		    	    	  
		    	    	  $("#testcaseText").html(info.event.extendedProps.testcases);
		    	    	  
		    	    	  $("#summaryText").html(info.event.extendedProps.summary);
		    	    	  
		    	    	  jQuery.noConflict();
		    	    	  $("#eventModal").modal('show');
		    	    	  
		    	      }
	    	    });

	    	    calendar.render();
	        }
	      
	      
	      