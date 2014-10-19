@echo off
REM クリーンアップ
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

REM GSバージョンサポート情報を確認してください
set dp_err=0
if not exist ..\doc\L2J_Server_Ver.txt echo GSは、バージョン情報のサポートを見つけられませんでした！
if not exist ..\doc\L2J_Server_Ver.txt echo もう一度してください：コンフィグGS →コンパイル→ GS解凍GS →セットを更新
if not exist ..\doc\L2J_Server_Ver.txt echo.
if not exist ..\doc\L2J_Server_Ver.txt pause
if not exist ..\doc\L2J_Server_Ver.txt goto end
REM GSのバージョン情報支援を得る
FOR /F %%g IN (..\doc\L2J_Server_Ver.txt) DO set vgs=%%g
REM GSチェックバージョン情報をサポート
if not %vgs% == Ertheia echo インストールを続行することができませんDP 、理由：
if not %vgs% == Ertheia echo サポートGSのバージョンは次のとおりです。：%vgs%
if not %vgs% == Ertheia echo DP DP版がサポートされています：Ertheia
if not %vgs% == Ertheia echo GSとDPは同じバージョンを使用していることを確認し、再度実行してください
if not %vgs% == Ertheia echo.
if not %vgs% == Ertheia pause
if not %vgs% == Ertheia goto end

REM 機能：定期的にエラーGSを防止するためのライブラリやキャッシュを削除
if not exist ..\libs\*.jar echo これらはインストールデータベースを続行するには、事前に、 GSの「コンパイルされた」 - 解凍を再度しなければならない
if not exist ..\libs\*.jar echo.
if not exist ..\libs\*.jar pause
if not exist ..\libs\*.jar exit

REM ライブラリキャッシュが存在しない場合は、サーバが起動していないと述べ、その後チェックをスキップ
if not exist ..\game\cachedir\ md ..\game\cachedir\
if not exist ..\game\cachedir\packages\*.pkc goto _lib_update

REM ログが存在しない場合は、サーバが起動していないと述べ、その後チェックをスキップ
if not exist ..\game\log\*.log goto _lib_update

REM ------------------------------------------------------
REM _lib_check1チェックが始まる
REM WindowsのCMD情報のバージョンがすでに存在する場合は、チェックをスキップ
if exist ..\game\cachedir\check_w_ver.txt goto _lib_check1

REM WindowsのCMD情報のバージョンが存在しない場合は、情報の確立
ver > ..\game\cachedir\check_w_ver.txt
goto _lib_del

:_lib_check1
REM WindowsのCMDニュースの現在のバージョンを取得する
ver > %temp%\check.txt
FOR /F "skip=1 delims=*" %%a IN (%temp%\check.txt) do set aaa=%%a

REM WindowsのCMDのバージョン情報がすでに存在し得る
FOR /F "skip=1 delims=*" %%b IN (..\game\cachedir\check_w_ver.txt) do set bbb=%%b

REM WindowsのCMDニュースのバージョンを比較
if "%aaa%"=="%bbb%" goto _start_lib_check2
echo Windowsのバージョンが更新されているので、そのように古いライブラリやエラーのGSを防止するためのキャッシュを削除する必要があります
echo.
pause
goto _lib_del
REM _lib_check1チェック終了
REM ------------------------------------------------------

REM ------------------------------------------------------
:_start_lib_check2
REM _lib_check2チェックが始まる
REM Javaパスが存在しない場合は、次の検査にスキップ
REM 存在しない場合は、このチェックを_start_lib_check3一時停止 "%ProgramFiles%\Java\jdk1.8.*" goto _start_lib_check3
if not exist "%ProgramFiles%\Java\jdk1.8.*" goto _lib_end

REM Javaのバージョン情報が既に存在する場合、二つのチェックスキップ
if exist ..\game\cachedir\check_j_ver.txt goto _lib_check2

