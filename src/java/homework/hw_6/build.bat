@echo off
javac -cp "%~dp0;%~dp0..\..\tests" %~dp0..\..\tests\expression\exceptions\*.java
javac -cp "%~dp0;%~dp0..\..\tests" %~dp0expression\exceptions\*.java