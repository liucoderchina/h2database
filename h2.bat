@java -cp "h2\target\h2-1.4.196-SNAPSHOT.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console %*
@if errorlevel 1 pause