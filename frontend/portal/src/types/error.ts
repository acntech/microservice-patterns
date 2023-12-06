export interface StateError {
    status: number;
    message: string;
    redirectUrl?: string;
}

export enum ErrorCode {
    UNCATEGORIZED = 0,
    PARAM_MISSING = 1,
    SESSION_DATA_MISSING = 2,
    ORDER_DATA_MISSING = 3,
    ORDER_LIST_DATA_MISSING = 4,
    ORDER_ITEM_DATA_MISSING = 5,
    PRODUCT_DATA_MISSING = 6,
    PRODUCT_LIST_DATA_MISSING = 7
}

export const UncategorizedStateError: StateError = {status: ErrorCode.UNCATEGORIZED, message: "Uncategorized Error"}
export const ParamMissingStateError: StateError = {status: ErrorCode.PARAM_MISSING, message: "Parameter Missing"}
export const SessionDataMissingStateError: StateError = {
    status: ErrorCode.SESSION_DATA_MISSING,
    message: "Session Data Missing"
}
export const OrderListDataMissingStateError: StateError = {
    status: ErrorCode.ORDER_LIST_DATA_MISSING,
    message: "Order List Data Missing"
}
export const ProductDataMissingStateError: StateError = {
    status: ErrorCode.PRODUCT_DATA_MISSING,
    message: "Product Data Missing"
}
export const ProductListDataMissingStateError: StateError = {
    status: ErrorCode.PRODUCT_LIST_DATA_MISSING,
    message: "Product List Data Missing"
}
