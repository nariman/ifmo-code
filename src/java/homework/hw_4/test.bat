@echo off
for %%m in (%*) do (
    if %%m == base java -ea -cp "%~dp0;%~dp0..\..\tests" queue.QueueTest
    if %%m == easy java -ea -cp "%~dp0;%~dp0..\..\tests" queue.QueueToArrayTest
    if %%m == hard java -ea -cp "%~dp0;%~dp0..\..\tests" queue.QueueFunctionsTest
)