#!/bin/bash
APP_PATH=${1}
PASSED_TESTS_QTY=0
FAILED_TESTS_QTY=0
POSITIVE_MSG="OK"
NEGATIVE_MSG="ERROR"
TITLE_FONT="\e[1;34m"
RESULT_FONT="\e[1;33m"
SPLIT_FONT="\e[1;40m"
RESET_FONT="\e[0m"
PASSED_FONT="\e[1;47m\e[1;30m"
FAILED_FONT="\e[1;41m"

###############################################################################################################################
# API
###############################################################################################################################
retVal=""

doTest(){
    name=${1}
    expected=${2}
    Cmd="${APP_PATH} ${@:3}"
    actual="$($Cmd)"
    actual_trimmed="$(echo -e "${actual}" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
    
    checkTestResult ${name} ${expected} ${actual_trimmed}
    printTestInfo ${name} ${expected} ${actual_trimmed} ${Cmd}
}

checkTestResult(){
    name=${1}
    excepted=${2}
    actual=${3}
    if [ "${expected}" = "${actual}" ]; then
        retVal="${PASSED_FONT} PASSED ${RESET_FONT}"
        ((PASSED_TESTS_QTY++))
    else
        retVal="${FAILED_FONT} FAILED ${RESET_FONT}"
        ((FAILED_TESTS_QTY++))
    fi
}

printTestInfo(){
    echo "________________________________________________________________________________________________________"
    echo -e "${TITLE_FONT} Name: ${RESET_FONT} ${1}"
    echo -e "${TITLE_FONT} Cmd: ${RESET_FONT} ${@:4}"
    echo -e "${TITLE_FONT} Expected: ${RESET_FONT} ${2}"
    echo -e "${TITLE_FONT} Actual: ${RESET_FONT} ${3}"
    echo -e "${TITLE_FONT} Result: ${RESET_FONT} ${retVal}"
}

printTestsSummary(){
    echo ""
    echo -e "${SPLIT_FONT} #-------------------------------------------------------------------------------------------------------# ${RESET_FONT}"
    echo ""
    allTestsQty=$((FAILED_TESTS_QTY+PASSED_TESTS_QTY))
    passedPercents=$(echo "scale=2; ${PASSED_TESTS_QTY}/${allTestsQty}*100" | bc)
    echo -e "${RESULT_FONT} DONE TESTS QTY: ${RESET_FONT} ${allTestsQty}"
    echo -e "${RESULT_FONT} FAILED TESTS QTY: ${RESET_FONT} ${FAILED_TESTS_QTY}"
    echo -e "${RESULT_FONT} PASSED TESTS QTY: ${RESET_FONT} ${PASSED_TESTS_QTY}"
    echo -e "${RESULT_FONT} PASSED TESTS: ${RESET_FONT} ${PASSED_FONT} ${passedPercents} % ${RESET_FONT}"
    echo ""
}

