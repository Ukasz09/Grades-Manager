# :bar_chart: Grades-Manager [![License](https://img.shields.io/badge/licence-MIT-blue)](https://choosealicense.com/licenses/mit/)

Simplified grades manager made for university classes. Main idea of project has been implementing unit, integrational and acceptance tests.

## About

Implemented code was intendeed to comply imposed on specification and give interface which allowed to run this commands:

- `<main_app_path> student <name> <nazwisko>` - adding new student to database  
- `<main_app_path> add subject <subject_name>` - adding new subject to database
- `<main_app_path> del student <name> <nazwisko>` - removing student from database 
- `<main_app_path> del subject <subject_name>` - removing subject from database 
- `<main_app_path> count students` - count students stored in a database  
- `<main_app_path> count subjects` - count subjects stored in a database
- `<main_app_path> set grade <name> <nazwisko> <subject_name> <grade>` - adding new grade to student from given subject
- `<main_app_path> average <name> <nazwisko> <subject_name>` - calculating average grade from given subject 

## Used tools / libraries

- **Language:** Java
- **Build system:** Gradle
- **Database:** MongoDB
- **DB Mapper:** Jongo
- **Tests library:** JUnit 5 
- **Mocking library:** Mockito
- **Acceptance tests script language:** Bash 

MongoDB as a database was used mainly for educational purpose. In this case overengineering is kinda intend

## Used metodology

- ✅ TDD (Test Driven Development)

## Tests

**Total tests qty: 193**

- **Unit tests:** 91
- **Integration tests:** 47
- **Acceptance tests:** 55

In order to mock DB dependencies in unit tests, have been applied mocks and spies with *Mockito* library


### Total code coverage:**
- **Classes:** 100%
- **Methods:** 96,6%
- **Lines of code:** 97,2%

![coverage](/readme_images/coverage.png)

Total of about **1700** lines of tests code. Full code coverage report in the form of `.html` files, can be found in `coverage.zip`

## How to use it?

You can run all tests from IDE or using gradle tool: 

**Units tests:** 
```bash
./gradle test
```
**Integration:** 
```bash
./gradle testInteg
```
**Acceptance**
```bash
./acceptance_tests.sh <main_app_path>
```
___
In order to test application API you can:
- a) Run java jar application: `java -jar app.jar <arguments>` for example:

```bash
java -jar app.jar add student Łukasz Gajerski
```
</br>

- b) Run wrapping scripts `./app.sh <arguments>` or `./app.bat <arguments>` for example:

**Linux**
```bash
./app.sh add subject Biology
```

**Windows**
```cmd
./app.bat add subject Biology
```

___
You can run acceptance tests by using `acceptance_tests.sh`. 

**Acceptance tests syntax**: 
```bash
./acceptance_tests.sh <path to main app file>
```

You can pass ass argument any app implementation which is comply with API mentioned in ([About](https://github.com/Ukasz09/Grades-Manager#about)) section. Test will generate results for every tests and summary report. For example:

![acceptance_tests](/readme_images/acceptance_summary.png)

___
## 📫 Contact 
Created by <br/>
<a href="https://github.com/Ukasz09" target="_blank"><img src="https://avatars0.githubusercontent.com/u/44710226?s=460&v=4"  width="100px;"></a>
<br/> gajerski.lukasz@gmail.com - feel free to contact me! ✊

