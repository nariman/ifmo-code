@echo off
for %%m in (%*) do (
    if %%m == base-easy (
        echo.
        echo ===============================================================================
        echo ============================== TESTING BASE EASY ==============================
        echo ===============================================================================
        echo.
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests" test.PrefixParserTest easy
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
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests" test.PrefixParserTest hard
        echo.
        echo TESTING BASE HARD COMPLETED
        echo.
    )

    if %%m == mod-one-easy (
        echo.
        echo ================================================================================
        echo ============================== TESTING MOD 1 EASY ==============================
        echo ================================================================================
        echo.
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests" test.ModifiedPrefixParserTest easy
        echo.
        echo TESTING MOD 1 EASY COMPLETED
        echo.
    )

    if %%m == mod-one-hard (
        echo.
        echo ================================================================================
        echo ============================== TESTING MOD 1 HARD ==============================
        echo ================================================================================
        echo.
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests" test.ModifiedPrefixParserTest hard
        echo.
        echo TESTING MOD 1 HARD COMPLETED
        echo.
    )

    if %%m == mod-two-easy (
        echo.
        echo ================================================================================
        echo ============================== TESTING MOD 2 EASY ==============================
        echo ================================================================================
        echo.
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests" test.PostfixParserTest easy
        echo.
        echo TESTING MOD 2 EASY COMPLETED
        echo.
    )

    if %%m == mod-two-hard (
        echo.
        echo ================================================================================
        echo ============================== TESTING MOD 2 HARD ==============================
        echo ================================================================================
        echo.
        java -ea -cp "%~dp0;%~dp0..\..\..\..\tests" test.PostfixParserTest hard
        echo.
        echo TESTING MOD 2 HARD COMPLETED
        echo.
    )
)
