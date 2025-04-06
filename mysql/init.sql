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


-- Main table for CustomAuditEvent
CREATE TABLE IF NOT EXISTS custom_audit_event (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    principal VARCHAR(255),
                                    timestamp TIMESTAMP,
                                    type VARCHAR(100),
                                    data JSON
);

-- Table for the data Map<String, Object> as a key-value pair table
# CREATE TABLE IF NOT EXISTS custom_audit_event_data (
#                                          event_id BIGINT NOT NULL,
#                                          data_key VARCHAR(255) NOT NULL,
#                                          data_value TEXT,
#                                          PRIMARY KEY (event_id, data_key),
#                                          FOREIGN KEY (event_id) REFERENCES custom_audit_event(id) ON DELETE CASCADE
# );




