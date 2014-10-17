@echo off
REM 清理
if exist ..\game\data\stats\skills\10000-10099.xml del ..\game\data\stats\skills\10000-10099.xml
if exist ..\game\data\stats\skills\27000-Add.xml del ..\game\data\stats\skills\27000-Add.xml
if exist ..\game\data\stats\skills\27000-Subclass.xml del ..\game\data\stats\skills\27000-Subclass.xml
if exist ..\game\data\stats\skills\27001-Item.xml del ..\game\data\stats\skills\27001-Item.xml
if exist ..\game\data\stats\skills\27004-Transfor.xml del ..\game\data\stats\skills\27004-Transfor.xml
if exist ..\game\data\stats\skills\27010-Other.xml del ..\game\data\stats\skills\27010-Other.xml
if exist ..\game\data\stats\items\50003-Cloak.xml del ..\game\data\stats\items\50003-Cloak.xml
if exist ..\game\data\stats\items\50004-Other.xml del ..\game\data\stats\items\50004-Other.xml
if exist ..\game\data\stats\npcs\50000-New.xml del ..\game\data\stats\npcs\50000-New.xml
if exist ..\sql\L2JTW_2\item_tw.sql del ..\sql\L2JTW_2\item_tw.sql
if exist ..\sql\L2JTW_2\npc_tw.sql del ..\sql\L2JTW_2\npc_tw.sql
if exist ..\sql\L2JTW_2\skill_tw.sql del ..\sql\L2JTW_2\skill_tw.sql

REM 檢查是否存在 GS 支援的版本資訊
set dp_err=0
if not exist ..\doc\L2J_Server_Ver.txt echo 沒有發現 GS 支援的版本資訊！
if not exist ..\doc\L2J_Server_Ver.txt echo 請再一次：更新 GS → 編譯 GS → 解壓縮 GS → 設定 Config
if not exist ..\doc\L2J_Server_Ver.txt echo.
if not exist ..\doc\L2J_Server_Ver.txt pause
if not exist ..\doc\L2J_Server_Ver.txt goto end
REM 取得 GS 支援的版本資訊
FOR /F %%g IN (..\doc\L2J_Server_Ver.txt) DO set vgs=%%g
REM 檢查 GS 支援的版本資訊
if not %vgs% == Ertheia echo 無法繼續安裝 DP，因為：
if not %vgs% == Ertheia echo GS 支援的版本是：%vgs%
if not %vgs% == Ertheia echo DP 支援的版本是：Ertheia
if not %vgs% == Ertheia echo 請確定 GS 和 DP 都使用相同的版本後，再試一次
if not %vgs% == Ertheia echo.
if not %vgs% == Ertheia pause
if not %vgs% == Ertheia goto end

REM 功能說明：每隔一段時間刪除 libs 和快取，以防止 GS 出錯
if not exist ..\libs\*.jar echo 您必須重新解壓縮「編譯完成」的 GS，才可以繼續安裝資料庫
if not exist ..\libs\*.jar echo.
if not exist ..\libs\*.jar pause
if not exist ..\libs\*.jar exit

REM 如果 libs 快取不存在，表示還沒有啟動過伺服器，則跳過檢查
if not exist ..\game\cachedir\ md ..\game\cachedir\
if not exist ..\game\cachedir\packages\*.pkc goto _lib_update

REM 如果 log 不存在，表示還沒有啟動過伺服器，則跳過檢查
if not exist ..\game\log\*.log goto _lib_update

REM ------------------------------------------------------
REM _lib_check1 的檢查 開始
REM 如果 Windows 的 CMD 版本資訊已存在，則跳到檢查1
if exist ..\game\cachedir\check_w_ver.txt goto _lib_check1

REM 如果 Windows 的 CMD 版本資訊不存在，則建立資訊
ver > ..\game\cachedir\check_w_ver.txt
goto _lib_del

:_lib_check1
REM 取得目前的 Windows CMD 版本資訊
ver > %temp%\check.txt
FOR /F "skip=1 delims=*" %%a IN (%temp%\check.txt) do set aaa=%%a

REM 取得已存在的 Windows CMD 版本資訊
FOR /F "skip=1 delims=*" %%b IN (..\game\cachedir\check_w_ver.txt) do set bbb=%%b

REM 比較 Windows 的 CMD 版本資訊
if "%aaa%"=="%bbb%" goto _start_lib_check2
echo 因為您的 Windows 版本有更新，所以必須刪除舊的 libs 和快取，以防止 GS 出錯
echo.
pause
goto _lib_del
REM _lib_check1 的檢查 結束
REM ------------------------------------------------------

REM ------------------------------------------------------
:_start_lib_check2
REM _lib_check2 的檢查 開始
REM 如果 Java 路徑不存在，則跳到下一個檢查
REM 暫停 _start_lib_check3 這個檢查 if not exist "%ProgramFiles%\Java\jdk1.8.*" goto _start_lib_check3
if not exist "%ProgramFiles%\Java\jdk1.8.*" goto _lib_end

