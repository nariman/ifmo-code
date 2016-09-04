@echo off
javac -cp "%~dp0;%~dp0..\..\..\..\tests" %~dp0..\..\..\..\tests\expression\*.java
javac -cp "%~dp0;%~dp0..\..\..\..\tests" %~dp0*.java