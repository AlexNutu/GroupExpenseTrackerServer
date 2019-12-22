CREATE TABLE IF NOT EXISTS `user_profile` (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    update_date DATETIME,
    username varchar(255),
    password varchar(255),
    name varchar(255),
    email varchar(255)
);

CREATE TABLE IF NOT EXISTS `trip` (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    update_date DATETIME,
    name varchar(255),
    destination varchar(255),
    start_date DATETIME
);

CREATE TABLE IF NOT EXISTS `trip_user` (
    trip_id int,
    user_id int,
    PRIMARY KEY (trip_id, user_id),
    FOREIGN KEY (trip_id) REFERENCES trip (id),
    FOREIGN KEY (user_id) REFERENCES user_profile (id)
);

CREATE TABLE IF NOT EXISTS `expense` (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    update_date DATETIME,
    expensive_type varchar(255),
    sum float(10,2),
    currency varchar(255),
    user_id int,
    trip_id int,
    FOREIGN KEY (trip_id) REFERENCES trip (id),
    FOREIGN KEY (user_id) REFERENCES user_profile (id)
);

CREATE TABLE IF NOT EXISTS `note_board` (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    update_date DATETIME,
    name varchar(255),
    trip_id int,
    FOREIGN KEY (trip_id) REFERENCES trip (id)
);

CREATE TABLE IF NOT EXISTS `note` (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    update_date DATETIME,
    message varchar(255),
    user_id int,
    note_board_id int,
    FOREIGN KEY (user_id) REFERENCES user_profile (id),
    FOREIGN KEY (note_board_id) REFERENCES trip (id)
);

CREATE TABLE IF NOT EXISTS `note_board_lock` (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    update_date DATETIME,
    locked boolean,
    user_id int,
    note_board_id int,
    FOREIGN KEY (user_id) REFERENCES user_profile (id),
    FOREIGN KEY (note_board_id) REFERENCES trip (id)
);