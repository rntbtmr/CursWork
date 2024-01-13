@echo off
if "%~1"=="" goto :exit
if %~1==copy (
    xcopy "%~dp0src\main\resources\" "target\classes\" /q /s
    goto :exit
)
if %~1==run goto :runnable
if %~1==build (
    call mvn clean
    call mvn package
)
:runnable
call mvn exec:java "-Dexec.mainClass=plannerApp.Launcher"
:exit