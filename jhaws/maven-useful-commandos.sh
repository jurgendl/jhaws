#!/bin/bash

PROJECTNAME=`mvn -q -DforceStdout help:evaluate -Dexpression=project.name`
PROJECTVERSION=`mvn -q -DforceStdout help:evaluate -Dexpression=project.version`
echo $PROJECTNAME:$PROJECTVERSION

read -n 1 -s -r -p "Press any key to continue..." 

mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec

read -n 1 -s -r -p "Press any key to continue..." 

mvn help:system

read -n 1 -s -r -p "Press any key to continue..." 

mvn help:effective-settings

read -n 1 -s -r -p "Press any key to continue..." 

mvn -Dverbose=false help:effective-pom -Doutput=effective-pom.xml

read -n 1 -s -r -p "Press any key to continue..." 

mvn dependency:list

read -n 1 -s -r -p "Press any key to continue..." 

mvn dependency:tree

read -n 1 -s -r -p "Press any key to continue..." 

mvn dependency:tree -Dincludes=com.fasterxml.jackson.datatype

read -n 1 -s -r -p "Press any key to continue..." 

mvn dependency:resolve-plugins

read -n 1 -s -r -p "Press any key to continue..." 

: '
mvn versions:set -DnewVersion=1.0.0-RELEASE
'

