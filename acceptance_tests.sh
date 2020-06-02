#!/bin/bash
APP_PATH=${1}
PASSED_TESTS_QTY=0
FAILED_TESTS_QTY=0
PASS_MSG="OK"
FAILED_MSG="ERROR"

###############################################################################################################################
# API
###############################################################################################################################
retVal=""

doTest(){
    name=${1}
    expected=${2}
    Cmd="${APP_PATH} ${@:3}"
    actual="$($cmd)"
    actual_trimmed="$(echo -e "${actual}" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
    
    checkTestResult ${name} ${expected} ${actual_trimmed}
    printTestInfo ${name} ${expected} ${actual_trimmed} ${cmd}
}

checkTestResult(){
    name=${1}
    excepted=${2}
    actual=${3}
    if [ "${expected}" = "${actual}" ]; then
        retVal="\e[1;47m\e[1;30m PASSED \e[0m"
        ((PASSED_TESTS_QTY++))
    else
        retVal="\e[1;41m FAILED \e[0m"
        ((FAILED_TESTS_QTY++))
    fi
}

printTestInfo(){
    echo "________________________________________________________________________________________________________"
    echo "Name: ${1}"
    echo "cmd: ${@:4}"
    echo "Expected: ${2}"
    echo "Actual: ${3}"
    echo -e "Result: ${retVal}"
    echo "________________________________________________________________________________________________________"
}

printTestsSummary(){
    echo "#-------------------------------------------------------------------------------------------------------#"
    allTestsQty=$((FAILED_TESTS_QTY+PASSED_TESTS_QTY))
    passedPercents=$(echo "scale=2; ${PASSED_TESTS_QTY}/${allTestsQty}*100" | bc)
    echo "DONE TESTS QTY: ${allTestsQty}"
    echo "FAILED TESTS QTY: ${FAILED_TESTS_QTY}"
    echo "PASSED TESTS QTY: ${PASSED_TESTS_QTY}"
    echo "PASSED TESTS: ${passedPercents} %"
    echo "#-------------------------------------------------------------------------------------------------------#"
}

#############################################################################################################################
# TESTS
#############################################################################################################################
doTest "givenEmptyCollectionWhenAddCorrectStudentThenAdded" ${PASS_MSG} "add student John Carter"
doTest "given1StudentInCollectionWhenAddTheSameStudentThenNotAdded" ${FAILED_MSG} "add student John Carter"
doTest "givenNotEmptyCollectionWhenAddStudentWithOnlyTheSameNameThenAdded" ${PASS_MSG} "add student John Dicaprio"
doTest "givenNotEmptyCollectionWhenAddStudentWithTheOnlySameSurnameThenAdded" ${PASS_MSG} "add student Eleonore Carter"
doTest "givenNotEmptyCollectionWhenAddDifferentStudentThenAdded" ${PASS_MSG} "add student Penelope Cruze"

printTestsSummary