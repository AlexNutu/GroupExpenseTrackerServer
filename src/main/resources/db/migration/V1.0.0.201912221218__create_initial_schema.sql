CREATE TABLE IF NOT EXISTS `user_profile` (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    modify_date DATETIME,
    email varchar(255),
    password varchar(255),
    first_name varchar(255),
    last_name varchar(255)
);

CREATE TABLE IF NOT EXISTS `trip` (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    modify_date DATETIME,
    name varchar(255),
    destination varchar(255),
    start_date DATETIME,
    end_date DATETIME
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
    modify_date DATETIME,
    expensive_type varchar(255),
    sum float(10,2),
    currency varchar(255),
    percent float(3,2),
    product varchar(255),
    user_id int,
    trip_id int,
    FOREIGN KEY (trip_id) REFERENCES trip (id),
    FOREIGN KEY (user_id) REFERENCES user_profile (id)
);

CREATE TABLE IF NOT EXISTS `to_do_list` (
	id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    modify_date DATETIME,
	description varchar(255),
    trip_id int,
    user_id int,
    finished boolean,
    FOREIGN KEY (trip_id) REFERENCES trip (id),
    FOREIGN KEY (user_id) REFERENCES user_profile (id)
);

CREATE TABLE IF NOT EXISTS `notification` (
	id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATETIME,
    modify_date DATETIME,
	title varchar(255),
    message varchar(255),
    user_id int,
    sent boolean,
    FOREIGN KEY (user_id) REFERENCES user_profile (id)
);