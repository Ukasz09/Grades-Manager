#!/bin/bash
PASSED_TESTS_QTY=0
FAILED_TESTS_QTY=0

#########################################################################################################
# API
#########################################################################################################
retVal=""

doTest(){
    name=${1}
    expected=${2}
    cmd="./app.sh ${@:3}"
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
        retVal="PASSED"
        ((PASSED_TESTS_QTY++))
    else
        retVal="FAILED"
        ((FAILED_TESTS_QTY++))
    fi
}

printTestInfo(){
    echo "_______________________________________"
    echo "Name: ${1}"
    echo "cmd: ${@:4}"
    echo "Expected: ${2}"
    echo "Actual: ${3}"
    echo "Result: ${retVal}"
    echo "_______________________________________"
}

printTestsSummary(){
    echo "#-----------------------------------------------#"
    allTestsQty=$((FAILED_TESTS_QTY+PASSED_TESTS_QTY))
    passedPercents=$((PASSED_TESTS_QTY/allTestsQty*100))
    echo "DONE TESTS QTY: ${allTestsQty}"
    echo "FAILED TESTS QTY: ${FAILED_TESTS_QTY}"
    echo "PASSED TESTS QTY: ${PASSED_TESTS_QTY}"
    echo "PASSED TESTS: ${passedPercents} %"
    echo "#-----------------------------------------------#"
}

#########################################################################################################
# TESTS
#########################################################################################################
givenEmptyCollectionWhenAddThenAdded() {
    name="givenEmptyCollectionWhenAddThenAdded"
    expected="OK"
    cmd="add student John Carter"
    doTest $name $expected $cmd
}

givenEmptyCollectionWhenAddThenAdded
printTestsSummary