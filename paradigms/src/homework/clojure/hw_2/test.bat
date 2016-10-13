@echo off
for %%m in (%*) do (
    if %%m == base-easy (
        echo.
        echo ===============================================================================
        echo ============================== TESTING BASE EASY ==============================
        echo ===============================================================================
        echo.
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests;%~dp0..\..\..\..\bin\clojure-1.8.0.jar" test.ClojureObjectExpressionTest easy
        echo.
        echo TESTING BASE EASY COMPLETED
        echo.
    )

    if %%m == base-hard (
        echo.
        echo ===============================================================================
        echo ============================== TESTING BASE HARD ==============================
        echo ===============================================================================
        echo.
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests;%~dp0..\..\..\..\bin\clojure-1.8.0.jar" test.ClojureObjectExpressionTest hard
        echo.
        echo TESTING BASE HARD COMPLETED
        echo.
    )

    if %%m == mod-easy (
        echo.
        echo ==============================================================================
        echo ============================== TESTING MOD EASY ==============================
        echo ==============================================================================
        echo.
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests;%~dp0..\..\..\..\bin\clojure-1.8.0.jar" test.ModifiedClojureObjectExpressionTest easy
        echo.
        echo TESTING MOD EASY COMPLETED
        echo.
    )

    if %%m == mod-hard (
        echo.
        echo ==============================================================================
        echo ============================== TESTING MOD HARD ==============================
        echo ==============================================================================
        echo.
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests;%~dp0..\..\..\..\bin\clojure-1.8.0.jar" test.ModifiedClojureObjectExpressionTest hard
        echo.
        echo TESTING MOD HARD COMPLETED
        echo.
    )
)