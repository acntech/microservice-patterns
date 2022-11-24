import React, {FC, ReactElement} from 'react';
import {Route, Switch} from 'react-router';
import {CreateOrderItemPage, CreateOrderPage, HomePage, NotFoundErrorPage, OrderItemPage, OrderPage} from './pages';

export const Routes: FC = (): ReactElement => {
    return (
        <Switch>
            <Route path="/orders/:orderId?/items/:itemId?" exact component={OrderItemPage}/>
            <Route path="/orders/:orderId?/create" exact component={CreateOrderItemPage}/>
            <Route path="/orders/:orderId?" exact component={OrderPage}/>
            <Route path="/create" exact component={CreateOrderPage}/>
            <Route path="/" exact component={HomePage}/>
            <Route component={NotFoundErrorPage}/>
        </Switch>
    );
};