REM 如果 Java 版本資訊已存在，則跳到檢查2
if exist ..\game\cachedir\check_j_ver.txt goto _lib_check2

REM 如果 Java 版本資訊不存在，則建立資訊
dir "%ProgramFiles%\Java\jdk1.8.*" /A:D /B /O > ..\game\cachedir\check_j_ver.txt
goto _lib_del

:_lib_check2
REM 取得目前的 Java 版本資訊
dir "%ProgramFiles%\Java\jdk1.8.*" /A:D /B /O > %temp%\check.txt
FOR /F %%j IN (%temp%\check.txt) DO set jjj=%%j

REM 取得已存在的 Java 版本資訊
FOR /F %%k IN (..\game\cachedir\check_j_ver.txt) do set kkk=%%k

REM 比較 Java 版本資訊
REM 暫停 _start_lib_check3 這個檢查 if "%jjj%"=="%kkk%" goto _start_lib_check3
if "%jjj%"=="%kkk%" goto _lib_end
echo 因為您的 Java 版本有更新，所以必須刪除舊的 libs 和快取，以防止 GS 出錯
echo.
pause
goto _lib_del
REM _lib_check2 的檢查 結束
REM ------------------------------------------------------

REM ------------------------------------------------------
:_start_lib_check3
REM _lib_check3 的檢查 開始
REM 如果 日期-月份 的資訊已存在，則跳到檢查3
if exist ..\game\cachedir\check_d_ver.txt goto _lib_check3

REM 如果 日期-月份 的資訊不存在，則建立資訊
date/t > ..\game\cachedir\check_d_ver.txt
goto _lib_del

:_lib_check3
REM 取得目前的 日期-月份 資訊
date/t > %temp%\check.txt
FOR /F "tokens=2 delims=/" %%d IN (%temp%\check.txt) DO set ddd=%%d

REM 取得已存在的 日期-月份 資訊
FOR /F "tokens=2 delims=/" %%m IN (..\game\cachedir\check_d_ver.txt) do set mmm=%%m

REM 比較 日期-月份 資訊
if "%ddd%"=="%mmm%" goto _lib_end
echo 此為每個月自動清理舊的 libs 和快取，以防止 GS 出錯
echo.
pause
goto _lib_del
REM _lib_check3 的檢查 結束
REM ------------------------------------------------------

REM ------------------------------------------------------
:_lib_del
echo.
if not exist ..\libs\backup\ md ..\libs\backup\
copy ..\libs\*.* ..\libs\backup\ /Y > nul
del ..\libs\*.* /F /Q > nul
del ..\game\cachedir\packages\*.* /F /Q > nul
if exist ..\libs\*.jar echo 無法刪除 libs 和快取！請先關閉伺服器或重新開機，然後再試一次
if exist ..\libs\*.jar echo.
if exist ..\libs\*.jar pause
if exist ..\libs\*.jar exit
if exist ..\game\cachedir\packages\*.pkc echo 無法刪除 libs 和快取！請先關閉伺服器或重新開機，然後再試一次
if exist ..\game\cachedir\packages\*.pkc echo.
if exist ..\game\cachedir\packages\*.pkc pause
if exist ..\game\cachedir\packages\*.pkc exit
ver > ..\game\cachedir\check_w_ver.txt
dir "%ProgramFiles%\Java\jdk1.8.*" /A:D /B /O > ..\game\cachedir\check_j_ver.txt
date/t > ..\game\cachedir\check_d_ver.txt
CLS
echo 舊的 libs 和快取清理完畢！
echo 您必須重新解壓縮「編譯完成」的 GS，才可以繼續安裝資料庫
echo.
pause
exit

:_lib_update
ver > ..\game\cachedir\check_w_ver.txt
dir "%ProgramFiles%\Java\jdk1.8.*" /A:D /B /O > ..\game\cachedir\check_j_ver.txt
date/t > ..\game\cachedir\check_d_ver.txt

:_lib_end
REM ------------------------------------------------------

REM ##############################################
REM ## L2JDP Database Installer - (by DrLecter) ##
REM ##############################################
REM ## Interactive script setup -  (by TanelTM) ##
REM ##############################################
REM Copyright (C) 2004-2013 L2J DataPack
REM
REM This file is part of L2J DataPack.
REM
REM L2J DataPack is free software: you can redistribute it and/or modify
REM it under the terms of the GNU General Public License as published by
REM the Free Software Foundation, either version 3 of the License, or
REM (at your option) any later version.
REM
REM L2J DataPack is distributed in the hope that it will be useful,
REM but WITHOUT ANY WARRANTY; without even the implied warranty of
REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
REM General Public License for more details.
REM
REM You should have received a copy of the GNU General Public License
REM along with this program. If not, see <http://www.gnu.org/licenses/>.

set config_file=vars.txt
set config_version=0

set workdir="%cd%"
set full=0
set stage=0-1
set logging=0

set upgrade_mode=0
set backup=.
set logdir=.
set safe_mode=1
set cmode=c
set fresh_setup=0

