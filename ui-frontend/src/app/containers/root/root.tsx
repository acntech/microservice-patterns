import * as React from 'react';
import { Component, ReactNode } from 'react';
import { Route, Switch } from 'react-router';
import { BrowserRouter } from 'react-router-dom';
import { CreateItemContainer, CreateOrderContainer, HomeContainer, ItemContainer, LoginContainer, OrderContainer, PageNotFoundErrorContainer } from '../';

class RootContainer extends Component {

    public render(): ReactNode {
        return (
            <BrowserRouter>
                <Switch>
                    <Route path="/orders/:orderId?/items/:itemId?" exact component={ItemContainer} />
                    <Route path="/orders/:orderId?/create" exact component={CreateItemContainer} />
                    <Route path="/orders/:orderId?" exact component={OrderContainer} />
                    <Route path="/create" exact component={CreateOrderContainer} />
                    <Route path="/login" exact component={LoginContainer} />
                    <Route path="/" exact component={HomeContainer} />
                    <Route component={PageNotFoundErrorContainer} />
                </Switch>
            </BrowserRouter>
        );
    }
}

export { RootContainer };