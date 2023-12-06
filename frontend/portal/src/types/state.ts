import {StateError} from "./error";

export enum Status {
    PENDING,
    LOADING,
    SUCCESS,
    FAILED
}

export interface State<T> {
    status: Status;
    data?: T;
    error?: StateError;
}