:loadconfig
cls
title 安裝 L2JTW DP - 讀取設定檔...（階段 %stage%）
if not exist %config_file% goto configure
ren %config_file% vars.bat
call vars.bat
ren vars.bat %config_file%
call :colors 17
if /i %config_version% == 2 goto ls_backup
set upgrade_mode=2
echo 您似乎是第一次使用這個版本的 database_installer
echo 但是我發現安裝資料庫的設定檔已經存在
echo 因此我將問您幾個問題，引導您繼續安裝
echo.
echo 更新設定選項：
echo.
echo (1) 導入＆繼續使用舊的設定：將使用原本舊的資料並且進行更新作業
echo.
echo (2) 導入＆使用新的設定：導入新的資料並且重新設定資料
echo.
echo (3) 導入全新的資料：所有舊的資料將會移除並且導入新的資料
echo.
echo (4) 查看存取的設定值
echo.
echo (5) 退出
echo.
set /P upgrade_mode="輸入數字後，請按 Enter（預設值為「%upgrade_mode%」）: "
if %upgrade_mode%==1 goto ls_backup
if %upgrade_mode%==2 goto configure
if %upgrade_mode%==3 goto configure
if %upgrade_mode%==4 (cls&type %config_file%&pause&goto loadconfig)
if %upgrade_mode%==5 goto :eof
goto loadconfig

:colors
if /i "%cmode%"=="n" (
if not "%1"=="17" (	color F	) else ( color )
) else ( color %1 )
goto :eof

:configure
cls
call :colors 17
set stage=0-2
title 安裝 L2JTW DP - 安裝（階段 %stage%）
set config_version=2
if NOT %upgrade_mode% == 2 (
set fresh_setup=1
set mysqlBinPath=%ProgramFiles%\MySQL\MySQL Server 5.0\bin

:_MySQL51
if not exist "%ProgramFiles%\MySQL\MySQL Server 5.1\bin\mysql.exe" goto _MySQL55
set mysqlBinPath=%ProgramFiles%\MySQL\MySQL Server 5.1\bin

:_MySQL55
if not exist "%ProgramFiles%\MySQL\MySQL Server 5.5\bin\mysql.exe" goto _MySQL56
set mysqlBinPath=%ProgramFiles%\MySQL\MySQL Server 5.5\bin

:_MySQL56
if not exist "%ProgramFiles%\MySQL\MySQL Server 5.6\bin\mysql.exe" goto _MySQL60
set mysqlBinPath=%ProgramFiles%\MySQL\MySQL Server 5.6\bin

:_MySQL60
if not exist "%ProgramFiles%\MySQL\MySQL Server 6.0\bin\mysql.exe" goto _AppServ
set mysqlBinPath=%ProgramFiles%\MySQL\MySQL Server 6.0\bin

:_AppServ
if not exist "%SystemDrive%\AppServ\MySQL\bin\mysql.exe" goto _other
set mysqlBinPath=%SystemDrive%\AppServ\MySQL\bin

:_other
set lsuser=root
set lspass=
set lsdb=l2jls_ertheia
set lshost=localhost
set cbuser=root
set cbpass=
set cbdb=l2jcs
set cbhost=localhost
set gsuser=root
set gspass=
set gsdb=l2jgs_ertheia
set gshost=localhost
set cmode=c
set backup=.
set logdir=.
)
set mysqlPath=%mysqlBinPath%\mysql.exe
echo 新的設定值：
echo.
echo 1.MySql 程式
echo --------------------
echo 請設定 mysql.exe 和 mysqldump.exe 的位置
echo.
if "%mysqlBinPath%" == "" (
set mysqlBinPath=use path
echo 沒有找到 MySQL 的位置
) else (
echo 請測試以下所找到的 MySQL 位置，是否可以進行導入作業
echo.
echo %mysqlPath%
)
if not "%mysqlBinPath%" == "use path" call :binaryfind
echo.
path|find "MySQL">NUL
if %errorlevel% == 0 (
echo 上面是找到的 MySQL，此位置將會被設為預設值，如果想換位置請修改...
set mysqlBinPath=use path
) else (
echo 無法找到 MySQL，請輸入 mysql.exe 的位置...
echo.
echo 如果不確定這是什麼意思和如何操作，請到相關網站查詢或者至 L2JTW 官方網站發問或尋找相關資訊
)
echo.
echo 請輸入 mysql.exe 的位置：
set /P mysqlBinPath="(default %mysqlBinPath%): "
cls
echo.
echo 2.「登入伺服器」設定
echo --------------------
echo 此作業將會連線至「登入伺服器」的 MySQL 伺服器，並且進行導入作業
echo.
set /P lsuser="使用者名稱（預設值「%lsuser%」）: "
:_lspass
set /P lspass="使用者密碼（預設值「%lspass%」）: "
if "%lspass%"=="" goto _lspass
set /P lsdb="資料庫（預設值「%lsdb%」）: "
set /P lshost="位置（預設值「%lshost%」）: "
echo.
cls
echo.
echo 3-「討論版伺服器」設定
echo --------------------
echo 此作業將會連線至「討論版伺服器」的 MySQL 伺服器，並且進行導入作業
echo.
set /P cbuser="使用者名稱（預設值「%cbuser%」）: "
:_cbpass
set /P cbpass="使用者密碼（預設值「%cbpass%」）: "
if "%cbpass%"=="" goto _cbpass
set /P cbdb="資料庫（預設值「%cbdb%」）: "
set /P cbhost="位置（預設值「%cbhost%」）: "
echo.
cls
echo.
echo 4.「遊戲伺服器」設定
echo --------------------
echo 此作業將會連線至「遊戲伺服器」的 MySQL 伺服器，並且進行導入作業
set /P gsuser="使用者名稱（預設值「%gsuser%」）: "
:_gspass
set /P gspass="使用者密碼（預設值「%gspass%」）: "
if "%gspass%"=="" goto _gspass
set /P gsdb="資料庫（預設值「%gsdb%」）: "
set /P gshost="位置（預設值「%gshost%」）: "
echo.
cls
echo.
echo 5.其他設定
echo --------------------
set /P cmode="顏色模式 (c)為顏色 或 (n)為無顏色（預設值「%cmode%」）: "
set /P backup="備份位置（預設值「%backup%」）: "
set /P logdir="Logs訊息位置（預設值「%logdir%」）: "
:safe1
set safemode=y
set /P safemode="Debug 模式（y/n， 預設值「%safemode%」）: "
if /i %safemode%==y (set safe_mode=1&goto safe2)
if /i %safemode%==n (set safe_mode=0&goto safe2)
goto safe1

