BrowserBot Project

### Что это такое?

    Проект создан для автоматической смены номера телефона каждые 30 минут через админку taplink.ru, которые отображается на рекламной странице.
    Таким образом распределеяя нагрузку между несколькими менеджерами работающими с клиентами.


# Функционал:

    - Смена номера телефона через админку роботом в один клик; 
    - Выполнение смены номера каждые 30 минут, с соблюдением условий будних\выходных дней;
    - Управлением списком номеров телефонов;
    - Возможность управлять через WEB GUI;
    - Возможность управлять через TelegramBot.
   
###   Для управления через телеграм бота для достаточно добавить человек в чат с ботом, командой /help можно получить все имеющися возможности.
   
###   Для управления через веб-интерфейс, необходимо зайти по соответсвующему URL, ввести логин и пароль.
   
    
   Технологии:
      
    - Java 8+ 
    - Maven
    - Spring, Spring Boot, Spring Security, Spring Testing
    - Telegram Bot API, Mysql server, WebDriver
    - Lombok, Mockito, ModelMapper
    
# Installation

## Ubuntu

    apt update
    apt upgrade

### Install Java

sudo apt install default-jre

### MySql

    apt install mysql-server
    mysql_secure_installation

    CREATE DATABASE browserbot DEFAULT CHARACTER SET utf_8;

    CREATE USER 'browserbot'@'' IDENTIFIED BY 'browserbot';
    GRANT ALL PRIVILEGES ON browserbot.* TO 'browserbot'@'%';
    FLUSH PRIVILEGES;

### Download WebDriver

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
