@echo off

SET configfile=variables.properties

For /F "tokens=1* delims==" %%A IN (%~dp0%configfile%) DO (
    IF "%%A"=="project" SET project=%%B
	IF "%%A"=="cluster" SET cluster=%%B
	IF "%%A"=="zone" SET zone=%%B
)