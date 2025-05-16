CREATE TABLE loan (
    id                                  INT                 NOT NULL   ,
    customer                            VARCHAR(200)        NOT NULL   ,
    purchase_value                      VARCHAR(200)        NOT NULL   ,
    number_of_installments              INT                 NOT NULL   ,
    installment_value                   VARCHAR(200)        NOT NULL   ,
    amount_retained_by_machine          VARCHAR(200)        NOT NULL   ,
    amount_released_by_machine          VARCHAR(200)        NOT NULL   ,
    amount_retained                     VARCHAR(200)        NOT NULL   ,
    amount_released_for_client          VARCHAR(200)        NOT NULL   ,
    purchase_date                       VARCHAR(200)        NOT NULL   ,
    PRIMARY KEY (id)
);