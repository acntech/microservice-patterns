CREATE TABLE ORDERS
(
    ID          INTEGER GENERATED BY DEFAULT AS IDENTITY,
    ORDER_ID    VARCHAR(36)  NOT NULL,
    CUSTOMER_ID VARCHAR(36)  NOT NULL,
    NAME        VARCHAR(50)  NOT NULL,
    DESCRIPTION VARCHAR(255),
    STATUS      VARCHAR(20)  NOT NULL,
    CREATED     TIMESTAMP(6) NOT NULL,
    MODIFIED    TIMESTAMP(6),
    CONSTRAINT ORDERS_PK PRIMARY KEY (ID),
    CONSTRAINT ORDERS_ORDER_ID_UC UNIQUE (ORDER_ID)
);

CREATE TABLE ORDER_ITEMS
(
    ID             INTEGER GENERATED BY DEFAULT AS IDENTITY,
    ORDER_ID       INTEGER      NOT NULL,
    ITEM_ID        VARCHAR(36)  NOT NULL,
    PRODUCT_ID     VARCHAR(36)  NOT NULL,
    RESERVATION_ID VARCHAR(36),
    QUANTITY       INTEGER      NOT NULL,
    STATUS         VARCHAR(20)  NOT NULL,
    CREATED        TIMESTAMP(6) NOT NULL,
    MODIFIED       TIMESTAMP(6),
    CONSTRAINT ORDER_ITEMS_PK PRIMARY KEY (ID),
    CONSTRAINT ORDER_ITEMS_ITEM_ID_UC UNIQUE (ITEM_ID),
    CONSTRAINT ORDER_ITEMS_ORDER_ID_PRODUCT_ID_UC UNIQUE (ORDER_ID, PRODUCT_ID),
    CONSTRAINT ORDER_ITEMS_ORDER_ID_FK FOREIGN KEY (ORDER_ID) REFERENCES ORDERS (ID)
);
