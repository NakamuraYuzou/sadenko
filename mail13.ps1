param (
	[string]$arg1,
	[string]$arg2
)
# メール情報の設定
$smtpServer = "smtp.office365.com"
$smtpPort = 587
$username = "flowlites@sadenko.co.jp"
$password = ConvertTo-SecureString "ED3sbpoqfcsY" -AsPlainText -Force
$from = "flowlites@sadenko.co.jp"
$to = $arg2
$subject = Get-Content "C:\powershell\mailtext\title01.txt" -Encoding UTF8
$array = Get-Content "C:\powershell\mailtext\body01.txt" -Encoding UTF8
$array[0] = $arg1 + "様`n"
$body = $array -join "`n"

# 認証情報の作成
$credential = New-Object System.Management.Automation.PSCredential($username, $password)

# メール送信
Send-MailMessage -SmtpServer $smtpServer -Port $smtpPort -Credential $credential -From $from -To $to -Subject $subject -Body $body -Encoding UTF8 -UseSsl