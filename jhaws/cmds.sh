#!/bin/bash

#M2_HOME="C:/java/programs/apache-maven-3.3.3"
#MAVEN_HOME="C:/java/programs/apache-maven-3.3.3"
#MAVEN_OPTS="-Xms1024m -Xmx6000m"
#JAVA_HOME="C:/Program Files/Java/jdk1.7.0_80"
MAVEN_DEFAULT_OPTIONS="-e -Dwtpversion=2.0 -DskipTests=true -Dmaven.tomcat.skip=true -P skipWar"
MAVEN_EXTRA_OPTIONS=$MAVEN_DEFAULT_OPTIONS

while true; do
	printf "\033c"
	echo WORKING_DIR=$(PWD)
	echo MAVEN_HOME=$MAVEN_HOME
	echo MAVEN_OPTS=$MAVEN_OPTS
	echo JAVA_HOME=$JAVA_HOME
	echo MAVEN_EXTRA_OPTIONS=$MAVEN_EXTRA_OPTIONS
	echo "------------------"
	echo "x. input"
	echo "u. svn: update"
	echo "t. svn: status"
	echo "i. maven: install (no tests)"
	echo "e. maven: eclipse (online)"
	echo "o. maven: eclipse (offline)"
	echo "c. maven: clean"
	echo "d. maven: download dependencies"
	echo "r. maven: release"
	echo "y. maven: update, deploy"
	echo "s. maven: generate sources"
	echo "m. maven: assembly"
	echo "p. maven: compile"
	echo "f. maven: info"
	echo "g. git: update"
	echo "h. git: hide changes pom.xml"
	echo "j. git: unhide changes pom.xml"
	echo "k. git: list hidden files"
	echo "q. quit"
	echo "------------------"
    read -p "? " nr
    case $nr in
		[xX]* ) 
			printf "\033c"
			echo ====================== ====================== ======================
			echo MAVEN_DEFAULT_OPTIONS=$MAVEN_EXTRA_OPTIONS
			echo current extra commandline parameters=$EXTRACMD
			read -p "input extra commandline parameters (-T 2)(-T 1C): " EXTRACMD
			set MAVEN_EXTRA_OPTIONS=$MAVEN_EXTRA_OPTIONS $EXTRACMD
			echo ====================== ====================== ======================
			read -p "Press 'any' key to continue..."
			;;
        [uU]* ) 
			printf "\033c"
			echo ====================== ====================== ======================
			svn update . --accept postpone
			echo ====================== ====================== ======================
			read -p "Press 'any' key to continue..."
			;;
		[tT]* ) 
			printf "\033c"
			echo ====================== ====================== ======================
			svn status
			echo ====================== ====================== ======================
			read -p "Press 'any' key to continue..."
			;;
        [iI]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS compile generate-sources install -DupdateReleaseInfo=true ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS compile generate-sources generate-resources install -DupdateReleaseInfo=true 
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS compile generate-sources install -DupdateReleaseInfo=true ======================
			read -p "Press 'any' key to continue..."
			;;
		[eE]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS -DdownloadSources=true eclipse:eclipse ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS -DdownloadSources=true eclipse:eclipse
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS -DdownloadSources=true eclipse:eclipse ======================
			read -p "Press 'any' key to continue..."
			;;
		[oO]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS -DdownloadSources=true eclipse:eclipse ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS -DdownloadSources=true eclipse:eclipse
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS -DdownloadSources=true eclipse:eclipse ======================
			read -p "Press 'any' key to continue..."
			;;
		[cC]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS clean ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS clean
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS clean ======================
			read -p "Press 'any' key to continue..."
			;;
		[dD]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS dependency:go-offline ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS dependency:go-offline
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS dependency:go-offline ======================
			read -p "Press 'any' key to continue..."
			;;
		[rR]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS release:prepare release:perform ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS release:prepare
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS release:perform
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS release:prepare release:perform ======================
			read -p "Press 'any' key to continue..."
			;;
		[yY]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS deploy ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS deploy
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS deploy ======================
			read -p "Press 'any' key to continue..."
			;;
		[sS]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS generate-sources generate-resources ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS generate-sources generate-resources
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS generate-sources generate-resources ======================
			read -p "Press 'any' key to continue..."
			;;
		[mM]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS package assembly:assembly ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS package assembly:assembly
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS package assembly:assembly ======================
			read -p "Press 'any' key to continue..."
			;;
		[pP]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS compile ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS compile
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS compile ======================
			read -p "Press 'any' key to continue..."
			;;
		[fF]* ) 
			printf "\033c"
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS help:all-profiles help:active-profiles ======================
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS help:all-profiles
			bash $MAVEN_HOME/bin/mvn $MAVEN_EXTRA_OPTIONS help:active-profiles
			echo ====================== mvn $MAVEN_EXTRA_OPTIONS help:all-profiles help:active-profiles ======================
			read -p "Press 'any' key to continue..."
			;;
        [gG]* ) 
			printf "\033c"
			echo ====================== ====================== ======================
			bash echo not implemented
			echo ====================== ====================== ======================
			read -p "Press 'any' key to continue..."
			;;
		[hH]* ) 
			printf "\033c"
			echo ====================== ====================== ======================
			git update-index --assume-unchanged pom.xml
			echo ====================== ====================== ======================
			read -p "Press 'any' key to continue..."
			;;
		[jJ]* ) 
			printf "\033c"
			echo ====================== ====================== ======================
			git update-index --no-assume-unchanged pom.xml
			echo ====================== ====================== ======================
			read -p "Press 'any' key to continue..."
			;;
		[kK]* ) 
			printf "\033c"
			echo ====================== ====================== ======================
			# http://stackoverflow.com/questions/17195861/undo-git-update-index-assume-unchanged-file
			git ls-files -v|grep '^h'|cut -c3-
			echo ====================== ====================== ======================
			read -p "Press 'any' key to continue..."
			;;
		* )
			break
			;;
    esac
done

