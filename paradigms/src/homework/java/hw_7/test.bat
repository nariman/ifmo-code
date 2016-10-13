@echo off
for %%m in (%*) do (
    if %%m == base java -ea -cp "%~dp0;%~dp0..\..\..\..\tests" expression.generic.GenericTest
    if %%m == easy java -ea -cp "%~dp0;%~dp0..\..\..\..\tests" expression.generic.GenericEasyTest
    if %%m == hard java -ea -cp "%~dp0;%~dp0..\..\..\..\tests" expression.generic.GenericHardTest
)