#!/bin/sh

BASEPATH=/Workspace/EnonicGIT/tools/ui-testing

PROJECT=ui-testing-1.0
CLASSPATH="$BASEPATH/target/$PROJECT-jar-with-dependencies.jar:$BASEPATH/target/$PROJECT-tests.jar"
java org.testng.TestNG template-remote.xml
