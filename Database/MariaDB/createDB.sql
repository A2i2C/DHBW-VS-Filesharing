CREATE DATABASE IF NOT EXISTS vs_filesharing;

USE vs_filesharing;

CREATE TABLE USER (
    user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password INT NOT NULL
);

CREATE TABLE FileDetails (
    filename VARCHAR(255) PRIMARY KEY,
    minioeins boolean,
    miniozwei boolean,
    miniodrei boolean,
    user_id INT,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES USER(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);
