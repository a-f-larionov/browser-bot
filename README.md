TapLinkBot

    Это рабочий, коммерческий проект.

    Автоматизации действий на сервисе taplink, не заложенных в самом сервисе.

    Некоторый функционал:

    - смена номера менеджера по рассписанию;
    - вкл\откл номеров менеджеров через веб интерфейс\телеграм бота;
    - включения\выключение работы расписания на будни\выходные\все дни;
    - получение номера со страницы через телеграм бота командой /get_number
    - и другое...

    

# Installation

## Ubuntu

    apt update
    apt upgrade



### MySql

    apt install mysql-server
    mysql_secure_installation

    CREATE USER 'taplinkbot'@'%' IDENTIFIED BY 'taplinkbot';
    GRANT ALL PRIVILEGES ON taplinkbot.* TO 'taplinkbot'@'%';
    FLUSH PRIVILEGES;


### Run on startup ubuntu

    cp ./init.d/taplinkbot to /etc/init.d

    sudo chmod 755 /etc/init.d/taplinkbot
    sudo chown root:root /etc/init.d/taplinkbot
    sudo dos2unix /etc/init.d/taplinkbot
    sudo update-rc.d taplinkbot defaults
    sudo update-rc.d taplinkbot enable
    sudo service taplinkbot start