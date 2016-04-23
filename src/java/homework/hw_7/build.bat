@echo off
javac -cp "%~dp0;%~dp0..\..\tests" %~dp0..\..\tests\expression\generic\*.java
javac -cp "%~dp0;%~dp0..\..\tests" %~dp0expression\generic\*.java