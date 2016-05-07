@echo off
for %%m in (%*) do (
    if %%m == base java -cp "%~dp0;%~dp0..\..\tests" search.BinarySearchTest
    if %%m == easy java -cp "%~dp0;%~dp0..\..\tests" search.BinarySearchMissingTest
    if %%m == hard java -cp "%~dp0;%~dp0..\..\tests" search.BinarySearchSpanTest
)