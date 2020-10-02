@echo off

@rem @for /d %%i in ("\Program Files\Java\jdk*") do set JAVA_HOME=%%i
set MAVEN_OPTS=-Xms64m -Xmx1024m -Xss2048k -Xmn32m

:start
cls
echo WORKING_DIR=%~dp0
echo MAVEN_HOME=%MAVEN_HOME%
echo MAVEN_OPTS=%MAVEN_OPTS%
echo JAVA_HOME=%JAVA_HOME%
echo ------------------
echo u. svn update
echo i. install (no tests)
echo e. eclipse (online)
echo o. eclipse (offline)
echo c. clean
echo d. download dependencies
echo r. release
echo y. update, deploy
echo s. generate sources
echo m. assembly
echo t. status
echo j. update java_home to latest (Windows)
echo a. update maven_home to maven3_home
echo q. quit
echo ------------------
CHOICE /N /C:uieocdrysmtjaq
set nr=%ERRORLEVEL%
IF %nr% EQU 1 goto lsvnup
IF %nr% EQU 2 goto llocinstall
IF %nr% EQU 3 goto leclipse
IF %nr% EQU 4 goto leclipseoff
IF %nr% EQU 5 goto lclean
IF %nr% EQU 6 goto ldependency
IF %nr% EQU 7 goto lrelease
IF %nr% EQU 8 goto ldeploy
IF %nr% EQU 9 goto lgen
IF %nr% EQU 10 goto lass
IF %nr% EQU 11 goto lstatus
IF %nr% EQU 12 goto ljavahome
IF %nr% EQU 13 goto lmavenhome
IF %nr% EQU 14 goto lquit
goto start

:lmavenhome
set MAVEN_HOME=%MAVEN3_HOME%
goto start

:ljavahome
@for /d %%i in ("\Program Files\Java\jdk*") do set JAVA_HOME=%%i
goto start

:lstatus
cls
if exist status.bat goto lstatusbat
svn status .
pause
goto start

:lstatusbat
cls
call status.bat
goto start

:lsvnup
cls
if exist svnup.bat goto lsvnupbat
svn update
pause
goto start

:lsvnupbat
cls
call svnup.bat
goto start

:llocinstall
cls
call mvn -T 1C -Dmaven.tomcat.skip=true -e install -DskipTests=true -DupdateReleaseInfo=true
pause
goto start

:leclipse
cls
call mvn -T 1C -Dmaven.tomcat.skip=true -e -U -DdownloadSources=true -Dwtpversion=2.0 eclipse:eclipse
pause
goto start

:leclipseoff
cls
call mvn -T 1C -Dmaven.tomcat.skip=true -e -U -DdownloadSources=true -Dwtpversion=2.0 eclipse:eclipse -o
pause
goto start

:lclean
cls
call mvn -T 1C -Dmaven.tomcat.skip=true -e clean
pause
goto start

:ldependency
cls
call mvn -T 1C -Dmaven.tomcat.skip=true -e dependency:go-offline
pause
goto start

:lrelease
cls
call mvn -Dmaven.tomcat.skip=true -e release:prepare
rem call mvn -T 1C -Dmaven.tomcat.skip=true -e release:perform
pause
goto start

:ldeploy
cls
svn update
call mvn -T 1C -Dmaven.tomcat.skip=true -e deploy
pause
goto start

:lgen
cls
call mvn -T 1C -Dmaven.tomcat.skip=true -e generate-sources
pause
goto start

:lass
cls
call mvn -T 1C -o -Dmaven.tomcat.skip=true -e package assembly:assembly
pause

:lquit
cls