:safe2
cls
echo.
if "%mysqlBinPath%" == "use path" (
set mysqlBinPath=
set mysqldumpPath=mysqldump
set mysqlPath=mysql
) else (
set mysqldumpPath=%mysqlBinPath%\mysqldump.exe
set mysqlPath=%mysqlBinPath%\mysql.exe
)
echo @echo off > %config_file%
echo set config_version=%config_version% >> %config_file%
echo set cmode=%cmode%>> %config_file%
echo set safe_mode=%safe_mode% >> %config_file%
echo set mysqlPath=%mysqlPath%>> %config_file%
echo set mysqlBinPath=%mysqlBinPath%>> %config_file%
echo set mysqldumpPath=%mysqldumpPath%>> %config_file%
echo set lsuser=%lsuser%>> %config_file%
echo set lspass=%lspass%>> %config_file%
echo set lsdb=%lsdb%>> %config_file%
echo set lshost=%lshost% >> %config_file%
echo set cbuser=%cbuser%>> %config_file%
echo set cbpass=%cbpass%>> %config_file%
echo set cbdb=%cbdb%>> %config_file%
echo set cbhost=%cbhost% >> %config_file%
echo set gsuser=%gsuser%>> %config_file%
echo set gspass=%gspass%>> %config_file%
echo set gsdb=%gsdb%>> %config_file%
echo set gshost=%gshost%>> %config_file%
echo set logdir=%logdir%>> %config_file%
echo set backup=%backup%>> %config_file%
echo.
echo 設定成功！
echo 你的設定值將會儲存在「%config_file%」，所有的帳號密碼將以明文顯示
echo.
pause
goto loadconfig

:ls_backup
cls
call :colors 17
set cmdline=
set stage=1-1
title 安裝 L2JTW DP - 備份「登入伺服器」的資料庫（階段 %stage%）
echo.
echo 正在備份「登入伺服器」的資料庫...
set cmdline="%mysqldumpPath%" --add-drop-table -h %lshost% -u %lsuser% --password=%lspass% %lsdb% ^> "%backup%\ls_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto ls_db_ok

:ls_err1
cls
set lsdbprompt=y
call :colors 47
set stage=1-2
title 安裝 L2JTW DP - 「登入伺服器」的資料庫備份失敗！（階段 %stage%）
echo.
echo 備份失敗！
echo 原因是因為「登入伺服器」的資料庫不存在
echo 現在可以幫你建立 %lsdb%，或者繼續其它設定
echo.
echo 建立「登入伺服器」的資料庫？
echo.
echo (y)確定
echo.
echo (n)取消
echo.
echo (r)重新設定
echo.
echo (q)退出
echo.
set /p lsdbprompt=請選擇（預設值-確定）:
if /i %lsdbprompt%==y goto ls_db_create
if /i %lsdbprompt%==n goto cs_backup
if /i %lsdbprompt%==r goto configure
if /i %lsdbprompt%==q goto end
goto ls_err1

:ls_db_create
cls
call :colors 17
set cmdline=
set stage=2-1
title 安裝 L2JTW DP - 建立「登入伺服器」的資料庫（階段 %stage%）
echo.
echo 正在建立「登入伺服器」的資料庫...
set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -e "CREATE DATABASE %lsdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto ls_db_ok
if %safe_mode% == 1 goto omfg

