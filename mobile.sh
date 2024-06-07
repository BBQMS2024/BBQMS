#!/bin/bash

echo "Building Mobile App APK"
cd mobile-app
sudo docker pull hmerritt/android-sdk:latest
sudo docker run -d --name mobile-app-app-build-1 --restart on-failure -v "$(pwd)":/project/app hmerritt/android-sdk:latest

sudo docker exec -it mobile-app-app-build-1 bash -c "npm install -g eas-cli"
sudo docker exec -it mobile-app-app-build-1 bash -c "eas build --local -p android --profile preview"

[ -f ./output.apk ] && sudo rm ./output.apk # Remove the output.apk file if it exists
apk_path=$(sudo docker exec mobile-app-app-build-1 bash -c "ls -t /project/app/ | grep build-*.apk | head -n 1")
sudo docker cp "mobile-app-app-build-1:/project/app/$apk_path" ./output.apk
echo "The APK file has been copied to ./output.apk"
