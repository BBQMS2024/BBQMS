#!/bin/bash

echo "Updating package manager"
sudo apt update -y

echo "Installing Git"
sudo apt install git -y

echo "Installing Docker Engine"
sudo apt install docker.io -y

echo "Installing Java 17"
sudo apt install openjdk-17-jdk openjdk-17-jre -y

echo "Installing Maven"
sudo apt install maven -y

#echo "Installing Node 21"
#sudo apt install curl -y
#sudo curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
#export NVM_DIR="$HOME/.nvm"
#[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"
#[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"
#nvm install 21
#nvm use 21

echo "Pulling DB Docker Image"
sudo docker pull mysql

echo "Starting DB Container"
sudo docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=bbqms --name db -d mysql

echo "Building Backend"
cd backend/stonebase
sudo git submodule update --init --recursive
sudo mvn install
cd ..
sudo mvn install -DskipTests
sudo docker build -t "backend" .

echo "Building Admin App Frontend"
cd ../admin-app
sudo docker build -t "admin-app" .

echo "Building Teller App Frontend"
cd ../teller-app
sudo docker build -t "teller-app" .

echo "Starting Admin App Frontend"
sudo docker run -p 5000:5000 --name admin -d admin-app

echo "Starting Teller App Frontend"
sudo docker run -p 3000:3000 --name teller -d teller-app

echo "Starting Backend"
cd ../backend
sudo docker run -p 8080:8080 --name backend -d \
    -e JWT_KEY=11111111111111111111111111111111 \
    -e GOOGLE_CLIENT_ID=123 \
    -e DB_URL=jdbc:mysql://host.docker.internal:3306 \
    -e DB_USERNAME=root \
    -e DB_PASSWORD=password \
    -e DB_SCHEMA=bbqms \
    --add-host=host.docker.internal:host-gateway \
    backend
