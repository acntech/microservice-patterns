CREATE TABLE ORDERS
( ID                                        NUMBER(19)                GENERATED BY DEFAULT AS IDENTITY
, ORDER_ID                                  VARCHAR2(36 CHAR)         NOT NULL
, CUSTOMER_ID                               VARCHAR2(36 CHAR)         NOT NULL
, NAME                                      VARCHAR2(50 CHAR)         NOT NULL
, DESCRIPTION                               VARCHAR2(255 CHAR)
, STATUS                                    VARCHAR2(20 CHAR)         NOT NULL
, CREATED                                   TIMESTAMP(6)              NOT NULL
, MODIFIED                                  TIMESTAMP(6)
, CONSTRAINT ORDERS_PK                      PRIMARY KEY (ID)
, CONSTRAINT ORDERS_ORDER_ID_UC             UNIQUE (ORDER_ID)
);

CREATE TABLE ITEMS
( ID                                        NUMBER(19)                GENERATED BY DEFAULT AS IDENTITY
, ORDER_ID                                  NUMBER(19)                NOT NULL
, ITEM_ID                                   VARCHAR2(36 CHAR)         NOT NULL
, PRODUCT_ID                                VARCHAR2(36 CHAR)         NOT NULL
, RESERVATION_ID                            VARCHAR2(36 CHAR)
, QUANTITY                                  NUMBER (19)               NOT NULL
, STATUS                                    VARCHAR2(20 CHAR)         NOT NULL
, CREATED                                   TIMESTAMP(6)              NOT NULL
, MODIFIED                                  TIMESTAMP(6)
, CONSTRAINT ITEMS_PK                       PRIMARY KEY (ID)
, CONSTRAINT ITEMS_ITEM_ID_UC               UNIQUE (ITEM_ID)
, CONSTRAINT ITEMS_ORDER_ID_PRODUCT_ID_UC   UNIQUE (ORDER_ID, PRODUCT_ID)
, CONSTRAINT ITEMS_ORDER_ID_FK              FOREIGN KEY (ORDER_ID)    REFERENCES ORDERS (ID)
);