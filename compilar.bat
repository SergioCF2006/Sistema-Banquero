@echo off
title Compilador - Algoritmo del Banquero
echo ========================================================
echo   COMPILANDO PROYECTO CON JAVA 17
echo ========================================================

if not exist "target\classes" mkdir "target\classes"

echo [1/3] Compilando codigo fuente con codificacion UTF-8...
javac -encoding UTF-8 -sourcepath src/main/java -d target/classes src/main/java/com/banquero/Main.java src/main/java/com/banquero/model/*.java src/main/java/com/banquero/algorithm/*.java src/main/java/com/banquero/ui/*.java

if %ERRORLEVEL% neq 0 (
    echo [ERROR] La compilacion fallo.
    pause
    exit /b %ERRORLEVEL%
)

echo [2/3] Generando archivo ejecutable SistemaBanquero.jar...
if exist "C:\Program Files\Java\jdk-17.0.1\bin\jar.exe" (
    "C:\Program Files\Java\jdk-17.0.1\bin\jar.exe" cfe SistemaBanquero.jar com.banquero.Main -C target/classes .
) else (
    jar cfe SistemaBanquero.jar com.banquero.Main -C target/classes .
)

echo [3/3] Compilacion completada con exito.
echo.
echo Puede ejecutar la aplicacion ejecutando 'ejecutar.bat' o con:
echo java -jar SistemaBanquero.jar
echo.
pause
