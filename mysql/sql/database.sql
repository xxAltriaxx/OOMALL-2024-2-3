CREATE DATABASE IF NOT EXISTS prodorder;
GRANT ALL ON prodorder.* TO 'demouser'@'localhost';
GRANT ALL ON prodorder.* TO 'demouser'@'%';
FLUSH PRIVILEGES;
