CREATE USER IF NOT EXISTS 'rococo_admin'@'localhost' IDENTIFIED BY 'rococo_admin123';
CREATE USER IF NOT EXISTS 'rococo_admin'@'%' IDENTIFIED BY 'rococo_admin123';
GRANT ALL PRIVILEGES ON auth-rococo-db.* TO 'rococo_admin'@'localhost';
GRANT ALL PRIVILEGES ON auth-rococo-db.* TO 'rococo_admin'@'%';
FLUSH PRIVILEGES;
SHOW GRANTS FOR 'rococo_admin'@'localhost';