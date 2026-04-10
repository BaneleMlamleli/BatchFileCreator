#!/bin/bash

# Auto-detect OS and set package type
if [[ "$OSTYPE" == "darwin"* ]]; then
    PKG_TYPE="dmg"
elif [ -f /etc/debian_version ]; then
    PKG_TYPE="deb"
elif [ -f /etc/redhat-release ] || [ -f /etc/amazon-linux-release ]; then
    PKG_TYPE="rpm"
else
    echo "Unknown Linux distro — falling back to app-image"
    PKG_TYPE="app-image"
fi

echo "Detected OS package type: $PKG_TYPE"

# Clean and prepare
echo "Cleaning previous output..."
rm -rf ~/BatchFileCreator/output
mkdir -p ~/BatchFileCreator/output

echo "Copying dist files..."
rm -rf ~/BatchFileCreator/input
mkdir -p ~/BatchFileCreator/input
cp -r /home/banele/Documents/Programming/Java/BatchFileCreator/BatchFileCreator.jar ~/BatchFileCreator/input/

#type exe dmg deb rpm
echo "Building installer..."
jpackage \
  --type $PKG_TYPE \
  --name "BatchFileCreator" \
  --app-version "1.0.0" \
  --vendor "banelemlamleli" \
  --input ~/BatchFileCreator/input \
  --main-jar BatchFileCreator.jar \
  --main-class com.banelemlamleli.batchfilecreator.BatchFileCreator \
  --icon /home/banele/Documents/Programming/Java/BatchFileCreator/src/main/resources/icons/paper_stack.png \
  --dest ~/BatchFileCreator/output

echo "Done! Installer is in ~/BatchFileCreator/output/"