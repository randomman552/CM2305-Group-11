wget -O Java.zip https://builds.openlogic.com/downloadJDK/openlogic-openjdk-jre/11.0.8%2B10/openlogic-openjdk-jre-11.0.8%2B10-windows-x64.zip
java -jar packr-all.jar windows.json
zip windows.zip ./windows/*