FOR /R . %%I IN (*org.eclipse.jdt.core.prefs) DO COPY /Y settings\org.eclipse.jdt.core.prefs_backup %%~fI
pause