REM Javaバージョン情報は、情報の設定が存在しない場合
dir "%ProgramFiles%\Java\jdk1.8.*" /A:D /B /O > ..\game\cachedir\check_j_ver.txt
goto _lib_del

:_lib_check2
REM Javaのニュースの現在のバージョンを取得する
dir "%ProgramFiles%\Java\jdk1.8.*" /A:D /B /O > %temp%\check.txt
FOR /F %%j IN (%temp%\check.txt) DO set jjj=%%j

REM 情報がすでに存在しているJavaのバージョンを取得
FOR /F %%k IN (..\game\cachedir\check_j_ver.txt) do set kkk=%%k

REM Javaのバージョン情報を比較
REM タイムアウト _start_lib_check3 このチェック if "%jjj%"=="%kkk%" goto _start_lib_check3
if "%jjj%"=="%kkk%" goto _lib_end
echo お使いのJavaのバージョンが更新されるので、あなたはエラーGSを防ぐために、古いライブラリとキャッシュを削除する必要があります
echo.
pause
goto _lib_del
REM _lib_check2 終了を確認してください
REM ------------------------------------------------------

REM ------------------------------------------------------
:_start_lib_check3
REM _lib_check3チェックが始まる
REM 日付あれば - すでに存在している情報の月は、3のチェックスキップ
if exist ..\game\cachedir\check_d_ver.txt goto _lib_check3

REM 月の情報が存在しない、情報の設定 - 日付があれば
date/t > ..\game\cachedir\check_d_ver.txt
goto _lib_del

:_lib_check3
REM 現在の日付を取得する - 月のニュース
date/t > %temp%\check.txt
FOR /F "tokens=2 delims=/" %%d IN (%temp%\check.txt) DO set ddd=%%d

REM 今月のニュース - すでに存在している日付を取得する
FOR /F "tokens=2 delims=/" %%m IN (..\game\cachedir\check_d_ver.txt) do set mmm=%%m

REM 日付の比較 - 月のニュース
if "%ddd%"=="%mmm%" goto _lib_end
echo これは、月、古いライブラリやエラーのGSを防止するために、キャッシュの自動クリーンアップされます。
echo.
pause
goto _lib_del
REM _lib_check3 チェック終了
REM ------------------------------------------------------

