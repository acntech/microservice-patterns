import React, {FC, ReactElement} from 'react';
import {FormattedMessage} from "react-intl";

export interface PricePanelProps extends React.HTMLAttributes<HTMLElement> {
    price: number;
    currency: string;
}

export const PricePanel: FC<PricePanelProps> = (props): ReactElement => {
    const {className, price, currency} = props;

    return (
        <span className={className}>
            <FormattedMessage id={`enum.currency-code.${currency}`}/> {price.toFixed(2)}
        </span>
    );
}
