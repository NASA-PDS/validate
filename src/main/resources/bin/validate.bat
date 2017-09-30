:: Copyright 2010-2014, by the California Institute of Technology.
:: ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
:: Any commercial use must be negotiated with the Office of Technology Transfer
:: at the California Institute of Technology.
::
:: This software is subject to U. S. export control laws and regulations
:: (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
:: is subject to U.S. export control laws and regulations, the recipient has
:: the responsibility to obtain export licenses or other export authority as
:: may be required before exporting such information to foreign countries or
:: providing access to foreign nationals.
::
:: $Id$

:: Batch file that allows easy execution of the Validate Tool
:: without the need to set the CLASSPATH or having to type in that long java
:: command (java gov.nasa.pds.validate.ValidateLauncher ...)

:: Expects the Validate Tool jar file to be located in the ../lib directory.

@echo off

:: Check if the JAVA_HOME environment variable is set.
if not defined JAVA_HOME (
echo The JAVA_HOME environment variable is not set.
goto END
)

:: Setup environment variables.
set SCRIPT_DIR=%~dps0
set PARENT_DIR=%SCRIPT_DIR%..
set LIB_DIR=%PARENT_DIR%\lib

:: Check for dependencies.
if exist "%LIB_DIR%\validate-*.jar" (
set VALIDATE_JAR=%LIB_DIR%\validate-*.jar
) else (
echo Cannot find VTool jar file in %LIB_DIR%
goto END
)

:: Finds the jar file in LIB_DIR and sets it to VALIDATE_JAR
for %%i in ("%LIB_DIR%"\validate-*.jar) do set VALIDATE_JAR=%%i

:: Executes the Validate Tool via the executable jar file
:: The special variable '%*' allows the arguments
:: to be passed into the executable.
"%JAVA_HOME%"\bin\java -Xms2048m -Xmx4096m -jar "%VALIDATE_JAR%" %*

:END
