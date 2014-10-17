# -*- coding: utf8 -*-
def encode(s):
    return repr(s.decode('utf8')).replace(r'\n', '\n').replace(r'\x', r'\u00')[2:-1]


zh_tw = """language = 中文
toolName = GS Registering Tool

error = 錯誤
reason = 原因:
yes = 是
no = 否

fileMenu = 檔案
exitItem = 離開
helpMenu = 幫助
aboutItem = 關於

btnRemove = 移除
btnRemoveAll = 移除全部
btnRegister = 註冊遊戲伺服器
confirmRemoveTitle = 確定移除
confirmRemoveText = 確定要移除遊戲伺服器 %d - %s？
confirmRemoveAllText = 確定要移除全部遊戲伺服器？
gsListRetrieveError = 無法取得註冊過的遊戲伺服器列表。

gsName = 遊戲伺服器名稱
gsAction = 動作

registerGS = 註冊遊戲伺服器
serverName = 伺服器名稱:
save = 儲存
cancel = 取消
saveHexId = hexid.txt 檔案必須儲存在遊戲伺服器的「configs」資料夾才能正常執行。
hexidDest = 請選擇 hexid 檔案的位置...

purpose = 允許從登入伺服器「註冊/移除」遊戲伺服器。
options = 選項:
fallbackOpt = 若在註冊遊戲伺服器時，發現該ID已經註冊過，將會自動註冊為下一個可使用的ID。
forceOpt = 若在註冊遊戲伺服器時，發現該ID已經註冊過，將會強制取代此遊戲伺服器的ID位置。
cmdOpt = 強制讓此程式啟動在命令模式。
helpOpt = 顯示此説明訊息與離開。
languageOpt = 嘗試讓系統自動偵測該平臺的語言，發生錯誤時將會自動復原。
registerOpt1 = 註冊遊戲伺服器的ID為 <id>，並儲存 hexid.txt 檔案至 <hexid_dest_dir>。
registerOpt2 = 您可以在 <id> 使用負的數值，讓系統自動註冊第一個可以使用的ID。
registerOpt3 = 若 <id> 已經被使用了，系統將不會做出任何動作，除非有加入 --force 或者 --fallback 的參數。
unregisterOpt = 利用指定的 <id> 來移除遊戲伺服器，用「all」來移除目前已註冊的遊戲伺服器。
wrongUnregisterArg = 移除遊戲伺服器的指令錯誤，請指定遊戲伺服器ID，或者用「all」來移除全部。
noAction = 無任何動作。
checkingIdInUse = 檢查是否遊戲伺服器 ID: %d 已被使用...
removingGsId = 正在移除遊戲伺服器 ID: %d
forcingRegistration = 強制註冊遊戲伺服器 ID: %d，之前註冊的遊戲伺服器將會被複蓋。
fallingBack = 嘗試註冊第一個可使用的ID。
registrationOk = 遊戲伺服器成功註冊於登入伺服器，並使用 ID: %d
unregisterOk = 遊戲伺服器 ID: %d 成功從登入伺服器移除。
unregisterAllOk = 所有遊戲伺服器成功移除。
noFreeId = 已經沒有多餘可使用的ID。
sqlErrorRegister = 遊戲伺服器註冊時，發生 SQL 錯誤。
ioErrorRegister = 遊戲伺服器註冊時，發生無法存取 hexid 檔案的錯誤。
errorRegister = 遊戲伺服器註冊時，發生錯誤。
errorUnregister = 遊戲伺服器移除時，發生錯誤。
sqlErrorUnregister = 遊戲伺服器移除時，發生 SQL 錯誤。
sqlErrorUnregisterAll = 遊戲伺服器全部移除時，發生 SQL 錯誤。
noServerNames = 沒有可以使用的名稱給遊戲伺服器，請檢查 servername.xml 此檔案，是否放在登入伺服器資料夾裡面。
noNameForId = ID: %d 沒有任何名稱。
idIsNotFree = 此 ID 無法使用。
noServerForId = 無任何遊戲伺服器使用 ID: %d


cmdMenuRegister = 註冊遊戲伺服器
cmdMenuListNames = 列出遊戲伺服器名稱以及ID
cmdMenuRemoveGS = 移除遊戲伺服器
cmdMenuRemoveAll = 移除全部遊戲伺服器
cmdMenuExit = 離開
yourChoice = 選擇:
invalidChoice = 錯誤選擇: %s
gsInUse = 使用中
gsFree = 可以正常使用
enterDesiredId = 請輸入想使用的ID:

credits = © 2008-2010 L2J 團隊版權所有
language = 語言:中文
langText = 語言:中文
icons = 圖片提供 http://www.famfamfam.com
translation = 翻譯: L2J 團隊 ShanSoft
bugReports = 錯誤回報:
"""


