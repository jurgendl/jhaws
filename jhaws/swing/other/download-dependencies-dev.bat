mkdir target
"%JAVA_HOME%/bin/javac" -d target src/main/java/DownloadDependencies.java
"%JAVA_HOME%/bin/java" -cp target DownloadDependencies