REM ------------------------------------------------------
:_lib_del
echo.
if not exist ..\libs\backup\ md ..\libs\backup\
copy ..\libs\*.* ..\libs\backup\ /Y > nul
del ..\libs\*.* /F /Q > nul
del ..\game\cachedir\packages\*.* /F /Q > nul
if exist ..\libs\*.jar echo あなたは、ライブラリとキャッシュを削除することはできません！サーバーの電源をオフにしたり再起動し、再度実行してください
if exist ..\libs\*.jar echo.
if exist ..\libs\*.jar pause
if exist ..\libs\*.jar exit
if exist ..\game\cachedir\packages\*.pkc echo あなたは、ライブラリとキャッシュを削除することはできません！サーバーの電源をオフにしたり再起動し、再度実行してください
if exist ..\game\cachedir\packages\*.pkc echo.
if exist ..\game\cachedir\packages\*.pkc pause
if exist ..\game\cachedir\packages\*.pkc exit
ver > ..\game\cachedir\check_w_ver.txt
dir "%ProgramFiles%\Java\jdk1.8.*" /A:D /B /O > ..\game\cachedir\check_j_ver.txt
date/t > ..\game\cachedir\check_d_ver.txt
CLS
echo 古いライブラリとキャッシュはクリア！
echo これらはインストールデータベースを続行するには、事前に、GSの「コンパイルされた」 - 解凍を再度しなければならない
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
title インストールL2JTW DP - プロファイルを見る...（ステージ %stage%）
if not exist %config_file% goto configure
ren %config_file% vars.bat
call vars.bat
ren vars.bat %config_file%
call :colors 17
if /i %config_version% == 2 goto ls_backup
set upgrade_mode=2
echo あなたは、このバージョンを使用する最初のように見える database_installer
echo しかし、私は、インストールプロファイルデータベースがすでに存在しているが見つかりました
echo だから私はあなたにいくつか質問をして、インストールを続行してご案内致します
echo.
echo アップデート設定オプション：
echo.
echo (1) インポート&古い設定を継続して使用する：元のデータの使用と古い仕事を更新
echo.
echo (2) インポート＆新しい設定：新しいデータと再設定されたデータをインポートします
echo.
echo (3) 新しいデータをインポート：すべての古いデータは削除され、新しいデータをインポートします
echo.
echo (4) 表示アクセス設定値
echo.
echo (5) 退出
echo.
set /P upgrade_mode="番号を入力した後、[Enter]キーを押します（デフォルトは「%upgrade_mode%」）: "
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
title インストール L2JTW DP - インストール（ステージ %stage%）
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
echo 新しいセットポイント：
echo.
echo 1.MySql プログラム
echo --------------------
echo 設定しmysql.exeとmysqldump.exeの場所
echo.
if "%mysqlBinPath%" == "" (
set mysqlBinPath=use path
echo MySQLのの場所を見つけられませんでした
) else (
echo ジョブをインポートすることができるかどうか、テストの場所を下記のMySQLください
echo.
echo %mysqlPath%
)
if not "%mysqlBinPath%" == "use path" call :binaryfind
echo.
path|find "MySQL">NUL
if %errorlevel% == 0 (
echo 上記のMySQLで発見され、この位置では、場所を変更したい場合は変更してください、デフォルト値に設定されます...
set mysqlBinPath=use path
) else (
echo MySQLを見つけることができない、mysql.exe位置を入力してください...
echo.
echo これが何を意味するかと操作方法がわからない場合は、質問をしたり、関連する情報を見つけるために公式サイトをL2JTWためにウェブサイトまたは関連にアクセスしてください
)
echo.
echo mysql.exe場所を入力してください：
set /P mysqlBinPath="(default %mysqlBinPath%): "
cls
echo.
echo 2.「ログインサーバ」の設定
echo --------------------
echo このジョブは、MySQLサーバおよびインポート操作の「ログインサーバ」に接続します
echo.
set /P lsuser="ユーザー名（デフォルト「%lsuser%」）: "
:_lspass
set /P lspass="ユーザパスワード（デフォルト「%lspass%」）: "
if "%lspass%"=="" goto _lspass
set /P lsdb="データベース（デフォルト「%lsdb%」）: "
set /P lshost="ロケーション（デフォルト「%lshost%」）: "
echo.
cls
echo.
echo 3-「ディスカッションボードサーバー」の設定
echo --------------------
echo このジョブは、「ディスカッションボードサーバ」、およびインポート操作のMySQLサーバーに接続します
echo.
set /P cbuser="ユーザー名（デフォルト「%cbuser%」）: "
:_cbpass
set /P cbpass="ユーザパスワード（デフォルト「%cbpass%」）: "
if "%cbpass%"=="" goto _cbpass
set /P cbdb="データベース（デフォルト「%cbdb%」）: "
set /P cbhost="ロケーション（デフォルト「%cbhost%」）: "
echo.
cls
echo.
echo 4.「ゲームサーバ」の設定
echo --------------------
echo このジョブは、MySQLサーバ「ゲームサーバ」に接続し、インポート操作になる
set /P gsuser="ユーザー名（デフォルト「%gsuser%」）: "
:_gspass
set /P gspass="ユーザパスワード（デフォルト「%gspass%」）: "
if "%gspass%"=="" goto _gspass
set /P gsdb="データベース（デフォルト「%gsdb%」）: "
set /P gshost="ロケーション（デフォルト「%gshost%」）: "
echo.
cls
echo.
echo 5.その他の設定
echo --------------------
set /P cmode="カラーモードカラー（c）色がない（n）（デフォルト「%cmode%」）: "
set /P backup="バックアップの場所（デフォルト「%backup%」）: "
set /P logdir="ログメッセージの場所（デフォルト「%logdir%」）: "
:safe1
set safemode=y
set /P safemode="デバッグモード（Y / N、デフォルト「%safemode%」）: "
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
echo あなたの設定は「%config_file%」に保存されます，クリアテキスト表示でのすべてのアカウントのパスワード
echo.
pause
goto loadconfig

