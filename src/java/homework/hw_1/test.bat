@echo off
for %%m in (%*) do (
    if %%m == base java -cp "%~dp0;%~dp0..\..\tests" hash.CalcMD5Test
    if %%m == easy java -cp "%~dp0;%~dp0..\..\tests" hash.CalcSHA256Test
    if %%m == hard java -cp "%~dp0;%~dp0..\..\tests" hash.SHA256SumTest
)