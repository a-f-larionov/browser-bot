TapLinkBot

### Что это такое?

    Рабочий, коммерческий проект.

    Проблема: Taplink.RU не представляет возможности, менять номер телефона по расписанию, например каждые 30 минут из списка 4-ех номеров.
    Решение: Автоматизация смены номера ботом, посредством WebDriver.

    Проект реализует функционал, не заложенных в самом сервисе, посредством автоматизации браузера, управление через web интерфейс и телеграм бота.

    Функционал: (//@too)

    - Смена номера в блоке "Узнать цену"
    - вкл\откл номеров менеджеров через веб интерфейс\телеграм бота;
    - включения\выключение работы расписания на будни\выходные\все дни;
    - получение номера со страницы через телеграм бота командой /get_number
    - и другое...

    Технологии:
    
    //@todo review it
    
    - Language: Java 8+ 
    - Spring, Spring boot, Maven
    - Lombok
    - Mysql server
    - WebDriver
    - Telegram Bot API

# Installation

## Ubuntu

    apt update
    apt upgrade

### Download WebDriver

### MySql

    apt install mysql-server
    mysql_secure_installation

    CREATE DATABASE browserbot DEFAULT CHARACTER SET utf_8;

    CREATE USER 'browserbot'@'' IDENTIFIED BY 'browserbot';
    GRANT ALL PRIVILEGES ON browserbot.* TO 'browserbot'@'%';
    FLUSH PRIVILEGES;

### Install WebDriver

wget https://chromedriver.chromium.org/downloads
./chromedriver --version

unzip   chromedriver_linux64.zip

### Run on startup ubuntu

    cp ./init.d/browserbot to /etc/init.d
    sudo chmod 755 /etc/init.d/browserbot
    sudo chown root:root /etc/init.d/browserbot
    sudo dos2unix /etc/init.d/browserbot
    
    sudo update-rc.d browserbot defaults
    sudo update-rc.d browserbot enable
    
    sudo service browserbot start
