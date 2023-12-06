import React, {createContext, FC, ReactElement, ReactNode, useEffect} from "react";
import {useSelector} from "react-redux";
import {useAppDispatch} from "../state/store";
import {getSession, sessionSelector} from "../state/session-slice";
import {ErrorPage, HeaderMenu, LoadingPage} from "../components";
import {SessionContext, SessionDataMissingStateError, Status} from "../types";

const initialSession: SessionContext = {
    sid: "",
    userContext: {
        uid: "",
        username: "",
        firstName: "",
        lastName: "",
        roles: []
    }
}

export const Session = createContext(initialSession);

export interface SessionProviderProps {
    children: ReactNode
}

export const SessionProvider: FC<SessionProviderProps> = (props: SessionProviderProps): ReactElement => {

    const dispatch = useAppDispatch();
    const sessionState = useSelector(sessionSelector);

    useEffect(() => {
        if (sessionState.status === Status.PENDING) {
            dispatch(getSession());
        }
    }, [dispatch, sessionState]);

    if (sessionState.status === Status.LOADING) {
        return <LoadingPage/>;
    } else if (sessionState.status === Status.FAILED) {
        return (
            <>
                <HeaderMenu hideMenu={true}/>
                <ErrorPage error={sessionState.error}/>
            </>
        );
    } else {
        if (!sessionState.data) {
            return (
                <>
                    <HeaderMenu hideMenu={true}/>
                    <ErrorPage error={SessionDataMissingStateError}/>
                </>
            );
        } else {
            return (
                <Session.Provider value={sessionState.data}>
                    {props.children}
                </Session.Provider>
            )
        }
    }
};
