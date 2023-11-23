import React, {createContext, FC, ReactElement, ReactNode, useEffect} from "react";
import {SessionContext, SessionDataMissingError, State, Status} from "../types";
import {LoadingPage} from "../components/loading-page";
import {ErrorPage} from "../components/error-page";
import {HeaderMenu} from "../components/header-menu";
import {useSelector} from "react-redux";
import {getSession, sessionSelector} from "../state/session-slice";
import {useAppDispatch} from "../state/store";

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

const initialSessionState: State<SessionContext> = {
    status: Status.PENDING
}

export interface SessionProviderProps {
    children: ReactNode
}

export const SessionProvider: FC<SessionProviderProps> = (props: SessionProviderProps): ReactElement => {

    const dispatch = useAppDispatch();
    const sessionState = useSelector(sessionSelector);

    useEffect(() => {
        dispatch(getSession());
    }, [dispatch]);

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
                    <ErrorPage error={SessionDataMissingError}/>
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
