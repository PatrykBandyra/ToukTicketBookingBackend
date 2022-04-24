#!/usr/bin/bash
# Install Postgres DB
sudo apt-get --assume-yes install wget ca-certificates
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'
sudo apt-get --assume-yes update
sudo apt-get --assume-yes install postgresql postgresql-contrib
echo "POSTGRES INSTALLED"
sudo -H -u postgres psql -c "\conninfo"
sudo -H -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres'"  # Just in case
sudo -H -u postgres psql -c "DROP DATABASE IF EXISTS booking"
sudo -H -u postgres psql -c "CREATE DATABASE booking"
sudo -H -u postgres psql -c "\l"
echo "FINISHED POSTGRES SETUP"
echo "ADDING EXECUTABLE RIGHTS TO NECESSARY SHELL SCRIPTS"
sudo chmod +x runApp.sh
sudo chmod +x gradlew
sudo chmod +x src/main/kotlin/com/example/booking/initDB.sh
echo "FINISHED"