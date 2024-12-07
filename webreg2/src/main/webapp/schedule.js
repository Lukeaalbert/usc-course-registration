/**
 * 
 */
db_data = []
						
function saveSchedule(){
	//console.log(document.getElementById("searchResults"));
	var classes = "";
	
	var div = document.getElementById('searchResults');
	var divs = div.getElementsByTagName('div');
	for (var i = 0; i < divs.length; i += 1) {
		const element = divs[i];
		if(element.childNodes[1].checked){
			var courseDetails = db_data[i][Object.keys(db_data[i])[0]];
			classes += courseDetails[1] + ": " + courseDetails[2] + "\n";
			classes += courseDetails[5] + ", " + courseDetails[0] + "\n";
			
			// parse days
			let days = "";
		    for (let i = 0; i < courseDetails[6].length - 1; i++) {
		    	if (courseDetails[6][i] == "H") {
		    		days += "TH, ";
		    	}
		    	else {
		    		days += courseDetails[6][i] + ", ";
		    	}
		    }
		    if (courseDetails[6][courseDetails[6].length - 1] == "H") {
	    		days += "TH";
	    	}
	    	else {
	    		days += courseDetails[6][courseDetails[6].length - 1];
	    	}
			
			classes += days + "; " + courseDetails[7];
			classes += "\n\n";
		}
	  
	}
	console.log(classes);
	
	// Create a Blob containing the string "hello"
	const blob = new Blob([classes], { type: "text/plain" });
	
	// Create an anchor (link) element
	const link = document.createElement("a");
	
	// Create a URL for the Blob and set it as the link's href
	link.href = URL.createObjectURL(blob);
	
	// Set the download attribute to specify the filename
	link.download = "classes.txt";
	
	// Programmatically click the link to trigger the download
	link.click();
}

function formatResultDiv() {
	const checkboxes = document.querySelectorAll('.class-checkbox');
	
	checkboxes.forEach(checkbox => {
		const courseElement = checkbox.closest('.course-selection');
		if (courseElement && !checkbox.checked) {
			courseElement.remove();
		}
	});
	
	const errorMessage = document.getElementById("Search_Error");
	if (errorMessage){
		errorMessage.remove();
	}
}

