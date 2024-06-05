@echo off
REM Purgatorio
SET "DIR=%~dp0"

REM Construir o caminho para o JavaFX SDK
SET "MODULE_PATH=%DIR%biblioteca\javafx-sdk-21.0.2\lib"

REM Executar o programa Java
java --module-path "%MODULE_PATH%" --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.graphics -jar "%DIR%biblioteca\programa.jar"