:ls_err2
cls
set omfgprompt=q
call :colors 47
set stage=2-2
title 安裝 L2JTW DP - 「登入伺服器」的資料庫建立失敗！（階段 %stage%）
echo.
echo 「登入伺服器」的資料庫建立失敗！
echo.
echo 可能的原因：
echo 1.輸入的資料錯誤，例如：使用者名稱/使用者密碼/其他相關資料
echo 2.使用者「%lsuser%」的權限不足
echo 3.資料庫已存在
echo.
echo 請檢查設定並且修正，或者直接重新設定
echo.
echo (c)繼續
echo.
echo (r)重新設定
echo.
echo (q)退出
echo.
set /p omfgprompt=請選擇（預設值-退出）:
if /i %omfgprompt%==c goto cs_backup
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto end
goto ls_err2

:ls_db_ok
cls
set loginprompt=u
call :colors 17
set stage=2-3
title 安裝 L2JTW DP - 「登入伺服器」的資料庫（階段 %stage%）
echo.
echo 安裝「登入伺服器」的資料庫：
echo.
echo (f) 完整：將移除所有舊的資料，重新導入新的資料
echo.
echo (u) 更新：將保留所有舊的資料，並且進行更新作業
echo.
echo (s) 省略：跳過此選項
echo.
echo (r) 重新設定
echo.
echo (q) 退出
echo.
set /p loginprompt=請選擇（預設值-更新）:
if /i %loginprompt%==f goto ls_cleanup
if /i %loginprompt%==u goto ls_upgrade
if /i %loginprompt%==s goto cs_backup
if /i %loginprompt%==r goto configure
if /i %loginprompt%==q goto end
goto ls_db_ok

:ls_cleanup
call :colors 17
set cmdline=
set stage=2-4
title 安裝 L2JTW DP - 完整安裝「登入伺服器」的資料庫（階段 %stage%）
echo.
echo 正在移除「登入伺服器」的資料庫，然後導入新的資料庫...
set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% ^< ls_cleanup.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
echo.
echo 「登入伺服器」資料庫已被刪除
goto ls_install

:ls_upgrade
cls
echo.
echo 更新「登入伺服器」資料庫結構
echo.
echo @echo off> temp.bat
if exist ls_errors.log del ls_errors.log
for %%i in (..\sql\login\updates\*.sql) do echo "%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% --force ^< %%i 2^>^> ls_errors.log >> temp.bat
call temp.bat> nul
del temp.bat
move ls_errors.log %workdir%
goto ls_install

:ls_install
cls
set cmdline=
if %full% == 1 (
set stage=2-5
title 安裝 L2JTW DP - 安裝「登入伺服器」的資料庫...（階段 %stage%）
echo.
echo 安裝新的「登入伺服器」的資料庫內容
echo.
) else (
title 安裝 L2JTW DP - 更新「登入伺服器」的資料庫...（階段 %stage%）
echo.
echo 更新「登入伺服器」的資料庫內容
echo.
)
if %logging% == 0 set output=NUL
set dest=ls
for %%i in (..\sql\login\*.sql) do call :dump %%i

echo 完成...
echo.
goto cs_backup

:cs_backup
cls
call :colors 17
set cmdline=
set stage=3-1
title 安裝 L2JTW DP - 備份「討論版伺服器」的資料庫（階段 %stage%）
echo.
echo 正在備份「討論版伺服器」的資料庫...
set cmdline="%mysqldumpPath%" --add-drop-table -h %cbhost% -u %cbuser% --password=%cbpass% %cbdb% ^> "%backup%\cs_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto cs_db_ok

:cs_err1
cls
set cbdbprompt=y
call :colors 47
set stage=3-2
title 安裝 L2JTW DP - 「討論版伺服器」的資料庫備份失敗！（階段 %stage%）
echo.
echo 備份失敗！
echo 原因是因為「討論版伺服器」的資料庫不存在
echo 現在可以幫你建立 %cbdb%，或者繼續其它設定
echo.
echo 建立「討論版伺服器」的資料庫？
echo.
echo (y)確定
echo.
echo (n)取消
echo.
echo (r)重新設定
echo.
echo (q)退出
echo.
set /p cbdbprompt=請選擇（預設值-確定）:
if /i %cbdbprompt%==y goto cs_db_create
if /i %cbdbprompt%==n goto gs_backup
if /i %cbdbprompt%==r goto configure
if /i %cbdbprompt%==q goto end
goto cs_err1

:cs_db_create
cls
call :colors 17
set cmdline=
set stage=4-1
title 安裝 L2JTW DP - 建立「討論版伺服器」的資料庫（階段 %stage%）
echo.
echo 正在建立「討論版伺服器」的資料庫...
set cmdline="%mysqlPath%" -h %cbhost% -u %cbuser% --password=%cbpass% -e "CREATE DATABASE %cbdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto cs_db_ok
if %safe_mode% == 1 goto omfg

