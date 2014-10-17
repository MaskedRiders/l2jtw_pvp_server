@echo off
title Game Server Console
REM ------------------------------------------------------
REM 檢查是否存在 DP 支援的版本資訊
if not exist ..\doc\L2J_DataPack_Ver.txt echo DP 沒有正確的完成編譯！
if not exist ..\doc\L2J_DataPack_Ver.txt echo 請再一次：更新 DP → 編譯 DP → 解壓縮 DP → 安裝資料庫
if not exist ..\doc\L2J_DataPack_Ver.txt echo.
if not exist ..\doc\L2J_DataPack_Ver.txt pause
if not exist ..\doc\L2J_DataPack_Ver.txt exit
REM 取得 DP 支援的版本資訊
FOR /F %%g IN (..\doc\L2J_DataPack_Ver.txt) DO set vdp=%%g
REM 檢查 DP 支援的版本資訊
if not %vdp% == Ertheia echo 無法繼續執行伺服器，因為：
if not %vdp% == Ertheia echo GS 支援的版本是：Ertheia
if not %vdp% == Ertheia echo DP 支援的版本是：%vdp%
if not %vdp% == Ertheia echo 請確定 GS 和 DP 都使用相同的版本後，再試一次
if not %vdp% == Ertheia echo.
if not %vdp% == Ertheia pause
if not %vdp% == Ertheia exit
REM ------------------------------------------------------
REM 刪除已經達到 90 個以上的 log (因為達到 100 個後,GS就會出錯)
if not exist log\backup\ md log\backup\
if exist log\accounting*.log.90* (
copy log\accounting*.* log\backup\ /Y > nul
del log\accounting*.* /F /Q > nul
)
if exist log\chat*.log.90* (
copy log\chat*.* log\backup\ /Y > nul
del log\chat*.* /F /Q > nul
)
if exist log\error*.log.90* (
copy log\error*.* log\backup\ /Y > nul
del log\error*.* /F /Q > nul
)
if exist log\item*.log.90* (
copy log\item*.* log\backup\ /Y > nul
del log\item*.* /F /Q > nul
)
if exist log\java*.log.90* (
copy log\java*.* log\backup\ /Y > nul
del log\java*.* /F /Q > nul
)
if exist log\olympiad*.csv.90* (
copy log\olympiad*.* log\backup\ /Y > nul
del log\olympiad*.* /F /Q > nul
)
REM ------------------------------------------------------

call :init
:: ----- 設定 java 記憶體用量及其他參數 -----
set javaheap_Xms=1024m
set javaheap_Xmx=1536m

set java_param=com.l2jserver.gameserver.GameServer
set java_param=-cp "%iii%libs\*";"%LLL%l2jserver.jar" %java_param%
set java_param=-Xms%javaheap_Xms% -Xmx%javaheap_Xmx% %java_param%
:: set java_param=-XX:PermSize=50m %java_param%
:: set java_param=-XX:MaxPermSize=100m %java_param%
set java_param=-XX:+UseParNewGC %java_param%
set java_param=-XX:+CMSParallelRemarkEnabled %java_param%
set java_param=-XX:+UseConcMarkSweepGC %java_param%
set java_param=-Djava.util.logging.manager=com.l2jserver.util.L2LogManager %java_param%
:: set java_param=-Djava.util.logging.manager=com.l2jserver.util.L2LogManager -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseParNewGC -XX:+UseCMSCompactAtFullCollection -XX:+CMSIncrementalMode -Xms%javaheap% -Xmx%javaheap% -cp "%iii%libs\*";"%LLL%l2jserver.jar" com.l2jserver.gameserver.GameServer
goto start

:init
set LLL=%~dp0
::FOR /F "delims=game" %%i IN ("%LLL%") do set iii=%%i
set iii=%LLL:~0,-5%
call :check_libs
call :try_java
goto :eof

:check_libs
REM ------------------------------------------------------
if not exist ..\libs\*.jar (
	echo 您必須重新解壓縮「編譯完成」的 GS，才可以繼續執行
	echo.
	pause
	exit
)
goto :eof
REM ------------------------------------------------------

:check_java
if "%found%"=="1" goto :eof
if not exist %1 goto :eof
set found=1
set PATH=%~dp1
echo 使用 java 位置 %path%
echo.
goto :eof

:set_java
echo 搜尋 java 位置
call :check_java "%ProgramW6432%\Java\jre8\bin\java.exe"
call :check_java "%ProgramFiles%\Java\jre8\bin\java.exe"
call :check_java "%ProgramFiles(x86)%\Java\jre8\bin\java.exe"
if not exist "%ProgramW6432%\Java\j*1.8.*" goto _next_set_x64
dir "%ProgramW6432%\Java\j*1.8.*" /A:D /B /O > %temp%\check.txt
FOR /F %%j IN (%temp%\check.txt) DO set ver=%%j
call :check_java "%ProgramFiles%\Java\%ver%\bin\java.exe"
:_next_set_x64
if not exist "%ProgramFiles%\Java\j*1.8.*" goto _next_set_x86
dir "%ProgramFiles%\Java\j*1.8.*" /A:D /B /O > %temp%\check.txt
FOR /F %%j IN (%temp%\check.txt) DO set ver=%%j
call :check_java "%ProgramFiles%\Java\%ver%\bin\java.exe"
:_next_set_x86
if not exist "%ProgramFiles(x86)%\Java\j*1.8.*" goto _next_set_java
dir "%ProgramFiles(x86)%\Java\j*1.8.*" /A:D /B /O > %temp%\check.txt
FOR /F %%j IN (%temp%\check.txt) DO set ver=%%j
call :check_java "%ProgramFiles(x86)%\Java\%ver%\bin\java.exe"
:_next_set_java
call :check_java "%windir%\system32\java.exe"
if "%found%"=="" (
	echo 找不到 java.exe
	echo 請先安裝 java 1.8
	echo 下載及安裝可參考論壇
	echo http://www.l2jtw.com/l2jtwbbs/viewtopic.php?f=42^&t=6264
	echo.
	pause
	exit
)
goto :eof

:try_java
java -version:1.8 -cp "%iii%libs\*";"%LLL%l2jserver.jar" com.l2jserver.JavaTest
if not %ERRORLEVEL% == 0 call :set_java
goto :eof

:start
echo Starting L2J Game Server.
echo.

java %java_param%

REM NOTE: If you have a powerful machine, you could modify/add some extra parameters for performance, like:
REM -Xms1536m
REM -Xmx3072m
REM -XX:+AggressiveOpts
REM Use this parameters carefully, some of them could cause abnormal behavior, deadlocks, etc.
REM More info here: http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end

:restart
echo.
echo Admin Restarted Game Server.
echo.
goto start

:error
echo.
echo Game Server Terminated Abnormally!
echo.

:end
echo.
echo Game Server Terminated.
echo.
pause