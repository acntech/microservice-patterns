import * as React from 'react';
import {Component, ReactNode} from 'react';
import {User} from '../../models/types';

interface ComponentState {
    user?: User;
}

const AuthenticationContext = React.createContext({});

class AuthenticationProvider extends Component<any, ComponentState> {

    public render(): ReactNode {
        return <AuthenticationContext.Provider value={this.state} {...this.props} />;
    }
}

const AuthenticationConsumer = AuthenticationContext.Consumer;

const useAuth = () => React.useContext(AuthenticationContext);

export {AuthenticationProvider, AuthenticationConsumer, useAuth};