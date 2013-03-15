#!/bin/sh

BASEPATH=/Workspace/EnonicGIT/tools/ui-testing/

PROJECT=auto-tests-1.0-SNAPSHOT
CLASSPATH="$BASEPATH"target/"$PROJECT".jar;"$BASEPATH"target/"$PROJECT"-tests.jar
java org.testng.TestNG template.xml
