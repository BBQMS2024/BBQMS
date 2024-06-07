#!/bin/bash

sudo docker stop teller
sudo docker remove teller

sudo docker stop admin
sudo docker remove admin

sudo docker stop backend
sudo docker remove backend