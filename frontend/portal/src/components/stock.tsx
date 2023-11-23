import React, {FC, ReactElement} from 'react';
import {FormattedMessage} from "react-intl";

export interface StockProps {
    stock: number;
}

export const Stock: FC<StockProps> = (props: StockProps): ReactElement => {
    const {stock} = props;

    return (
        <span>
            {stock} <FormattedMessage id="label.in-stock"/>
        </span>
    );
}
