import * as React from 'react';
import { Component, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Route, Switch } from 'react-router';
import { Action } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { CreateItemContainer, CreateOrderContainer, HomeContainer, ItemContainer, LoginContainer, OrderContainer, PageNotFoundErrorContainer } from '../';
import { PrimaryHeader } from '../../components/headers';
import { RootState } from '../../models/types';
import { getConfig } from '../../state/actions';

interface ComponentStateProps {
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

const mapStateToProps = (state: RootState): Partial<ComponentStateProps> => ({});

const mapDispatchToProps = (dispatch: ThunkDispatch<RootState, void, Action>): Partial<ComponentDispatchProps> => ({
    getConfig: () => dispatch(getConfig())
});

const ConnectedRootContainer = connect(mapStateToProps, mapDispatchToProps)(RootContainer);

export { ConnectedRootContainer as RootContainer };