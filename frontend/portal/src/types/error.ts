export interface Error {
    status: number;
}

export enum ErrorCode {
    PARAM_MISSING = 1,
    SESSION_DATA_MISSING = 2,
    ORDER_DATA_MISSING = 3,
    ORDER_LIST_DATA_MISSING = 4,
    ORDER_ITEM_DATA_MISSING = 5,
    PRODUCT_DATA_MISSING = 6,
    PRODUCT_LIST_DATA_MISSING = 7
}

export const ParamMissingError: Error = {status: ErrorCode.PARAM_MISSING}
export const SessionDataMissingError: Error = {status: ErrorCode.SESSION_DATA_MISSING}
export const OrderListDataMissingError: Error = {status: ErrorCode.ORDER_LIST_DATA_MISSING}
export const ProductDataMissingError: Error = {status: ErrorCode.PRODUCT_DATA_MISSING}
export const ProductListDataMissingError: Error = {status: ErrorCode.PRODUCT_LIST_DATA_MISSING}
