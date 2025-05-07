CREATE TABLE users (
    username                        VARCHAR(200)        NOT NULL   ,
    password                        VARCHAR(200)        NOT NULL   ,
    authorities                     VARCHAR(100)        NOT NULL   ,
    PRIMARY KEY (username)
);