:cs_err2
cls
set omfgprompt=q
call :colors 47
set stage=4-2
title 安裝 L2JTW DP - 「討論版伺服器」的資料庫建立失敗！（階段 %stage%）
echo.
echo 「討論版伺服器」的資料庫建立失敗！
echo.
echo 可能的原因：
echo 1.輸入的資料錯誤，例如：使用者名稱/使用者密碼/其他相關資料
echo 2.使用者「%cbuser%」的權限不足
echo 3.資料庫已存在
echo.
echo 請檢查設定並且修正，或者直接重新設定
echo.
echo (c)繼續
echo.
echo (r)重新設定
echo.
echo (q)退出
echo.
set /p omfgprompt=請選擇（預設值-退出）:
if /i %omfgprompt%==c goto gs_backup
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto end
goto cs_err2

:cs_db_ok
cls
set communityprompt=u
call :colors 17
set stage=4-3
title 安裝 L2JTW DP - 「討論版伺服器」的資料庫（階段 %stage%）
echo.
echo 安裝「討論版伺服器」的資料庫：
echo.
echo (f)完整：將移除所有舊的資料，重新導入新的資料
echo.
echo (u)更新：將保留所有舊的資料，並且進行更新作業
echo.
echo (s)省略：跳過此選項
echo.
echo (r)重新設定
echo.
echo (q)退出
echo.
set /p communityprompt=請選擇（預設值-更新）:
if /i %communityprompt%==f goto cs_cleanup
if /i %communityprompt%==u goto cs_upgrade
if /i %communityprompt%==s goto gs_backup
if /i %communityprompt%==r goto configure
if /i %communityprompt%==q goto end
goto cs_db_ok

:cs_cleanup
call :colors 17
set cmdline=
set stage=4-4
title 安裝 L2JTW DP - 完整安裝「討論版伺服器」的資料庫（階段 %stage%）
echo.
echo 正在移除「討論版伺服器」的資料庫，然後導入新的資料庫...
set cmdline="%mysqlPath%" -h %cbhost% -u %cbuser% --password=%cbpass% -D %cbdb% ^< cs_cleanup.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
echo.
echo 「討論版伺服器」的資料庫已被刪除
goto cs_install

:cs_upgrade
cls
echo.
echo 更新「討論版伺服器」的資料庫結構
echo.
echo @echo off> temp.bat
if exist cs_errors.log del cs_errors.log
for %%i in (..\sql\community\updates\*.sql) do echo "%mysqlPath%" -h %cbhost% -u %cbuser% --password=%cbpass% -D %cbdb% --force ^< %%i 2^>^> cs_errors.log >> temp.bat
call temp.bat> nul
del temp.bat
move cs_errors.log %workdir%
goto cs_install

:cs_install
cls
set cmdline=
if %full% == 1 (
set stage=4-5
title 安裝 L2JTW DP - 安裝「討論伺服器」的資料庫...（階段 %stage%）
echo.
echo 安裝新的「討論版伺服器」的資料庫內容...
echo.
) else (
title 安裝 L2JTW DP - 更新「討論伺服器」的資料庫..（階段 %stage%）
echo.
echo 更新「討論版伺服器」的資料庫內容...
echo.
)
if %logging% == 0 set output=NUL
set dest=cb
for %%i in (..\sql\community\*.sql) do call :dump %%i

echo done...
echo.
goto gs_backup

:gs_backup
cls
call :colors 17
set cmdline=
set stage=5-1
title 安裝 L2JTW DP - 備份「遊戲伺服器」的資料庫（階段 %stage%）
echo.
echo 正在備份「遊戲伺服器」的資料庫...
set cmdline="%mysqldumpPath%" --add-drop-table -h %gshost% -u %gsuser% --password=%gspass% %gsdb% ^> "%backup%\gs_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto gs_db_ok

:gs_err1
cls
set gsdbprompt=y
call :colors 47
set stage=5-2
title 安裝 L2JTW DP - 「遊戲伺服器」的資料庫備份失敗！（階段 %stage%）
echo.
echo 備份失敗！
echo 原因是因為「遊戲伺服器」的資料庫不存在
echo 現在可以幫你建立 %gsdb%，或者繼續其它設定
echo.
echo 建立「遊戲伺服器」的資料庫？
echo.
echo (y)確定
echo.
echo (n)取消
echo.
echo (r)重新設定
echo.
echo (q)退出
echo.
set /p gsdbprompt=請選擇（預設值-確定）:
if /i %gsdbprompt%==y goto gs_db_create
if /i %gsdbprompt%==n goto eof
if /i %gsdbprompt%==r goto configure
if /i %gsdbprompt%==q goto end
goto gs_err1

:gs_db_create
cls
call :colors 17
set stage=6-1
set cmdline=
title 安裝 L2JTW DP - 建立「遊戲伺服器」的資料（階段 %stage%）
echo.
echo 正在建立「遊戲伺服器」的資料庫...
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -e "CREATE DATABASE %gsdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto gs_db_ok
if %safe_mode% == 1 goto omfg

