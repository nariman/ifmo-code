@echo off
for %%m in (%*) do (
    if %%m == base java -ea -cp "%~dp0;%~dp0..\..\tests" expression.exceptions.ExceptionsTest
    if %%m == easy java -ea -cp "%~dp0;%~dp0..\..\tests" expression.exceptions.ExceptionsEasyTest
    if %%m == hard java -ea -cp "%~dp0;%~dp0..\..\tests" expression.exceptions.ExceptionsHardTest
)