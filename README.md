TapLinkBot

### Что это такое?

    Рабочий, коммерческий проект.

    Пробелма: Taplink.RU не представляет возможности, менять номер телефона по расписанию, например каждые 30 минут из списка 4-ех номеров.
    Решение: Автоматизация смены номера ботом, посредством WebDriver.

    Проект реализует функционал на сервисе https://taplink.ru/, не заложенных в самом сервисе, посредством автоматизации браузера.

    Функционал: (@too)

    - смена номера на странице, по рассписанию;
    - вкл\откл номеров менеджеров через веб интерфейс\телеграм бота;
    - включения\выключение работы расписания на будни\выходные\все дни;
    - получение номера со страницы через телеграм бота командой /get_number
    - и другое...

    Технологии:
    
    - Language: Java 
    - Spring, Spring boot
    - Lombok
    - Mysql server
    

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