TapLinkBot


#Installation

##Ubuntu

    apt update
    apt upgrade

###MySql

    apt install mysql-server
    mysql_secure_installation

    CREATE USER 'taplinkbot'@'%' IDENTIFIED BY 'taplinkbot';
    GRANT ALL PRIVILEGES ON taplinkbot.* TO 'taplinkbot'@'%';
    FLUSH PRIVILEGES;


### Start on start ubuntu

    cp ./init.d/taplinkbot to /etc/init.d

    sudo chmod 755 /etc/init.d/taplinkbot
    sudo chown root:root /etc/init.d/taplinkbot
    sudo dos2unix /etc/init.d/taplinkbot
    sudo update-rc.d taplinkbot defaults
    sudo update-rc.d taplinkbot enable
    sudo service taplinkbot start