:gs_err2
cls
set omfgprompt=q
call :colors 47
set stage=6-2
title 安裝 L2JTW DP - 「遊戲伺服器」的資料庫建立失敗！（階段 %stage%）
echo.
echo 「遊戲伺服器」的資料庫建立失敗！
echo.
echo 可能的原因：
echo 1.輸入的資料錯誤，例如：使用者名稱/使用者密碼/其他相關資料
echo 2.使用者「%gsuser%」的權限不足
echo 3.資料庫已存在
echo.
echo 請檢查設定並且修正，或者直接重新設定
echo.
echo (r)重新設定
echo.
echo (q)退出
echo.
set /p omfgprompt=請選擇（預設值-退出）:
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto end
goto gs_err2

:gs_db_ok
cls
set installtype=u
call :colors 17
set stage=6-3
title 安裝 L2JTW DP - 「遊戲伺服器」的資料庫（階段 %stage%）
echo.
echo 安裝「遊戲伺服器」的資料庫：
echo.
echo (f)完整：將移除所有舊的資料，重新導入新的資料
echo.
echo (u)更新：將保留所有舊的資料，並且進行更新作業
echo.
echo (s)省略：跳過此選項
echo.
echo (q)退出
echo.
set /p installtype=請選擇（預設值-更新）:
if /i %installtype%==f goto gs_cleanup
if /i %installtype%==u goto gs_upgrade
if /i %installtype%==s goto custom_ask
if /i %installtype%==q goto end
goto gs_db_ok

:gs_cleanup
call :colors 17
set cmdline=
set stage=6-4
title 安裝 L2JTW DP - 完整安裝「遊戲伺服器」的資料庫（階段 %stage%）
echo.
echo 正在移除「遊戲伺服器」的資料庫，然後導入新的資料庫...
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< gs_cleanup.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
echo.
echo 「遊戲伺服器」的資料庫已被刪除
goto gs_install

:gs_upgrade
cls
echo.
echo 更新「遊戲伺服器」的資料庫結構
echo.
echo @echo off> temp.bat
if exist gs_errors.log del gs_errors.log
for %%i in (..\sql\game\updates\*.sql) do echo "%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% --force ^< %%i 2^>^> gs_errors.log >> temp.bat
call temp.bat> nul
del temp.bat
move gs_errors.log %workdir%
goto gs_install

:gs_install
cls
set cmdline=
if %full% == 1 (
set stage=6-5
title 安裝 L2JTW DP - 安裝「遊戲伺服器」的資料庫...（階段 %stage%）
echo.
echo 安裝新的「遊戲伺服器」的資料庫內容
echo.
) else (
title 安裝 L2JTW DP - 更新「遊戲伺服器」的資料庫...（階段 %stage%）
echo.
echo 更新「遊戲伺服器」的資料庫內容
echo.
)
if %logging% == 0 set output=NUL
set dest=gs
for %%i in (..\sql\game\*.sql) do call :dump %%i
for %%i in (..\sql\game\mods\*.sql) do call :dump %%i
for %%i in (..\sql\game\custom\*.sql) do call :dump %%i
for %%i in (..\sql\L2JTW\*.sql) do call :dump %%i

echo 完成...
echo.
set charprompt=y
set /p charprompt=安裝「NPC/物品/技能等名稱」中文化: (y) 確定 或 (N) 取消？（預設值-確定）:
if /i %charprompt%==n goto custom_ask
for %%i in (..\sql\L2JTW_2\*.sql) do call :dump %%i
echo 完成...
echo.
echo ☆注意：部分系統安裝中文化會失敗，導致遊戲中出現亂碼
echo 　　　　如果遇到這種情形，請再手動導入 SQL 裡面的
echo 　　　　skill_tw / item_tw / messagetable 這 3 個 SQL
goto custom_ask

:dump
set cmdline=
if /i %full% == 1 (set action=安裝) else (set action=更新)
echo %action% %1>>"%output%"
echo %action% %~nx1
if "%dest%"=="ls" set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% ^< %1 2^>^>"%output%"
if "%dest%"=="cb" set cmdline="%mysqlPath%" -h %cbhost% -u %cbuser% --password=%cbpass% -D %cbdb% ^< %1 2^>^>"%output%"
if "%dest%"=="gs" set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< %1 2^>^>"%output%"
%cmdline%
if %logging%==0 if NOT %ERRORLEVEL%==0 call :omfg2 %1
goto :eof

:omfg2
REM ------------------------------------------------------
REM 資料庫安裝過程中發生錯誤
set dp_err=2
echo 資料庫安裝過程中發生錯誤：Ertheia> ..\doc\L2J_DataPack_Ver.txt
REM ------------------------------------------------------
cls
set ntpebcak=c
call :colors 47
title 安裝 L2JTW DP - 階段 %stage% 發生錯誤
echo.
echo 出現錯誤：
echo %mysqlPath% -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb%
echo.
echo 檔案 %~nx1
echo.
echo 處理方式？
echo.
echo (l)儲存錯誤訊息，以方便查詢
echo.
echo (c)繼續
echo.
echo (r)重新設定
echo.
echo (q)退出
echo.
set /p ntpebcak=請選擇（預設值-繼續）:
if /i %ntpebcak%==c (call :colors 17 & goto :eof)
if /i %ntpebcak%==l (call :logginon %1 & goto :eof)
if /i %ntpebcak%==r set dp_err=0
if /i %ntpebcak%==r (call :configure & exit)
if /i %ntpebcak%==q (call :end)
goto omfg2

