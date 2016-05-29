@echo off
javac -cp "%~dp0;%~dp0..\..\..\..\tests;%~dp0..\..\..\..\bin\clojure-1.8.0.jar" %~dp0..\..\..\..\tests\test\ClojureObjectExpressionTest.java
echo BUILDING COMPLETED