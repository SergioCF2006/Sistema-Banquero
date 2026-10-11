@echo off
title Simulador del Algoritmo del Banquero - Java 17
echo ========================================================
echo   SIMULADOR DEL ALGORITMO DEL BANQUERO (JAVA 17)
echo   Sistemas Operativos - Prevencion de Deadlocks
echo ========================================================
echo Iniciando aplicacion...

if exist "SistemaBanquero.jar" (
    java -jar SistemaBanquero.jar
) else (
    echo No se encontro SistemaBanquero.jar. Compilando primero...
    call compilar.bat
    java -jar SistemaBanquero.jar
)

pause
