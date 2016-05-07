@echo off
javac -cp "%~dp0;%~dp0..\..\tests" %~dp0..\..\tests\search\*.java
javac -cp "%~dp0;%~dp0..\..\tests" %~dp0*.java