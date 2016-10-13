@echo off
javac -cp "%~dp0;%~dp0..\..\..\..\tests" %~dp0..\..\..\..\tests\test\PrefixParserTest.java
javac -cp "%~dp0;%~dp0..\..\..\..\tests" %~dp0..\..\..\..\tests\test\ModifiedPrefixParserTest.java
javac -cp "%~dp0;%~dp0..\..\..\..\tests" %~dp0..\..\..\..\tests\test\PostfixParserTest.java
echo BUILDING COMPLETED