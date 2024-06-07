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
sudo docker exec -it desktop_app-build_1 bash -c "eas login"
sudo docker exec -it desktop_app-build_1 bash -c "npm install -g eas-cli"
sudo docker exec -it desktop_app-build_1 bash -c "eas build --local -p android --profile preview"
