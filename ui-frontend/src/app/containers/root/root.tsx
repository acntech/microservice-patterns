import * as React from 'react';
import { Component, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Route, Switch } from 'react-router';
import { Action } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { ClientConfigErrorContainer, CreateItemContainer, CreateOrderContainer, HomeContainer, ItemContainer, LoginContainer, OrderContainer, PageNotFoundErrorContainer } from '../';
import { LoadingIndicator, PrimaryHeader } from '../../components';
import { ConfigState, RootState } from '../../models';
import { getConfig } from '../../state/actions';

interface ComponentStateProps {
    configState: ConfigState;
}

interface ComponentDispatchProps {
    getConfig: () => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps;

class RootContainer extends Component<ComponentProps> {

    public componentDidMount(): void {
        this.props.getConfig();
    }

    public render(): ReactNode {
        const {loading, error} = this.props.configState;

        if (loading) {
            return (
                <main>
                    <LoadingIndicator />
                </main>
            );
        } else {
            if (error) {
                return (
                    <main>
                        <ClientConfigErrorContainer />
                    </main>
                );
            } else {
                return (
                    <main>
                        <PrimaryHeader />
                        <Switch>
                            <Route path="/orders/:orderId?/items/:itemId?" exact component={ItemContainer} />
                            <Route path="/orders/:orderId?/create" exact component={CreateItemContainer} />
                            <Route path="/orders/:orderId?" exact component={OrderContainer} />
                            <Route path="/create" exact component={CreateOrderContainer} />
                            <Route path="/login" exact component={LoginContainer} />
                            <Route path="/" exact component={HomeContainer} />
                            <Route component={PageNotFoundErrorContainer} />
                        </Switch>
                    </main>
                );
            }
        }
    }
}

const mapStateToProps = (state: RootState): Partial<ComponentStateProps> => ({
    configState: state.configState
});

const mapDispatchToProps = (dispatch: ThunkDispatch<RootState, void, Action>): Partial<ComponentDispatchProps> => ({
    getConfig: () => dispatch(getConfig())
});

const ConnectedRootContainer = connect(mapStateToProps, mapDispatchToProps)(RootContainer);

export { ConnectedRootContainer as RootContainer };