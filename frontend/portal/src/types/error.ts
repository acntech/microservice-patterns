export interface Error {
    status: number;
}

export enum ErrorCode {
    PARAM_MISSING = 1,
    ORDER_DATA_MISSING = 2,
    ORDER_LIST_DATA_MISSING = 3,
    ORDER_ITEM_DATA_MISSING = 4,
    PRODUCT_DATA_MISSING = 5,
    PRODUCT_LIST_DATA_MISSING = 6
}