@echo off
for /R %~dp0 %%f in (*.class) do (
    del %%f
)