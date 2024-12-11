CREATE DATABASE IF NOT EXISTS vs_filesharing;

USE vs_filesharing;

CREATE TABLE User (
    user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password INT NOT NULL
);

CREATE TABLE FileDetails (
    file_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    shardeins boolean,
    shardzwei boolean,
    user_id INT,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES USER(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);