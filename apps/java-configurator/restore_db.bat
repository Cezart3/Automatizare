@echo off
setlocal enabledelayedexpansion

echo ===============================================
echo  Restaurare baza de date Shower Configurator
echo ===============================================
echo.

title Restaurare Baza de Date Shower Configurator

:: Obține calea către directorul curent
set "CURRENT_DIR=%~dp0"
echo Director curent: %CURRENT_DIR%
echo.

echo 1. Caut serviciul MySQL...
set "MYSQL_SERVICE="
set "SERVICE_FOUND=0"

for /f "tokens=1*" %%i in ('sc query type^= service state^= all ^| find /i "mysql"') do (
    if "%%i"=="SERVICE_NAME:" (
        set "MYSQL_SERVICE=%%j"
        set "SERVICE_FOUND=1"
        echo Serviciu MySQL gasit: !MYSQL_SERVICE!
        goto :check_running
    )
)

if !SERVICE_FOUND!==0 (
    echo EROARE: Niciun serviciu MySQL nu a fost gasit.
    echo.
    echo Verificati in Services.msc daca serviciul MySQL exista.
    echo.
    echo Daca MySQL nu este instalat, descarcati-l de la:
    echo https://dev.mysql.com/downloads/mysql/
    echo.
    pause
    exit /b 1
)

:check_running
echo Verific daca serviciul !MYSQL_SERVICE! ruleaza...
sc query "!MYSQL_SERVICE!" | find "RUNNING" >nul
if errorlevel 1 (
    echo Serviciul MySQL nu ruleaza. Incerc sa il pornesc...
    net start "!MYSQL_SERVICE!"
    if errorlevel 1 (
        echo EROARE: Nu pot porni serviciul MySQL.
        echo.
        echo Verificati in Services.msc daca serviciul MySQL este setat pe automat.
        echo.
        pause
        exit /b 1
    )
    echo Serviciul MySQL a fost pornit cu succes.
) else (
    echo Serviciul MySQL ruleaza deja.
)

echo.
echo 2. Caut utilitarul mysql.exe...
set "MYSQL_PATH="

:: Cauta mysql.exe in locatiile comune
for %%d in (
    "C:\Program Files\MySQL\MySQL Server 8.0\bin"
    "C:\Program Files\MySQL\MySQL Server 5.7\bin"
    "C:\Program Files\MySQL\MySQL Server 9.4.0\bin"
    "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin"
    "C:\Program Files (x86)\MySQL\MySQL Server 5.7\bin"
     "C:\Program Files (x86)\MySQL\MySQL Server 9.4.0\bin"
    "C:\MySQL\bin"
) do (
    if exist "%%d\mysql.exe" (
        set "MYSQL_PATH=%%d"
        echo Utilitar mysql.exe gasit in: !MYSQL_PATH!
        goto :check_file
    )
)

:: Verifica daca mysql este in PATH
mysql --version >nul 2>&1
if not errorlevel 1 (
    set "MYSQL_PATH=mysql"
    echo Utilitar mysql.exe gasit in PATH.
    goto :check_file
)

if "!MYSQL_PATH!"=="" (
    echo EROARE: Utilitarul mysql.exe nu a fost gasit.
    echo.
    echo SOLUTII:
    echo 1. Adaugati manual folderul bin MySQL la PATH
    echo 2. Folositi MySQL Workbench pentru a importa fisierul SQL
    echo 3. Reinstalati MySQL si bifati optiunea "Add to PATH"
    echo.
    echo Cautati manual mysql.exe in:
    echo C:\Program Files\MySQL\
    echo.
    pause
    exit /b 1
)

:check_file
echo.
echo 3. Verific daca fisierul mydb_backup.sql exista...
if not exist "%CURRENT_DIR%mydb_backup.sql" (
    echo EROARE: Fisierul mydb_backup.sql nu a fost gasit!
    echo Cautat in: %CURRENT_DIR%mydb_backup.sql
    echo.
    echo Asigurati-va ca toate fisierele sunt in acelasi folder.
    echo.
    pause
    exit /b 1
)

echo Fisierul mydb_backup.sql a fost gasit.
echo.
echo 4. Restaurez baza de date din mydb_backup.sql...
echo Asteptati, aceasta operatie poate dura cateva minute...

:: Încearcă mai întâi cu parola din .env
set "MYSQL_PASSWORD=FelineFulger2004!"
echo Folosesc parola configurata din .env...

:: Foloseste calea completa daca este cale de folder, altfel foloseste direct comanda mysql
if "!MYSQL_PATH!"=="mysql" (
    mysql -u root -p%MYSQL_PASSWORD% < "%CURRENT_DIR%mydb_backup.sql"
) else (
    cd /d "!MYSQL_PATH!"
    mysql -u root -p%MYSQL_PASSWORD% < "%CURRENT_DIR%mydb_backup.sql"
)

if errorlevel 1 (
    echo.
    echo Eroare la restaurarea bazei de date cu parola din .env
    echo Verificati parola in fisierul .env
    echo.
    echo Incercare alternativa - va rugam introduceti parola manual...
    
    if "!MYSQL_PATH!"=="mysql" (
        mysql -u root -p < "%CURRENT_DIR%mydb_backup.sql"
    ) else (
        cd /d "!MYSQL_PATH!"
        mysql -u root -p < "%CURRENT_DIR%mydb_backup.sql"
    )
    
    if errorlevel 1 (
        echo.
        echo Restaurare esuata. Verificati urmatoarele:
        echo.
        echo CAUZE POSIBILE:
        echo   1. Parola MySQL este gresita
        echo   2. Nu aveti drepturi de administrator
        echo   3. Baza de date mydb exista deja si are conflicte
        echo   4. Fisierul SQL este corupt
        echo.
        echo SOLUTII:
        echo   1. Verificati parola in fisierul .env
        echo   2. Importati manual in MySQL Workbench
        echo.
        echo INSTRUCTIUNI IMPORT MANUAL:
        echo   1. Deschideti MySQL Workbench
        echo   2. Conectati-va la server-ul local
        echo   3. Click dreapta in sectiunea Schemas
        echo   4. Selectati "Create Schema" si numiti-l "mydb"
        echo   5. Click dreapta pe schema mydb si selectati "Table Data Import Wizard"
        echo   6. Selectati fisierul mydb_backup.sql
        echo   7. Urmati pasii wizard-ului
        echo.
        echo Contactati suportul pentru asistenta.
        echo.
        pause
        exit /b 1
    )
)

echo.
echo ===============================================
echo Baza de date a fost restaurata cu succes!
echo ===============================================
echo.
echo Detalii baza de date:
echo    Nume: mydb
echo    User: root
echo    Parola: FelineFulger2004! (din fisierul .env)
echo    Host: localhost:3306
echo.
echo Urmatorii pasi:
echo    1. Verificati fisierul .env sa contina parola corecta
echo    2. Dublu-click pe ShowerConfigurator.exe
echo.
echo Instalarea a fost finalizata cu succes!
echo.
pause