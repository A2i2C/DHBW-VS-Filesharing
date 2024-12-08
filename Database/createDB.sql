CREATE DATABASE IF NOT EXISTS vs_filesharing;

USE vs_filesharing;

CREATE TABLE USER (
    user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password INT NOT NULL
);

CREATE TABLE UserCommunication (
    communication_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    user_id2 INT,
    CONSTRAINT fk_user_communication
        FOREIGN KEY (user_id)
            REFERENCES USER(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_user2_communication
        FOREIGN KEY (user_id2)
            REFERENCES USER(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE UserCommunicationMessages (
    message_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    communication_id INT,
    message VARCHAR(255),
    CONSTRAINT fk_user_communication_messages
        FOREIGN KEY (communication_id)
            REFERENCES UserCommunication(communication_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE FileDetails (
    filename VARCHAR(255) PRIMARY KEY,
    minioeins boolean,
    miniozwei boolean,
    miniodrei boolean,
    user_id INT,
    CONSTRAINT fk_user_file_details
        FOREIGN KEY (user_id)
            REFERENCES USER(user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);