#############################################################################################################################
# TESTS
#############################################################################################################################
doTest "givenEmptyCollectionWhenDeleteStudentThenNoChanges" ${NEGATIVE_MSG} "del student John Mackenzi"
doTest "givenStudentWithNameStartedLowercaseWhenAddThenNotAdded" ${NEGATIVE_MSG} "add student thomson Warter"
doTest "givenStudentWithNameStartedLowercaseWhenAddThenNotAdded" ${NEGATIVE_MSG} "add student thomson Warter"
doTest "givenStudentWithSurnameStartedWithLowercaseWhenAddStudentThenNotAdded" ${NEGATIVE_MSG} "add student Thomson warter"
doTest "givenEmptyCollectionWhenAvgThen0" "0.0" "average Penelope Cruze Math"
doTest "givenEmptyCollectionWhenAddGradeThenNoChanges" ${NEGATIVE_MSG} "set grade John Carter Biology 4"
doTest "givenEmptyCollectionWhenCountThen0" "0" "count students"
doTest "givenEmptyCollectionWhenAddCorrectStudentThenAdded" ${POSITIVE_MSG} "add student John Carter"
doTest "given1StudentInCollectionWhenDeleteThisStudentThenDeleted" ${POSITIVE_MSG} "del student John Carter"
doTest "givenEmptyCollectionWhenAddStudentDeletedStudentThenAdded" ${POSITIVE_MSG} "add student John Carter"
doTest "given1StudentsInCollectionWhenAvgOfDifferentStudentThen0" "0.0" "average Tom Garsia Math"
doTest "givenEmptySubjectsAnd1StudentInCollectionWhenAvgThen0" "0.0" "average John Carter Math"
doTest "given1StudentInCollectionWhenAddTheSameStudentThenNotAdded" ${NEGATIVE_MSG} "add student John Carter"
doTest "givenNotEmptyCollectionWhenAddStudentWithOnlyTheSameNameThenAdded" ${POSITIVE_MSG} "add student John Dicaprio"
doTest "givenNotEmptyCollectionWhenAddStudentWithTheOnlySameSurnameThenAdded" ${POSITIVE_MSG} "add student Eleonore Carter"
doTest "givenNotEmptyCollectionWhenAddDifferentStudentThenAdded" ${POSITIVE_MSG} "add student Penelope Cruze"
doTest "given4StudentsInCollectionWhenDeleteDifferentStudentThenNoChanges" ${NEGATIVE_MSG} "del student Anthony Hopkins"
doTest "given4StudentsInCollectionWhenDeleteOneOfThemThenDeleted" ${POSITIVE_MSG} "del student Eleonore Carter"
doTest "given3SizeCollectionWhenCountThen3" "3" "count students"
doTest "givenEmptyCollectionWhenCountThen0" "0" "count subjects"
doTest "givenEmptyCollectionWhenDeleteSubjectThenNoChanges" ${NEGATIVE_MSG} "del subject Biology"
doTest "givenEmptyCollectionWhenAddSubjectThenAdded" ${POSITIVE_MSG} "add subject Biology"
doTest "given1SubjectsAndNotEmptyStudentsCollectionAndNoGradesWhenAvgThen0" "0.0" "average John Carter Biology"
doTest "given1SubjectInCollectionWhenDeleteDifferentThenNoChanges" ${NEGATIVE_MSG} "del subject Math"
doTest "given1SubjectInCollectionAndNoGradesFromItWhenRemoveThisSubjectThenRemoved" ${POSITIVE_MSG} "del subject Biology"
doTest "givenEmptySubjectCollectionWhenAddRemovedBeforeSubjectThenAdded" ${POSITIVE_MSG} "add subject Biology"
doTest "given1SubjectInCollectionWhenAddTheSameSubjectThenNotAdded" ${NEGATIVE_MSG} "add subject Biology"
doTest "givenNotEmptySubjectCollectionWhenAddDifferentSubjectThenAdded" ${POSITIVE_MSG} "add subject Math"
doTest "given2SizeSubjectsCollectionWhenCountThen2" "2" "count subjects"
doTest "givenCorrectSubjectAndGradeAndIncorrectStudentWhenAddGradeThenNoChanges" ${NEGATIVE_MSG} "set grade Emily Rose Biology 4"
doTest "givenCorrectStudentAndGradeAndIncorrectSubjectWhenAddGradeThenNoChanges" ${NEGATIVE_MSG} "set grade Penelope Cruze PE 4"
doTest "givenCorrectStudentAndSubjectAndGrade1WhenAddGradeThenNoChanges" ${NEGATIVE_MSG} "set grade Penelope Cruze Biology 1"
doTest "givenCorrectStudentAndSubjectAndGrade6WhenAddGradeThenNoChanges" ${NEGATIVE_MSG} "set grade Penelope Cruze Biology 6"
doTest "givenCorrectDataAndStudentWithoutAnyGradesWhenAddGradeThenAdded" ${POSITIVE_MSG} "set grade Penelope Cruze Biology 2"
doTest "given1GradeForGivenSubjectWhenAvgThenThisGrade" "2.0" "average Penelope Cruze Biology"
doTest "given1GradeForOtherSubjectWhenAvgWithDifferentSubjectThen0" "0.0" "average Penelope Cruze Math"
doTest "givenCorrectDataAndStudentWithoutGradesFromGivenSubjectWhenAddGradeThenAdded" ${POSITIVE_MSG} "set grade Penelope Cruze Math 3"
doTest "givenCorrectDataAndStudentWithOneGradeFromGivenSubjectWhenAddGradeThenAdded" ${POSITIVE_MSG} "set grade Penelope Cruze Math 4"
doTest "givenCorrectDataAndStudentWith2DifferentGradesFromGivenSubjectWhenAddDifferentGradeThenAdded" ${POSITIVE_MSG} "set grade Penelope Cruze Math 2"
doTest "givenCorrectDataAndStudentWitt1GradeFromGivenSubjectWhenAddTheSameGradeThenAdded" ${POSITIVE_MSG} "set grade Penelope Cruze Biology 2"
doTest "givenCorrectDataAndStudentWith2TheSameGradesFromGivenSubjectWhenAddTheSameGradeThenAdded" ${POSITIVE_MSG} "set grade Penelope Cruze Biology 2"
doTest "givenCorrectDataAndStudentWith3TheSameGradesFromGivenSubjectWhenAddDifferentGradeThenAdded" ${POSITIVE_MSG} "set grade Penelope Cruze Biology 4"
doTest "given2SubjectsInCollectionAnd1StudentWithGradesFromItWhenRemoveThisSubjectThenRemoved" ${POSITIVE_MSG} "del subject Math"
doTest "given4GradeFor1SubjectWhenAvgThenProperResult" "2.5" "average Penelope Cruze Biology"
other="$(${APP_PATH} add subject Math)" 
other="$(${APP_PATH} set grade Penelope Cruze Biology 4)" 
other="$(${APP_PATH} set grade Penelope Cruze Biology 2)"
doTest "given6GradeFor1SubjectWithRoundedAvgWhenAvgThenProperResult" "2.7" "average Penelope Cruze Biology"
other="$(${APP_PATH} set grade Penelope Cruze Math 3)" 
other="$(${APP_PATH} set grade Penelope Cruze Math 2)"
other="$(${APP_PATH} set grade Penelope Cruze Math 2)"
doTest "given2SubjectWithGradesWhenAvgThenResultOnlyWithGradesFrom1Subject" "2.3" "average Penelope Cruze Math"
other="$(${APP_PATH} set grade John Carter Math 2)"
other="$(${APP_PATH} set grade John Carter Math 3)"
doTest "given2SubjectWithGradesWhenAvgThenResultOnlyWithGradesFrom1Subject" "2.5" "average John Carter Math"
doTest "givenNameContainingDigitsWhenAddSubjectThenNotAdded" ${NEGATIVE_MSG} "add subject Phys1c"
doTest "whenAddSubjectWithNameStartedWithLowercaseThenFalse" ${NEGATIVE_MSG} "add subject chemistry"
doTest "givenGrade1WhenAddGradeThenNotAdded" ${NEGATIVE_MSG} "set grade Penelope Cruze Biology 1"
doTest "givenGrade6WhenAddGradeThenNotAdded" ${NEGATIVE_MSG} "set grade Penelope Cruze Biology 6"
doTest "givenGradeNeg2WhenAddGradeThenNotAdded" ${NEGATIVE_MSG} "set grade Penelope Cruze Biology -2"
doTest "givenGrade0WhenAddGradeThenNotAdded" ${NEGATIVE_MSG} "set grade Penelope Cruze Biology 0"
doTest "givenStudentWithNameContainingDigitsWhenAddThenNotAdded" ${NEGATIVE_MSG} "add student Th0mson Warter"
doTest "givenStudentWithSurnameContainsDigitsWhenAddThenNotAdded" ${NEGATIVE_MSG} "add student Th0mson Warter2"

#############################################################################################################################
# SUMMARY
#############################################################################################################################
printTestsSummary