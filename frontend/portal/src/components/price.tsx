import React, {FC, ReactElement} from 'react';
import {FormattedMessage} from "react-intl";

export interface PriceProps {
    price: number;
    currency: string;
}

export const Price: FC<PriceProps> = (props: PriceProps): ReactElement => {
    const {price, currency} = props;

    return (
        <span className="fw-bold">
            <FormattedMessage id={`enum.currency-code.${currency}`}/> {price.toFixed(2)}
        </span>
    );
}