:ls_backup
cls
call :colors 17
set cmdline=
set stage=1-1
title インストールL2JTW DP - バックアップ「ログインサーバ」データベース（ステージ %stage%）
echo.
echo 「ログインサーバ」のデータベースをバックアップ...
set cmdline="%mysqldumpPath%" --add-drop-table -h %lshost% -u %lsuser% --password=%lspass% %lsdb% ^> "%backup%\ls_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto ls_db_ok

:ls_err1
cls
set lsdbprompt=y
call :colors 47
set stage=1-2
title インストールL2JTW DP - 「ログインサーバ」データベースのバックアップが失敗しました！（ステージ %stage%）
echo.
echo 失敗したバックアップ！
echo 「ログインサーバ」データベースが存在しないためである
echo 構築を支援することができます %lsdb%，または他の設定を継続
echo.
echo 「ログインサーバ」のデータベースを確立する？
echo.
echo (y)確定
echo.
echo (n)取消
echo.
echo (r)リセット
echo.
echo (q)退出
echo.
set /p lsdbprompt=（デフォルト - OK）選択してください：
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
title インストールL2JTW DP - 確立「ログインサーバ」データベース（ステージ %stage%）
echo.
echo 「ログインサーバ」のデータベースを確立...
set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -e "CREATE DATABASE %lsdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto ls_db_ok
if %safe_mode% == 1 goto omfg

:ls_err2
cls
set omfgprompt=q
call :colors 47
set stage=2-2
title インストールL2JTW DP - 「ログインサーバ」データベースの作成が失敗しました！（ステージ %stage%）
echo.
echo 「ログインサーバ」のデータベースの作成に失敗しました！
echo.
echo 考えられる原因：
echo 1.ユーザー名/ユーザーパスワード/その他の関連情報：などのデータ入力エラー、
echo 2.ユーザー「%lsuser%」不十分なユーザ権限
echo 3.データベースがすでに存在しています
echo.
echo 設定や修正を確認してください、または直接再設定
echo.
echo (c)継続
echo.
echo (r)リセット
echo.
echo (q)退出
echo.
set /p omfgprompt=（ - 出口デフォルト）を選択:
if /i %omfgprompt%==c goto cs_backup
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto end
goto ls_err2

:ls_db_ok
cls
set loginprompt=u
call :colors 17
set stage=2-3
title インストールL2JTW DP - 「ログインサーバ」データベース（ステージ %stage%）
echo.
echo 「ログインサーバ」のデータベースをインストールします：
echo.
echo (f) 完全：すべての古いデータと再インポート新しいデータが削除されます
echo.
echo (u) アップデート：すべての古いデータを保持し、更新操作になる
echo.
echo (s) 省略：このオプションをスキップ
echo.
echo (r) リセット
echo.
echo (q) 退出
echo.
set /p loginprompt=（ - 更新デフォルト）を選択してください:
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
title インストールL2JTW DP - フルインストール"ログオンサーバー"データベース（ステージ %stage%）
echo.
echo 「ログインサーバ」のデータベースを削除して、新しいデータベースをインポート...
set cmdline="%mysqlPath%" -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% ^< ls_cleanup.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
echo.
echo 「ログインサーバ」データベースが削除されました
goto ls_install

