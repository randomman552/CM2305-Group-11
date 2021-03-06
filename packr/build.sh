# Get JRE's
wget -q -O Java_Windows.zip https://builds.openlogic.com/downloadJDK/openlogic-openjdk-jre/11.0.8%2B10/openlogic-openjdk-jre-11.0.8%2B10-windows-x64.zip
wget -q -O Java_Linux.tar.gz https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/OpenJDK11U-jre_x64_linux_hotspot_11.0.11_9.tar.gz
wget -q -O Java_Mac.tar.gz https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.11%2B9/OpenJDK11U-jre_x64_mac_hotspot_11.0.11_9.tar.gz

# Run build
java -jar packr-all.jar windows.json
java -jar packr-all.jar linux.json
java -jar packr-all.jar mac.json

# Zip results
cd out
zip -q -r windows.zip ./windows
zip -q -r linux.zip ./linux
zip -q -r mac.zip ./mac