zh_cn = """language = 中文 (简体)
toolName = GS Registering Tool

error = 错误
reason = 原因:
yes = 是
no = 否

fileMenu = 档案
exitItem = 离开
helpMenu = 帮助
aboutItem = 关于

btnRemove = 移除
btnRemoveAll = 移除全部
btnRegister = 注册游戏服务器
confirmRemoveTitle = 确认移除
confirmRemoveText = 确定要移除游戏服务器 %d - %s?
confirmRemoveAllText = 确定要移除全部游戏服务器s?
gsListRetrieveError = 无法正常取得所注册过的游戏服务器列表.

gsName = 游戏服务器名称
gsAction = 动作

registerGS = 注册游戏服务器
serverName = 服务器名称:
save = 存取
cancel = 取消
saveHexId = hexid.txt 档案必须存取在游戏服务器的 'configs' 文件夹才能正常执行.
hexidDest = 请选择hexid档案的位置...

purpose = 允许从登入服务器 注册/移除 游戏服务器.
options = 选项:
fallbackOpt = 若在注册游戏服务器的程序发现该游戏服务器ID已经注册过,将会自动注册为下一个可使用的ID.
forceOpt = 若在注册游戏服务器的程序发现该游戏服务器ID已经注册过,将会强制取代此游戏服务器ID位置.
cmdOpt = 强制让此程序启动在指令窗口模式.
helpOpt = 显示此帮助讯息与离开.
languageOpt = 尝试让系统自动侦测该平台的语言, 错误时会自动复原.
registerOpt1 = 注册游戏服务器的ID为 <id> 并存取hexid.txt档案至 <hexid_dest_dir>.
registerOpt2 = 您可以在 <id> 使用负的数值, 让系统自动注册第一个可以使用的ID.
registerOpt3 = 若 <id> 已经被使用了, 系统将不会做出任何动作, 除非有使用到 --force 或者 --fallback .
unregisterOpt = 利用指定的 <id> 来移除游戏服务器, 用 "all" 来移除目前已注册的游戏服务器.
wrongUnregisterArg = 移除游戏服务器指令错误, 请指定游戏服务器编号或者用 all 来移除全部.
noAction = 无任何动作.
checkingIdInUse = 检查是否游戏服务器 ID %d 已被使用...
removingGsId = 正在移除游戏服务器 ID: %d
forcingRegistration = 强制注册游戏服务器 ID %d, 上一个以注册的游戏服务器将会被复写.
fallingBack = 尝试注册第一个可使用的ID.
registrationOk = 游戏服务器成功注册上登入服务器并使用 ID %d.
unregisterOk = 游戏服务器 ID: %d 成功从登入服务器移除.
unregisterAllOk = 所有游戏服务器成功移除.
noFreeId = 已经没有多余可使用的ID.
sqlErrorRegister = 游戏服务器注册时发生SQL错误.
ioErrorRegister = 游戏服务器注册时发生无法正常存取hexid档案.
errorRegister = 游戏服务器注册时发生错误.
errorUnregister = 游戏服务器移除时发生错误.
sqlErrorUnregister = 游戏服务器移除时发生SQL错误.
sqlErrorUnregisterAll = 游戏服务器全部移除时发生SQL错误.
noServerNames = 没有可以使用的名称给游戏服务器, 检察是否 servername.xml 此档案再登入服务器文件夹里面.
noNameForId = ID: %d 没有任何名称.
idIsNotFree = 此 ID 无法使用.
noServerForId = 无任何游戏服务器使用 ID: %d


cmdMenuRegister = 注册游戏服务器
cmdMenuListNames = 列出游戏服务器名称以及ID
cmdMenuRemoveGS = 移除游戏服务器
cmdMenuRemoveAll = 移除全部游戏服务器
cmdMenuExit = 离开
yourChoice = 选择:
invalidChoice = 错误选择: %s
gsInUse = 以使用
gsFree = 可使用
enterDesiredId = 请输入想使用的ID:

credits = © 2008-2010 L2J 团队版权所有.
language = 语言: 中文 (简体)
langText = 语言: 中文 (简体)
icons = 图片提供 http://www.famfamfam.com
translation = 翻译: L2J 团队 ShanSoft
bugReports = 错误回报:
"""

with open('GSRegister_zh.properties','w') as f:
    f.write(encode(zh_tw))
with open('GSRegister_zh_CN.properties','w') as f:
    f.write(encode(zh_cn))
