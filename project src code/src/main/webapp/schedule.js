/**
 * 
 */
let db_data = []
						
function saveSchedule(){
	//console.log(document.getElementById("searchResults"));
    const now = new Date();

    // Define options for formatting date and time
    const options = {
    year: 'numeric',        // e.g., 2023
    month: '2-digit',       // e.g., 04
    day: '2-digit',         // e.g., 27
    hour: '2-digit',        // e.g., 03
    minute: '2-digit',      // e.g., 24
    second: '2-digit',      // e.g., 15
    hour12: true,           // Use 12-hour format (AM/PM)
    timeZoneName: 'short'   // e.g., GMT, PDT
    };

	var classes = `${localStorage.getItem("user_id")}'s saved schedule at ${now.toLocaleString(undefined, options)}\n\n`;

	//console.log(classes);
	document.querySelectorAll(".class-checkbox").forEach(box => {
        // console.log(typeof box.id);
        if(box.checked){
            for(var i = 0; i < db_data.length; i ++){
                var courseDetails = db_data[i][Object.keys(db_data[i])[0]];
                if(courseDetails[0] === box.id){
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
                    break;
                }
            }
        }
    });
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

async function getClassesFromBackend(){
    const searchInput = document.getElementById('searchForm').querySelector('.search-input');
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
            
            db_data = db_data.concat(data);
            
            // clear unchecked boxes or previous error <p>,
            // keep checked (selected) boxes.
            formatResultDiv();
            
            const resultDiv = document.getElementById("searchResults");
            
            for (let i = 0; i < data.length; i++) {
                const key = Object.keys(data[i])[0];
                const courseDetails = data[i][key];
                //courseDetails[0]

                if (document.getElementById(courseDetails[0])) {
                    continue;
                }
                
                // Handle case with no results 
                if (i == 0 && courseDetails == "No Classes Found."){
                    const tempDiv = document.createElement('div');
                    tempDiv.innerHTML = `<p id = "Search_Error">No Classes Found.</p>`;
                    resultDiv.prepend(tempDiv.firstElementChild);
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
                               value="${courseDetails[1]} $ ${courseDetails[5]} $ ${days} $ ${courseDetails[7]} $ ${courseDetails[0]}"
                               id="${courseDetails[0]}">
                        <label for="${courseDetails[0]}">
                            <strong>${courseDetails[1]}: ${courseDetails[2]}<br><br>
                            ${courseDetails[9]}</strong><br><br>
                            <i>${courseDetails[5]}, ${courseDetails[0]},<br>
                            ${days}; ${courseDetails[7]}</i>
                        </label>
                    </div>
                `;

                resultDiv.prepend(tempDiv.firstElementChild);
                
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

	
function uncheckCheckBox(id) {
    const checkbox = document.getElementById(id);
    if (checkbox) {
        checkbox.checked = false;
    }
}

function renderSchedule(selectedClasses) {
    //clear any prev schedules displayed
    var scheduleContainer = document.querySelector(".schedule-container")
    const cells = scheduleContainer.querySelectorAll(".class-cell");
    if (cells && cells.length) {
        cells.forEach(cell => cell.remove());
    }
    
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
        
        //console.log("days " + days);
        
        const dayArray = days.split(", ");

        for (let i = 0; i < dayArray.length; i++) {
            const day = dayArray[i];

            const classDiv = document.createElement("div");
            classDiv.classList.add("class-cell");
            classDiv.id = `in-calendar-${id}`;
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
                var class_id = id;
                if(class_id){
                    var element = document.getElementById(`in-calendar-${class_id}`);
                    while(element){
                        scheduleContainer.removeChild(element);
                        element = document.getElementById(`in-calendar-${class_id}`);
                    }
                }
                uncheckCheckBox(class_id);
            });
            
            classDiv.appendChild(overlay);
            
            scheduleContainer.appendChild(classDiv);
        }
    }
    
}

const dayOffsets = {
    "M": 1,
    "T": 2,
    "W": 3,
    "TH": 4,
    "F": 5
};

document.addEventListener("DOMContentLoaded", function() {
    
    // search for classes
    document.getElementById('searchForm').addEventListener('submit', function(event) {
        event.preventDefault();
        getClassesFromBackend();
    });

    document.getElementById("optimize-button").addEventListener("click", function(){

        // var wantedClasses = ['29904R', '30028R', '30107R', '30108R', '30134R', 
        //     '30237R', '30238R', '30239R', '30241R', '30385R', '30389D', '30396R', 
        //     '29994D', '30029R', '30109R', '30190R', '30222R', '30245R', '30294R', 
        //     '30361R', '30362R', '30363R', '19880R', '19881R', '19882R', '19883R', 
        //     '19884R', '19885R', '19886R', '49739R', '49740R', '49741R', '49743R', 
        //     '49744R', '49746R', '50420R', '50421R', '50426R', '50427R', '50428R', 
        //     '50429R', '50430R'];

        var wantedClasses = [];

        document.querySelectorAll(".class-checkbox").forEach(box => {
            // console.log(typeof box.id);
            if(box.checked){
                wantedClasses.push(box.id);
            }
        });

        optimizeSchedule(wantedClasses); 
    });

    document.getElementById("put-on-calendar").addEventListener("click", function() {
        const selectedClasses = Array.from(document.querySelectorAll(".class-checkbox:checked")).map(checkbox => checkbox.value);
		//console.log(selectedClasses);
        renderSchedule(selectedClasses);
    });

    document.getElementById("submit-unavailable").addEventListener("click", function() {
        renderUnavailableTimes();
    });

    function renderUnavailableTimes() {
        var scheduleContainer = document.querySelector(".schedule-container");
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
            unavailableDiv.style.position="absolute";
            unavailableDiv.style.marginTop = `${(startOffset / 60) * 83 + 3}px`;
            unavailableDiv.style.height = `${(duration / 60) * 80}px`;
            unavailableDiv.style.marginLeft = `${dayOffsets[day] * 16.7 - 2}%`;
            unavailableDiv.style.width = "16%";

            scheduleContainer.appendChild(unavailableDiv);
        });
    }
    //dropdown visibility
    document.querySelectorAll(".day-section h4").forEach(header => {
        header.addEventListener("click", function() {
            const dropdown = this.nextElementSibling;
            dropdown.style.display = dropdown.style.display === "none" ? "block" : "none";
        });
    });
});

// OPTIMIZE

function convertClassesToStrings(returnedClasses) {
    var dayMap = {
        0: "M",
        1: "T",
        2: "W",
        3: "TH",
        4: "F"
    };

    var formattedClasses = [];

    returnedClasses.forEach(classItem => {
        var { name, type, dClassCode, id, startTime, endTime, dates } = classItem;

        // Helper function to format time with leading zeros
        var formatTime = (time) => {
            var hour = String(time.hour).padStart(2, '0');
            var minute = String(time.minute).padStart(2, '0');
            return `${hour}:${minute}`;
        };

        var start = formatTime(startTime);
        var end = formatTime(endTime);
        var timeRange = `${start}-${end}`;

        dates.forEach((flag, index) => {
            if (flag === 1) {
                var day = dayMap[index];
                var formattedString = `${name} $ ${type} $ ${day} $ ${timeRange} $ ${id}`;
                formattedClasses.push(formattedString);
            }
        });
    });

    return formattedClasses;
}

function convertClassesToIDs(returnedClasses) {

    var formattedClasses = [];
    returnedClasses.forEach(classItem => {
        formattedClasses.push(classItem.id);
    });
    return formattedClasses;
}


async function optimizeSchedule(wantedClasses1) {
    console.log(wantedClasses1);

    try {
        const response = await fetch('AlgorithmServlet', {
            method: 'POST', headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(wantedClasses1)
        });

        const parsedMsg = await response.json();
        if (!response.ok) {
            console.log(`${parsedMsg}`);
            throw new Error(`${parsedMsg}`);
        }
        else {
            //console.log(parsedMsg);
            var selectedClasses = convertClassesToStrings(parsedMsg);
            var selectedIDs = convertClassesToIDs(parsedMsg);
            // console.log(selectedIDs);
            document.querySelectorAll(".class-checkbox").forEach(box => {
                // console.log(typeof box.id);
                if (!selectedIDs.includes(box.id)) {
                    box.checked = false;
                }
            });

            renderSchedule(selectedClasses);
            
        }
    }
    catch (error) {
        console.log(error);
    }
}
