export type SliceStatus =
    | 'IDLE'
    | 'LOADING'
    | 'SUCCESS'
    | 'FAILED';

export interface SliceState<T1, T2> {
    status: SliceStatus;
    data?: T1;
    error?: T2;
}

export interface SliceError {
    errorId: string;
    errorCode: string;
}
