CREATE DATABASE IF NOT EXISTS vs_filesharing;

USE vs_filesharing;

CREATE TABLE User (
    user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(8) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE UserCommunication (
    communication_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    user_id2 INT,
    bucketname VARCHAR(63) NOT NULL,
    CONSTRAINT fk_user_communication
        FOREIGN KEY (user_id)
            REFERENCES User(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_user2_communication
        FOREIGN KEY (user_id2)
            REFERENCES User(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE FileDetails (
    file_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    bucketname VARCHAR(63) NOT NULL,
    filename VARCHAR(255) NOT NULL,
    shardeins boolean,
    shardzwei boolean,
    user_id INT,
    CONSTRAINT fk_user_file_details
        FOREIGN KEY (user_id)
            REFERENCES User(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);
