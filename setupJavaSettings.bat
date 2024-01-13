@REM Запускать только от имени администратора
assoc .jar=javafile && ftype jarfile=%JAVA_PATH%\javaw.exe -jar "%1" %*