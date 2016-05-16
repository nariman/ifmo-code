@echo off
javac -cp "%~dp0;%~dp0..\..\..\..\tests" %~dp0..\..\..\..\tests\test\ExpressionTest.java
javac -cp "%~dp0;%~dp0..\..\..\..\tests" %~dp0..\..\..\..\tests\test\ModifiedExpressionTest.java
echo BUILDING COMPLETED