@echo off
javac -cp "%~dp0;%~dp0..\..\..\..\tests" %~dp0..\..\..\..\tests\test\ObjectExpressionTest.java
javac -cp "%~dp0;%~dp0..\..\..\..\tests" %~dp0..\..\..\..\tests\test\ModifiedObjectExpressionTest.java
echo BUILDING COMPLETED