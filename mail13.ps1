param (
	[string]$arg1,
	[string]$arg2
)
# ���[�����̐ݒ�
$smtpServer = "smtp.office365.com"
$smtpPort = 587
$username = "flowlites@sadenko.co.jp"
$password = ConvertTo-SecureString "ED3sbpoqfcsY" -AsPlainText -Force
$from = "flowlites@sadenko.co.jp"
$to = $arg2
$subject = Get-Content "C:\powershell\mailtext\title01.txt" -Encoding UTF8
$array = Get-Content "C:\powershell\mailtext\body01.txt" -Encoding UTF8
$array[0] = $arg1 + "�l`n"
$body = $array -join "`n"

# �F�؏��̍쐬
$credential = New-Object System.Management.Automation.PSCredential($username, $password)

# ���[�����M
Send-MailMessage -SmtpServer $smtpServer -Port $smtpPort -Credential $credential -From $from -To $to -Subject $subject -Body $body -Encoding UTF8 -UseSsl