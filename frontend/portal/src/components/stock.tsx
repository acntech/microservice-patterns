import React, {FC, ReactElement} from 'react';
import {FormattedMessage} from "react-intl";

export interface StockPanelProps {
    stock: number;
}

export const StockPanel: FC<StockPanelProps> = (props): ReactElement => {
    const {stock} = props;

    return (
        <span>
            {stock} <FormattedMessage id="label.in-stock"/>
        </span>
    );
};
