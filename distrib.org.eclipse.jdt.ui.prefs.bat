FOR /R . %%I IN (*org.eclipse.jdt.ui.prefs) DO COPY /Y settings\org.eclipse.jdt.ui.prefs_backup %%~fI
pause