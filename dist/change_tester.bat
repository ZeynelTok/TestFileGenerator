@ECHO OFF
SETLOCAL
SET "filename1=%CD%\users.xml"
SET "outfile=%CD%\temp.xml"
set newuser=%USERNAME%
setlocal DisableDelayedExpansion
SET "SEARCH2=TesterUN"
SET "REPLACE2=%newuser%"

for /F "delims=" %%a in (%filename1%) DO (
	set line=%%a
	setlocal EnableDelayedExpansion
	SET "line=!line:%search2%=%replace2%!"
	echo !line!
	endlocal
)>> %outfile%
MOVE /Y temp.xml users.xml
exit 0