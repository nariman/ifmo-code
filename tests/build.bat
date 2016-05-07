for /D /r %~dp0 %%d in (.) do (
    javac -cp %~dp0 %%d\*.java
)