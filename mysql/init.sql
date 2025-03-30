CREATE DATABASE IF NOT EXISTS auth_service;

CREATE TABLE IF NOT EXISTS ROLES (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              name VARCHAR(255) NOT NULL
);

# INSERT INTO ROLES (name) VALUES ('ADMIN');
# INSERT INTO ROLES (name) VALUES ('USER');

CREATE TABLE IF NOT EXISTS USER (
                       ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                       NAME VARCHAR(255) NOT NULL,
                       USERNAME VARCHAR(255) UNIQUE NOT NULL,
                       PASSWORD VARCHAR(255) NOT NULL,
                       EMAIL VARCHAR(255) UNIQUE NOT NULL,
                       CONTACT VARCHAR(20) UNIQUE
);


CREATE TABLE IF NOT EXISTS USER_ROLES (
                            USER_ID BIGINT NOT NULL,
                            ROLE_ID BIGINT NOT NULL,
                            PRIMARY KEY (USER_ID, ROLE_ID),
                            FOREIGN KEY (USER_ID) REFERENCES USER(ID) ON DELETE CASCADE,
                            FOREIGN KEY (ROLE_ID) REFERENCES ROLES(ID) ON DELETE CASCADE
);



