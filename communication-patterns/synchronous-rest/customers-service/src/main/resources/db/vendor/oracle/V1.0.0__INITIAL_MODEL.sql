CREATE TABLE CUSTOMERS
( ID                                        NUMBER(19)                GENERATED BY DEFAULT AS IDENTITY
, CUSTOMER_ID                               VARCHAR2(36 CHAR)         NOT NULL
, FIRST_NAME                                VARCHAR2(50 CHAR)         NOT NULL
, LAST_NAME                                 VARCHAR2(50 CHAR)         NOT NULL
, ADDRESS                                   VARCHAR2(200 CHAR)        NOT NULL
, CREATED                                   TIMESTAMP(6)              NOT NULL
, MODIFIED                                  TIMESTAMP(6)
, CONSTRAINT CUSTOMERS_PK                   PRIMARY KEY (ID)
);