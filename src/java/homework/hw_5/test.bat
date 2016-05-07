@echo off
for %%m in (%*) do (
    if %%m == base java -ea -cp "%~dp0;%~dp0..\..\tests" expression.ExpressionTest
    if %%m == easy java -ea -cp "%~dp0;%~dp0..\..\tests" expression.DoubleExpressionTest
    if %%m == hard java -ea -cp "%~dp0;%~dp0..\..\tests" expression.TripleExpressionTest
)