:ls_upgrade
cls
echo.
echo アップデート「ログインサーバ」データベース構造
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
title インストールL2JTW DPは - "ログオンサーバー"データベースをインストールする...（ステージ %stage%）
echo.
echo 新しい「ログインサーバ」データベースの内容をインストールします。
echo.
) else (
title インストールL2JTW DP - アップデート「ログインサーバ」データベース...（ステージ %stage%）
echo.
echo アップデート「ログインサーバ」データベースの内容
echo.
)
if %logging% == 0 set output=NUL
set dest=ls
for %%i in (..\sql\login\*.sql) do call :dump %%i

echo 実行
echo.
goto cs_backup

:cs_backup
cls
call :colors 17
set cmdline=
set stage=3-1
title インストールL2JTW DP - バックアップ」ディスカッションボードサーバー"データベース（ステージ %stage%）
echo.
echo 「ディスカッションボードサーバー」のデータベースをバックアップ...
set cmdline="%mysqldumpPath%" --add-drop-table -h %cbhost% -u %cbuser% --password=%cbpass% %cbdb% ^> "%backup%\cs_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto cs_db_ok

:cs_err1
cls
set cbdbprompt=y
call :colors 47
set stage=3-2
title インストールL2JTW DP - "ディスカッションボードサーバー「データベースのバックアップが失敗しました！（ステージ %stage%）
echo.
echo 失敗したバックアップ！
echo 「ディスカッションボードサーバー"データベースが存在しないためである
echo これで、構築を支援することができます %cbdb%，または他の設定を継続
echo.
echo 「議論掲示板サーバ」データベースを作成します？
echo.
echo (y)確定
echo.
echo (n)取消
echo.
echo (r)リセット
echo.
echo (q)退出
echo.
set /p cbdbprompt=（ - OKデフォルト）を選択してください:
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
title インストールL2JTW DP - 」討論掲示板サーバ」データベースを作成する（ステージ %stage%）
echo.
echo 「ディスカッションボードサーバー」のデータベースを確立している...
set cmdline="%mysqlPath%" -h %cbhost% -u %cbuser% --password=%cbpass% -e "CREATE DATABASE %cbdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto cs_db_ok
if %safe_mode% == 1 goto omfg

:cs_err2
cls
set omfgprompt=q
call :colors 47
set stage=4-2
title インストールL2JTW DP - "ディスカッションボードサーバー「データベースの作成が失敗しました！（ステージ段 %stage%）
echo.
echo 「ディスカッションボードServerは、「データベースの作成に失敗しました！
echo.
echo 考えられる原因：
echo 1.ユーザー名/ユーザーパスワード/その他の関連情報：などのデータ入力エラー、
echo 2.ユーザー「%cbuser%」権威の欠如
echo 3.データベースがすでに存在しています
echo.
echo 設定や修正を確認してください、または直接再設定
echo.
echo (c)続けていく
echo.
echo (r)リセット
echo.
echo (q)退出
echo.
set /p omfgprompt=選択してください（デフォルト - 出口）：
if /i %omfgprompt%==c goto gs_backup
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto end
goto cs_err2

:cs_db_ok
cls
set communityprompt=u
call :colors 17
set stage=4-3
title インストール L2JTW DP -「ディスカッションボードサーバー」のデータベース（ステージ %stage%）
echo.
echo 「議論掲示板サーバ」のデータベースをインストールします。
echo.
echo (f)完全：すべての古いデータと再インポート新しいデータが削除されます
echo.
echo (u)アップデート：すべての古いデータを保持し、更新操作になる
echo.
echo (s)省略：このオプションをスキップ
echo.
echo (r)リセット
echo.
echo (q)退出
echo.
set /p communityprompt=選択してください（デフォルト - アップデート）:
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
title インストール L2JTW DP -完全インストール"ディスカッションボードサーバー"データベース（ステージ %stage%）
echo.
echo 「ディスカッションボードサーバー」のデータベースを削除して、新しいデータベースをインポート...
set cmdline="%mysqlPath%" -h %cbhost% -u %cbuser% --password=%cbpass% -D %cbdb% ^< cs_cleanup.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
echo.
echo 「ディスカッションボードサーバ」データベースが削除されました
goto cs_install

