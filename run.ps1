# run.ps1 - compile and run the VolunteerManagement app
$ErrorActionPreference='Stop'
if (-not (Test-Path ".tools")) { mkdir -Force .tools }
$jar = '.tools\sqlite-jdbc.jar'
if (-not (Test-Path $jar)) {
  Write-Host 'Downloading sqlite-jdbc...'
  Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar' -OutFile $jar -UseBasicParsing
}
Write-Host 'Compiling sources...'
mkdir -Force out\classes
$files = Get-ChildItem -Recurse -Filter *.java -Path src\main\java | ForEach-Object { $_.FullName }
javac -d out\classes -cp $jar $files
Write-Host 'Starting app...'
$javaArgs = @('-cp', "out\\classes;$jar", 'com.volunteer.App')
Start-Process -FilePath 'java' -ArgumentList $javaArgs -WorkingDirectory (Get-Location).Path -NoNewWindow
Write-Host 'App started.'