:logginon
cls
call :colors 17
title 安裝 L2JTW DP - 儲存錯誤訊息
set logging=1
if %full% == 1 (
  set output=%logdir%\install-%~nx1.log
) else (
  set output=%logdir%\upgrade-%~nx1.log
)
echo.
echo 準備儲存錯誤訊息
echo.
echo 檔案為「%output%」
echo.
echo 如果此檔案已存在，請進行備份，否則將會覆蓋過去
echo.
pause
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^<..\sql\%1 2^>^>"%output%"
date /t >"%output%"
time /t >>"%output%"
%cmdline%
echo 儲存錯誤訊息...
call :colors 17
set logging=0
set output=NUL
goto :eof

:custom_ask
set stage=7
title 安裝 L2JTW DP - custom 自訂資料表（階段 %stage%）
cls
set cstprompt=y
echo.
echo custom 自訂資料表加入資料庫完成
echo 所有錯誤訊息將儲存在「custom_errors.log」
echo.
echo 請注意，如果要使這些自訂資料表能夠啟用
echo 你必須修改 config 的檔案設定
echo.
set /p cstprompt=安裝 custom 自訂資料表: (y) 確定 或 (N) 取消（預設值-確定）:
if /i %cstprompt%==y goto custom_install
if /i %cstprompt%==n goto mod_ask

:custom_install
cls
echo.
echo 安裝 custom 自訂內容
echo @echo off> temp.bat
if exist custom_errors.log del custom_errors.log
for %%i in (..\sql\game\custom\*.sql) do echo "%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< %%i 2^>^> custom_errors.log >> temp.bat
call temp.bat> nul
del temp.bat
move custom_errors.log %workdir%
goto mod_ask

:mod_ask
set stage=8
title 安裝 L2JTW DP - Mod 自訂資料表（階段 %stage%）
cls
set cstprompt=y
echo.
echo Mod 自訂資料表加入資料庫完成
echo 所有錯誤資訊將放入「mod_errors.log」
echo.
echo 請注意，如果要使這些自訂資料表能夠啟用
echo 你必須修改 config 的檔案設定
echo.
echo.
set /p cstprompt=安裝 Mods 自訂資料表: (y) 確定 或 (N) 取消（預設值-確定）:
if /i %cstprompt%==y goto mod_install
if /i %cstprompt%==n goto end

:mod_install
cls
echo.
echo 安裝 Mods 自訂內容
echo @echo off> temp.bat
if exist mods_errors.log del mods_errors.log
for %%i in (..\sql\game\mods\*.sql) do echo "%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< %%i 2^>^> mods_errors.log >> temp.bat
call temp.bat> nul
del temp.bat
move mods_errors.log %workdir%
REM ------------------------------------------------------
REM 資料庫安裝完成
if %dp_err% == 0 set dp_err=1
REM ------------------------------------------------------
goto end

:omfg
REM ------------------------------------------------------
REM 資料庫安裝過程中發生錯誤
set dp_err=2
echo 資料庫安裝過程中發生錯誤：Ertheia> ..\doc\L2J_DataPack_Ver.txt
REM ------------------------------------------------------
set omfgprompt=q
call :colors 57
cls
title 安裝 L2JTW DP - 階段 %stage% 發生錯誤
echo.
echo 執行時出現錯誤：
echo.
echo "%cmdline%"
echo.
echo 建議檢查一下設定的資料，以確保所有輸入的數值沒有錯誤！
echo.
if %stage% == 1 set label=ls_err1
if %stage% == 2 set label=ls_err2
if %stage% == 3 set label=cs_err1
if %stage% == 4 set label=cs_err2
if %stage% == 5 set label=gs_err1
if %stage% == 6 set label=gs_err2
echo.
echo (c)繼續
echo.
echo (r)重新設定
echo.
echo (q)退出
echo.
set /p omfgprompt=請選擇（預設值-退出）:
if /i %omfgprompt%==c goto %label%
if /i %omfgprompt%==r set dp_err=0
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto end
goto omfg

:binaryfind
if EXIST "%mysqlBinPath%" (echo 找到的 MySQL) else (echo 沒有找到 MySQL，請在下面輸入正確的位置...)
goto :eof

:end
REM ------------------------------------------------------
REM 儲存 DP 支援的版本資訊
if %dp_err% == 0 echo 資料庫安裝未完成：Ertheia> ..\doc\L2J_DataPack_Ver.txt
if %dp_err% == 1 echo Ertheia> ..\doc\L2J_DataPack_Ver.txt
REM ------------------------------------------------------
call :colors 17
title 安裝 L2JTW DP - 完成
cls
echo.
echo L2JTW DP 安裝完畢
echo.
echo 感謝使用 L2JTW 伺服器
echo 相關資訊可以在 http://www.l2jtw.com 查詢到
echo.
pause