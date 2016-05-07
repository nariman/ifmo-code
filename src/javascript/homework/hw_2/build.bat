@echo off
javac -cp "%~dp0;%~dp0..\..\tests;%~dp0..\..\..\java\tests" %~dp0..\..\tests\test\*.java