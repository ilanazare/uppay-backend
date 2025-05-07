CREATE TABLE fee (
    id                          INT                 NOT NULL   ,
    number_table                VARCHAR(200)        NOT NULL   ,
    number_of_installments      INT                 NOT NULL   ,
    flag                        VARCHAR(200)        NOT NULL   ,
    machine_fee                 VARCHAR(200)        NOT NULL   ,
    client_fee                  VARCHAR(200)        NOT NULL   ,
    PRIMARY KEY (id)
);