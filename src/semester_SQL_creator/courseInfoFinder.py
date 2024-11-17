import json
import requests
from dataclasses import dataclass
from typing import List

departments = ["AHIS", "ALI", "AMST", "ANTH", "ARAB", "ASTR", "BISC", "CHEM", "CLAS", "COLT", "CORE", "CSLC",
               "EALC", "EASC", "ECON", "ENGL", "ENST", "FREN", "GEOG", "GEOL", "GERM", "SWMS", "GR", "HEBR",
               "HIST", "HBIO", "INDS", "IR", "IRAN", "ITAL", "JS", "LAT", "LING", "MATH", "MDA", "MDES", "MPW",
               "NEUR", "NSCI", "OS", "PHED", "PHIL", "PHYS", "POIR", "PORT", "POSC", "PSYC", "REL", "RNR",
               "SLL", "SOCI", "SPAN", "SSCI", "SSEM", "USC", "VISS", "WRIT", "ACCT", "ARCH", "ACAD", "ACCT",
               "BAEP", "BUAD", "BUCO", "DSO", "FBE", "GSBA", "MKT", "MOR", "HRM", "CMPP", "CNTV", "CTAN",
               "CTCS", "CTIN", "CTPR", "CTWR", "IML", "ASCJ", "CMGT", "COMM", "DSM", "JOUR", "PR", "PUBD",
               "DANC", "DENT", "CBY", "DHIS", "THTR", "EDCO", "EDHP", "EDUC", "AME", "ASTE", "BME", "CHE", "CE",
               "CSCI", "EE", "ENE", "ENGR", "ISE", "INF", "ITP", "MASC", "PTE", "SAE", "ART", "CRIT", "DES",
               "FA", "FACE", "FACS", "FADN", "FADW", "FAIN", "FAPH", "FAPT", "FAPR", "FASC", "WCT", "GCT",
               "SCIN", "SCIS", "ARLT", "SI", "ARTS", "HINQ", "SANA", "LIFE", "PSC", "QREA", "GPG", "GPH",
               "GESM", "GERO", "LAW", "ACMD", "ANST", "BIOC", "CBG", "DSR", "HP", "INTD", "MEDB", "MEDS",
               "MICB", "MPHY", "MSS", "NIIN", "PATH", "PHBI", "PM", "PCPA", "SCRM", "ARTL", "MTEC", "MSCR",
               "MTAL", "MUCM", "MUCO", "MUCD", "MUEN", "MUHL", "MUIN", "MUJZ", "MPEM", "MPGU", "MPKS", "MPPM",
               "MPST", "MPVA", "MPWP", "MUSC", "SCOR", "OT", "HCDA", "PHRD", "PMEP", "RXRS", "BKN", "PT",
               "AEST", "HMGT", "MS", "NAUT", "NSC", "PPD", "PPDE", "PLUS", "RED"]

# Note: 20243 = Fall 2024
semester = 20243

@dataclass
class Section:
    id_and_d_class_code: str
    type: str
    day: str
    time: str
    loc: str
    prof_name: str

@dataclass
class Course:
    name: str
    title: str
    description: str
    units: str
    sections: List[Section]

def getDepartmentJsonString(department_id: str, semester_id: str) -> str:
    url = f"https://web-app.usc.edu/web/soc/api/classes/{department_id}/{semester_id}"
    response = requests.get(url)
    return response.text

def createClassMapping(json_response: str) -> List[Course]:
    try:
        data = json.loads(json_response)
    except json.JSONDecodeError as e:
        print(f"Error parsing JSON: {e}")
        return []

    courses = []
    
    # for all courses from the 'OfferedCourses' section
    for course in data.get("OfferedCourses", {}).get("course", []):
        if not isinstance(course, dict):
            print(f"Warning: Invalid course data: {course}")
            continue
        course_data = course.get("CourseData", {})
        
        # Course Name
        name = f"{course_data.get('prefix', '')}-{course_data.get('number', '')}"
        # Get course title (ie, "Explorations in Computing")
        title = course.get("title", '')
        if (title == ''):
            title = "N/A"
        # Get course description
        description = course.get("description", '')
        if (description == ''):
            description = "N/A"
        # Get units for course (split from "4.0, 0" to "4.0")
        units = (course.get("units", '')).split(",", 1)[0]
        if (units == ''):
            units = "N/A"
        
        # To hold all sections for a course
        sections = []
        for section_data in course_data.get("SectionData", []):
            # Error check to ensure that section is a dictionary
            if not isinstance(section_data, dict):
                print(f"Warning: Invalid section data: {section_data}")
                continue

            id_and_d_class_code = f"{section_data.get('id', '')} {section_data.get('dclass_code', '')}"
            if (id_and_d_class_code == ''):
                id_and_d_class_code = "Unknown"

            type = section_data.get('type', '')
            if (type == ''):
                type = "Unknown"
            
            day = section_data.get('day', '')
            if (day == ''):
                day = "Unknown"

            # Create the time string
            start_time = section_data.get('start_time', '')
            end_time = section_data.get('end_time', '')
            time = f"{start_time} - {end_time}"
            if (time == ''):
                time = "Unknown"

            loc = section_data.get('location', '')
            if (loc == ''):
                loc = "Unknown"
            
            prof = section_data.get('instructor', {})
            if isinstance(prof, dict) and 'first_name' in prof and 'last_name' in prof:
                prof_name = f"{prof['last_name']}, {prof['first_name']}"
            else:
                prof_name = "Unknown"
            
            # Create section
            section = Section(
                id_and_d_class_code=id_and_d_class_code,
                type=type,
                day=day,
                time=time,
                loc=loc,
                prof_name=prof_name
            )
            sections.append(section)

        # Append iff sections found
        if sections:
            course = Course(
                name=name, 
                title=title, 
                description=description,
                units=units,
                sections=sections
            )
            courses.append(course)
    
    return courses

def print_courses(courses: List[Course]):
    if not courses:
        print("No courses found.")
        return

    for course in courses:
        print(f" Name: {course.name}")
        print(f" Title: {course.title}")
        print(f" Description: {course.description}")
        print(f" Units: {course.units}")
        print()
        print("-" * 50)
        for section in course.sections:
            print(f" ID: {section.id_and_d_class_code}")
            print(f" Type: {section.type}")
            print(f" Day: {section.day}")
            print(f" Time: {section.time}")
            print(f" Loc: {section.loc}")
            print(f" Prof Name: {section.prof_name}")
            print()
            print("-" * 50)

if __name__ == "__main__":
    try:
        for department in departments:
            json_response = getDepartmentJsonString(department, semester)
            courses = createClassMapping(json_response)
            print_courses(courses)
    except requests.RequestException as e:
        print(f"Error fetching data from API: {e}")
    except Exception as e:
        import traceback
        print(f"Unexpected error: {e}")
        print(f"Error occurred on line {traceback.extract_tb(e.__traceback__)[-1].lineno}")