@echo off
for %%m in (%*) do (
    if %%m == base java -ea -cp "%~dp0;%~dp0..\..\tests" queue.ArrayQueueTest
    if %%m == easy java -ea -cp "%~dp0;%~dp0..\..\tests" queue.ArrayQueueToArrayTest
    if %%m == hard java -ea -cp "%~dp0;%~dp0..\..\tests" queue.ArrayQueueDequeTest
)