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
    name: str
    time: str
    units: str

def getDepartmentJsonString(department_id: str, semester_id: str) -> str:
    url = f"https://web-app.usc.edu/web/soc/api/classes/{department_id}/{semester_id}"
    response = requests.get(url)
    return response.text

def createClassMapping(json_response: str) -> List[List[Section]]:
    try:
        data = json.loads(json_response)
    except json.JSONDecodeError as e:
        print(f"Error parsing JSON: {e}")
        return []

    courses = []
    
    # for all courses from the 'OfferedCourses' section
    for course in data.get("OfferedCourses", {}).get("course", []):
        course_data = course.get("CourseData", {})
        
        # Course Name
        name = f"{course_data.get('prefix', '')}-{course_data.get('number', '')}"
        
        # To hold all sections for a course
        sections = []
        for section_data in course_data.get("SectionData", []):
            # Error check to ensure that section is a dictionary
            if not isinstance(section_data, dict):
                print(f"Warning: Invalid section data: {section_data}")
                continue

            # Create the time string
            start_time = section_data.get('start_time', '')
            end_time = section_data.get('end_time', '')
            time = f"{start_time} - {end_time}"

            # Get units
            units = section_data.get("units", "")
            
            # Create section
            section = Section(
                name=name,
                time=time,
                units=units
            )
            sections.append(section)

        # Append iff sections found
        if sections:
            courses.append(sections)
    
    return courses

def print_courses(courses: List[List[Section]]):
    if not courses:
        print("No courses found.")
        return

    for course_sections in courses:
        print(f"Course sections:")
        for section in course_sections:
            print(f" Name: {section.name}")
            print(f" Time: {section.time}")
            print(f" Units: {section.units}")
            print()
        print("-" * 50)

if __name__ == "__main__":
    try:
        json_response = getDepartmentJsonString("CSCI", semester)
        courses = createClassMapping(json_response)
        print_courses(courses)
    except requests.RequestException as e:
        print(f"Error fetching data from API: {e}")
    except Exception as e:
        print(f"Unexpected error: {e}")