:cs_upgrade
cls
echo.
echo アップデート "ディスカッション掲示板サーバ「データベース構造
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
title インストール L2JTW DP -「ディスカッションサーバー」のデータベースをインストールします。...（ステージ %stage%）
echo.
echo 新しい「ディスカッションボードサーバー」のデータベースの内容をインストールします。...
echo.
) else (
title インストール L2JTW DP - 「ディスカッションサーバー」データベースを更新します..（ステージ %stage%）
echo.
echo アップデート "ディスカッション掲示板サーバ「データベースの内容...
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
title インストール L2JTW DP - バックアップ「ゲームサーバー」データベース（ステージ %stage%）
echo.
echo 「ゲームサーバー」データベースをバックアップされて...
set cmdline="%mysqldumpPath%" --add-drop-table -h %gshost% -u %gsuser% --password=%gspass% %gsdb% ^> "%backup%\gs_backup.sql" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto gs_db_ok

:gs_err1
cls
set gsdbprompt=y
call :colors 47
set stage=5-2
title インストール L2JTW DP - 「ゲームサーバ」は、データベースのバックアップに失敗しました！（ステージ %stage%）
echo.
echo バックアップが失敗しました！
echo 「ゲームサーバー"データベースが存在しないためである
echo これで、構築を支援することができます %gsdb%，または他の設定を継続
echo.
echo 「ゲームサーバ」のデータベースを作成しますか？
echo.
echo (y)OK
echo.
echo (n)取消
echo.
echo (r)リセット
echo.
echo (q)退出
echo.
set /p gsdbprompt=選択してください：（デフォルト - OK）
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
title インストール L2JTW DP -「ゲームサーバ」の情報を作成します。（ステージ %stage%）
echo.
echo 「ゲームサーバ」のデータベースを確立している...
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -e "CREATE DATABASE %gsdb%" 2^> NUL
%cmdline%
if %ERRORLEVEL% == 0 goto gs_db_ok
if %safe_mode% == 1 goto omfg

:gs_err2
cls
set omfgprompt=q
call :colors 47
set stage=6-2
title インストール L2JTW DP - 「ゲームサーバー」は、データベースの作成に失敗しました！（ステージ %stage%）
echo.
echo 「ゲームサーバー」は、データベースの作成に失敗しました！
echo.
echo 考えられる原因：
echo 1.データ入力エラー，たとえば、次のようにユーザー名/ユーザーパスワード/その他の関連情報
echo 2.ユーザー「%gsuser%」権威の欠如
echo 3.データベースがすでに存在しています
echo.
echo 設定や修正を確認してください、または直接再設定
echo.
echo (r)リセット
echo.
echo (q)退出
echo.
set /p omfgprompt=選択してください：（デフォルト - 出口）
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto end
goto gs_err2

:gs_db_ok
cls
set installtype=u
call :colors 17
set stage=6-3
title インストール L2JTW DP - 「ゲームサーバ」データベース（ステージ %stage%）
echo.
echo 「ゲームサーバ」のデータベースをインストールします。
echo.
echo (f)完全：すべての古いデータと再インポート新しいデータが削除されます
echo.
echo (u)アップデート：すべての古いデータを保持し、更新操作になる
echo.
echo (s)省略：このオプションをスキップ
echo.
echo (q)退出
echo.
set /p installtype=選択してください：（デフォルト - 更新）
if /i %installtype%==f goto gs_cleanup
if /i %installtype%==u goto gs_upgrade
if /i %installtype%==s goto custom_ask
if /i %installtype%==q goto end
goto gs_db_ok

:gs_cleanup
call :colors 17
set cmdline=
set stage=6-4
title インストール L2JTW DP - 完全インストール"ゲームサーバー」のデータベース（ステージ %stage%）
echo.
echo 「ゲームサーバ」のデータベースを削除して、新しいデータベースをインポート...
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< gs_cleanup.sql 2^> NUL
%cmdline%
if not %ERRORLEVEL% == 0 goto omfg
set full=1
echo.
echo 「ゲームサーバー"データベースが削除されました
goto gs_install

:gs_upgrade
cls
echo.
echo 「ゲームサーバ」データベース構造を更新しました
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
title インストール L2JTW DP -「ゲームサーバ」のデータベースをインストールします。...（ステージ %stage%）
echo.
echo 新しい "ゲームサーバー"データベースの内容をインストール
echo.
) else (
title インストール L2JTW DP - 「ゲームサーバ」のデータベースを更新しました...（ステージ %stage%）
echo.
echo 「ゲームサーバ」データベースの内容を更新しました
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
set /p charprompt=インストール「NPC/アイテム/名前などのスキル」文化: (y)OKまたは (N) 取消？（デフォルト - [OK]）:
if /i %charprompt%==n goto custom_ask
for %%i in (..\sql\L2JTW_2\*.sql) do call :dump %%i
echo 完成...
echo.
echo ☆注意：システムの一部が文字化けゲームで、その結果、培養液中でのインストールに失敗します
echo 　　　　これが事実であれば、手動で内部のSQLをインポートする
echo 　　　　skill_tw / item_tw / messagetable この 3 個 SQL
goto custom_ask

:dump
set cmdline=
if /i %full% == 1 (set action=インストール) else (set action=更新)
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
REM エラーがインストールデータベース中に発生した
set dp_err=2
echo エラーがインストールデータベース中に発生した：Ertheia> ..\doc\L2J_DataPack_Ver.txt
REM ------------------------------------------------------
cls
set ntpebcak=c
call :colors 47
title インストール L2JTW DP -ステージ %stage% エラーが発生しました
echo.
echo エラー：
echo %mysqlPath% -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb%
echo.
echo アーカイブズ %~nx1
echo.
echo 治療？
echo.
echo (l)ストレージエラーメッセージは、問い合わせを容易にする
echo.
echo (c)続けていく
echo.
echo (r)リセット
echo.
echo (q)退出
echo.
set /p ntpebcak=（デフォルト - 続き）選択してください：
if /i %ntpebcak%==c (call :colors 17 & goto :eof)
if /i %ntpebcak%==l (call :logginon %1 & goto :eof)
if /i %ntpebcak%==r set dp_err=0
if /i %ntpebcak%==r (call :configure & exit)
if /i %ntpebcak%==q (call :end)
goto omfg2

:logginon
cls
call :colors 17
title インストール L2JTW DP - ストレージエラーメッセージ
set logging=1
if %full% == 1 (
  set output=%logdir%\install-%~nx1.log
) else (
  set output=%logdir%\upgrade-%~nx1.log
)
echo.
echo エラーメッセージを格納する準備ができました
echo.
echo のためのアーカイブ「%output%」
echo.
echo ファイルがすでに存在する場合、バックアップは、それ以外の場合は、過去にカバーされる
echo.
pause
set cmdline="%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^<..\sql\%1 2^>^>"%output%"
date /t >"%output%"
time /t >>"%output%"
%cmdline%
echo ストレージエラーメッセージ...
call :colors 17
set logging=0
set output=NUL
goto :eof

:custom_ask
set stage=7
title インストール L2JTW DP - custom カスタムデータシート（ステージ %stage%）
cls
set cstprompt=y
echo.
echo custom データベースに追加カスタムテーブルが完成する
echo すべてのエラーメッセージは、「custom_errors.log」に保存されます
echo.
echo 必要に応じてこれらのカスタムテーブルが有効にすることができますのでご注意ください
echo あなたが変更する必要があります config ファイルの設定
echo.
set /p cstprompt=インストール custom カスタムデータシート: (y) OKまたは (N) （デフォルト - OK）キャンセル：
if /i %cstprompt%==y goto custom_install
if /i %cstprompt%==n goto mod_ask

:custom_install
cls
echo.
echo インストール custom カスタムコンテンツ
echo @echo off> temp.bat
if exist custom_errors.log del custom_errors.log
for %%i in (..\sql\game\custom\*.sql) do echo "%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< %%i 2^>^> custom_errors.log >> temp.bat
call temp.bat> nul
del temp.bat
move custom_errors.log %workdir%
goto mod_ask

:mod_ask
set stage=8
title インストール L2JTW DP - Mod カスタムデータシート（ステージ %stage%）
cls
set cstprompt=y
echo.
echo Mod データベースに追加カスタムテーブルが完成する
echo すべての情報は間違って配置されます「mod_errors.log」
echo.
echo 必要に応じてこれらのカスタムテーブルが有効にすることができますのでご注意ください
echo あなたが変更する必要があります config ファイルの設定
echo.
echo.
set /p cstprompt=インストール Modsカスタムデータシート: (y)OK  (N)キャンセル （デフォルト - OK）：
if /i %cstprompt%==y goto mod_install
if /i %cstprompt%==n goto end

:mod_install
cls
echo.
echo インストール Modsカスタムコンテンツ
echo @echo off> temp.bat
if exist mods_errors.log del mods_errors.log
for %%i in (..\sql\game\mods\*.sql) do echo "%mysqlPath%" -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< %%i 2^>^> mods_errors.log >> temp.bat
call temp.bat> nul
del temp.bat
move mods_errors.log %workdir%
REM ------------------------------------------------------
REM データベースのインストールが完了しました
if %dp_err% == 0 set dp_err=1
REM ------------------------------------------------------
goto end

:omfg
REM ------------------------------------------------------
REM エラーがインストールデータベース中に発生した
set dp_err=2
echo エラーがインストールデータベース中に発生した：Ertheia> ..\doc\L2J_DataPack_Ver.txt
REM ------------------------------------------------------
set omfgprompt=q
call :colors 57
cls
title インストール L2JTW DP -ステージ %stage% エラーが発生しました
echo.
echo 実行中にエラーが発生しました：
echo.
echo "%cmdline%"
echo.
echo データセットは、すべての値が間違いを入力されていないことを確認するためにチェックすることをお勧めします！
echo.
if %stage% == 1 set label=ls_err1
if %stage% == 2 set label=ls_err2
if %stage% == 3 set label=cs_err1
if %stage% == 4 set label=cs_err2
if %stage% == 5 set label=gs_err1
if %stage% == 6 set label=gs_err2
echo.
echo (c)続けていく
echo.
echo (r)リセット
echo.
echo (q)退出
echo.
set /p omfgprompt=選択してください（デフォルト - 出口）:
if /i %omfgprompt%==c goto %label%
if /i %omfgprompt%==r set dp_err=0
if /i %omfgprompt%==r goto configure
if /i %omfgprompt%==q goto end
goto omfg

:binaryfind
if EXIST "%mysqlBinPath%" (echo 見つかった MySQL) else (echo 見つかりませんでした MySQL，正しい場所を入力してください...)
goto :eof

:end
REM ------------------------------------------------------
REM DPは、格納されたバージョン情報をサポート
if %dp_err% == 0 echo データベースのインストールが完了していない：Ertheia> ..\doc\L2J_DataPack_Ver.txt
if %dp_err% == 1 echo Ertheia> ..\doc\L2J_DataPack_Ver.txt
REM ------------------------------------------------------
call :colors 17
title インストール L2JTW DP - 完成
cls
echo.
echo L2JTW DP インストール
echo.
echo L2JTWサーバーをご利用いただきありがとうございます
echo 情報がhttp://www.l2jtw.comをチェックインすることができます
echo.
pause
