@echo off
set GS_DEPLOYED=
set DP_DEPLOYED=
set EXIT_F=
set DEPLOY_DIR=..\..\staging

:deployed_start
echo デプロイを開始します。

:gsdeployed_start
set /P GS_DEPLOYED="ゲームサーバーをデプロイしますか？(y/n) "
if /i %GS_DEPLOYED%==y (goto gs_deployed_run) else (goto gs_deployed_end)

:gs_deployed_run
echo ゲームサーバーのデプロイを開始します
robocopy server\build\dist %DEPLOY_DIR%\ /s /xf General.properties>gsdeploylog.txt
echo ゲームサーバーのデプロイが完了しました

:gs_deployed_end

:dp_deployed_start
set /P DP_DEPLOYED="データーパックをデプロイしますか？(y/n) "
if /i %DP_DEPLOYED%==y (goto dp_deployed_run) else (goto dp_deployed_end)

:dp_deployed_run
echo データーパックのデプロイを開始します
robocopy dp\build\dist %DEPLOY_DIR%\ /s >dpdeploylog.txt

echo データーパックのデプロイが完了しました

:dp_deployed_end
:exit_start
set /P EXIT_F="終了しますか？(y/n) "
if /i %EXIT_F%==y (goto exit_run) else (goto deployed_start)

:exit_run
echo 終了します。
pause
exit
