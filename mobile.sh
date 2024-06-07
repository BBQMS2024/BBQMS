# USERNAME : emacaga
# PASSWORD : bbqms13579

filename="docker-compose.yml"

echo "version: '3.1'" | sudo tee "$filename"
echo "services:" | sudo tee -a "$filename"
echo " app-build:" | sudo tee -a "$filename"
echo "  image: hmerritt/android-sdk:latest" | sudo tee -a "$filename"
echo "  network_mode: 'host'" | sudo tee -a "$filename"
echo "  restart: on-failure" | sudo tee -a "$filename"
echo "  volumes:" | sudo tee -a "$filename"
echo "   - ./:/project/app" | sudo tee -a "$filename"

sudo apt install docker-compose -y
sudo docker-compose --version
sudo docker-compose up -d
sudo docker exec -it mobile-app-app-build-1 bash -c "npm install -g eas-cli"
sudo docker exec -it mobile-app-app-build-1 bash -c "eas build --local -p android --profile preview"

[ -f ./output.apk ] && sudo rm ./output.apk # Remove the output.apk file if it exists
apk_path=$(sudo docker exec mobile-app-app-build-1 bash -c "ls -t /project/app/ | grep build-*.apk | head -n 1") # Get the latest APK file
sudo docker cp "mobile-app-app-build-1:/project/app/$apk_path" ./output.apk # Copy the APK file to the host
echo "The APK file has been copied to ./output.apk" # Print the message