document.addEventListener("DOMContentLoaded", function() {
    const availableClasses = [
        { name: "ACAD 182", type: "Lecture", day: "Monday", start: "10:00", end: "11:50" },
        { name: "AHIS 100", type: "Lecture", day: "Tuesday", start: "9:30", end: "10:50" },
        { name: "CSCI 201", type: "Lecture", day: "Tuesday", start: "11:00", end: "12:20" },
        { name: "CSCI 201", type: "Lab", day: "Tuesday", start: "14:00", end: "15:50" },
        { name: "ACAD 188", type: "Lecture", day: "Tuesday", start: "16:00", end: "17:50" },
        { name: "CSCI 270", type: "Lecture", day: "Monday", start: "15:30", end: "16:50" },
        { name: "ACAD 182", type: "Lecture", day: "Wednesday", start: "10:00", end: "11:50" },
        { name: "CSCI 270", type: "Lecture", day: "Wednesday", start: "15:30", end: "16:50" },
        { name: "AHIS 100", type: "Lecture", day: "Thursday", start: "9:30", end: "10:50" },
        { name: "CSCI 201", type: "Lecture", day: "Thursday", start: "11:00", end: "12:20" },
        { name: "AHIS 100", type: "Discussion", day: "Friday", start: "11:00", end: "11:50" },
        { name: "CSCI 270", type: "Discussion", day: "Friday", start: "12:00", end: "13:50" }
    ];

    const scheduleContainer = document.querySelector(".schedule-container");
    const dayOffsets = {
        "M": 1,
        "T": 2,
        "W": 3,
        "TH": 4,
        "F": 5
    };
    
    // search for classes
    document.getElementById('searchForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const searchInput = this.querySelector('.search-input');
        const searchQuery = searchInput.value.trim();

        if (searchQuery) {
            const formData = new FormData();
            formData.set("user_search", searchQuery);

            const urlEncodedData = new URLSearchParams(formData).toString();
            
            fetch('getClasses', {
                method: 'POST',
                headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: urlEncodedData
            })
            .then(response => {
                return response.json();
            })
            .then(data => {
				
                db_data = data;
				
				// clear unchecked boxes or previous error <p>,
				// keep checked (selected) boxes.
				formatResultDiv();
                
                const resultDiv = document.getElementById("searchResults");
				
                for (let i = 0; i < data.length; i++) {
                    const key = Object.keys(data[i])[0];
                    const courseDetails = data[i][key];
                    
                    // Handle case with no results 
                    if (i == 0 && courseDetails == "No Classes Found."){
						const tempDiv = document.createElement('div');
						tempDiv.innerHTML = `<p id = "Search_Error">No Classes Found.</p>`;
						resultDiv.appendChild(tempDiv.firstElementChild);
                        return;
                    }
                    
                    // Reformat days.
                    // Don't worry too much about this part, dummy.
                    // I know my code is cryptic. sorry about dat.
                    let days = "";
                    for (let i = 0; i < courseDetails[6].length - 1; i++) {
                        if (courseDetails[6][i] == "H") {
                            days += "TH, ";
                        }
                        else {
                            days += courseDetails[6][i] + ", ";
                        }
                    }
                    if (courseDetails[6][courseDetails[6].length - 1] == "H") {
                        days += "TH";
                    }
                    else {
                        days += courseDetails[6][courseDetails[6].length - 1];
                    }
                    
                     // green default (for lectures/random)
                    let color = "rgb(234, 255, 227)";
                    // purple for quiz
                    if (courseDetails[5] == "Qz") {
                        color = "rgb(222, 205, 247)";
                    }
                    // blue for discussion and lab
                    else if (courseDetails[5] == "Lab"
                            || courseDetails[5] == "Dis") {
                        color = "rgb(205, 241, 247)";
                    }

					const tempDiv = document.createElement('div');
					
					tempDiv.innerHTML = `
					    <div class="course-selection" style="background-color: ${color};">
					        <input type="checkbox" id="${courseDetails[0]}" class="class-checkbox" 
					               name="${courseDetails[0]}" 
					               value="${courseDetails[1]} $ ${courseDetails[5]} $ ${days} $ ${courseDetails[7]} $ ${courseDetails[0]}">
					        <label for="${courseDetails[0]}">
					            <strong>${courseDetails[1]}: ${courseDetails[2]}<br><br>
					            ${courseDetails[9]}</strong><br><br>
					            <i>${courseDetails[5]}, ${courseDetails[0]},<br>
					            ${days}; ${courseDetails[7]}</i>
					        </label>
					    </div>
					`;

					resultDiv.appendChild(tempDiv.firstElementChild);
                    
                    //console.log(`Course ${i + 1}:`);
                    //console.log(`ID: ${courseDetails[0]}`);
                    //console.log(`Course Number: ${courseDetails[1]}`);
                    //console.log(`Title: ${courseDetails[2]}`);
                    //console.log(`Description: ${courseDetails[3]}`);
                    //console.log(`Type: ${courseDetails[5]}`);
                    //console.log(`Day: ${courseDetails[6]}`);
                    //console.log(`Time: ${courseDetails[7]}`);
                    //console.log(`Location: ${courseDetails[8]}`);
                    //console.log(`Instructor: ${courseDetails[9]}`);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                const resultDiv = document.getElementById("searchResults");
				
				const tempDiv = document.createElement('div');
				tempDiv.innerHTML = '<p id = "Search_Error">An unexpected error occurred.</p>';
				resultDiv.appendChild(tempDiv.firstElementChild);
            });
        }
    });

    document.getElementById("submit-classes").addEventListener("click", function() {
        const selectedClasses = Array.from(document.querySelectorAll(".class-checkbox:checked")).map(checkbox => checkbox.value);
        renderSchedule(selectedClasses);
    });

    document.getElementById("submit-unavailable").addEventListener("click", function() {
        renderUnavailableTimes();
    });
	
	function uncheckCheckBox(id) {
	    const checkbox = document.getElementById(id);
	    if (checkbox) {
	        checkbox.checked = false;
	    }
	}

    function renderSchedule(selectedClasses) {
        //clear any prev schedules displayed
        scheduleContainer.querySelectorAll(".class-cell").forEach(cell => cell.remove());
        
        for (let i = 0; i < selectedClasses.length; i++) {
            const parts = selectedClasses[i].split(" $ ");

            const name = parts[0];
            const type = parts[1];
            const days = parts[2];
            const timeRange = parts[3];
            const [startTime, endTime] = timeRange.split("-");
			const id = parts[4];
            
            const st = parseTime(startTime);
            const et = parseTime(endTime);
            
            const duration = (et - st) / (1000 * 60); // duration in minutes
            const startOffset = (st.getHours() - 8) * 60 + st.getMinutes();
            
            console.log("days " + days);
            
            const dayArray = days.split(", ");

            for (let i = 0; i < dayArray.length; i++) {
                const day = dayArray[i];

                const classDiv = document.createElement("div");
                classDiv.classList.add("class-cell");
				classDiv.classList.add(id);
                classDiv.textContent = `${name} ${type}`;
                classDiv.style.top = `${(startOffset / 60) * 80}px`;
                classDiv.style.height = `${(duration / 60) * 80}px`;
                classDiv.style.left = `${dayOffsets[day] * 16.5}%`;
                classDiv.style.width = "16%";
                
                // Create overlay element
                const overlay = document.createElement("div");
                overlay.style.top = "0";
                overlay.style.left = "0";
                overlay.style.width = "100%";
                overlay.style.height = "100%";
                overlay.style.display = "none";
                overlay.style.justifyContent = "center";
                overlay.style.alignItems = "center";
                overlay.style.color = "white";
                overlay.style.fontWeight = "bold";
                
                classDiv.addEventListener("mouseenter", () => {
                    classDiv.style.transition = "background-color 0.3s ease-in-out";
                    classDiv.style.backgroundColor = "red";
                    overlay.style.opacity = "1";
                    overlay.style.display = "flex";
                });

                classDiv.addEventListener("mouseleave", () => {
                    classDiv.style.backgroundColor = ""; // Reset to original color
                    overlay.style.backgroundColor = "rgba(255, 0, 0, 0)";
                    overlay.style.opacity = "0";
                });
                
				classDiv.addEventListener("click", () => {
				    // Find the id class name
				    const class_id = Array.from(classDiv.classList).find(cls => cls !== "class-cell");
				    
				    if (class_id) {
				        const escapedClassName = CSS.escape(class_id);
				        const elementsToRemove = scheduleContainer.querySelectorAll(`.${escapedClassName}`);
				        elementsToRemove.forEach(element => {
				            scheduleContainer.removeChild(element);
				        });
				    }
					
					uncheckCheckBox(class_id);
				});
                
                classDiv.appendChild(overlay);
                
                scheduleContainer.appendChild(classDiv);
            }
        }
        
    }

    function renderUnavailableTimes() {
        //clear prev greyed out times
        scheduleContainer.querySelectorAll(".unavailable-block").forEach(block => block.remove());

        //display new greyed out times
        document.querySelectorAll(".unavailable-checkbox:checked").forEach(checkbox => {
            const [day, startHour] = checkbox.value.split("|");
            const startTime = new Date();
            startTime.setHours(Number(startHour));
            startTime.setMinutes(0);
            const endTime = new Date(startTime.getTime() + 60 * 60000); // Each block represents 60 minutes

            const duration = (endTime - startTime) / (1000 * 60); // duration in minutes
            const startOffset = (startTime.getHours() - 8) * 60 + startTime.getMinutes();

            const unavailableDiv = document.createElement("div");
            unavailableDiv.classList.add("unavailable-block");
            unavailableDiv.style.top = `${(startOffset / 60) * 80}px`;
            unavailableDiv.style.height = `${(duration / 60) * 80}px`;
            unavailableDiv.style.left = `${dayOffsets[day] * 16.5}%`;
            unavailableDiv.style.width = "16%";

            scheduleContainer.appendChild(unavailableDiv);
        });
    }

    function parseTime(timeString) {
        const [hours, minutes] = timeString.split(":").map(Number);
        const date = new Date();
        date.setHours(hours);
        date.setMinutes(minutes);
        date.setSeconds(0);
        date.setMilliseconds(0);
        return date;
    }

    //dropdown visibility
    document.querySelectorAll(".day-section h4").forEach(header => {
        header.addEventListener("click", function() {
            const dropdown = this.nextElementSibling;
            dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
        });
    });
});