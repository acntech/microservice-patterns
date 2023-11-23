import React, {FC, ReactElement} from 'react';
import {FormattedMessage} from "react-intl";

export interface QuantityProps {
    packaging: string;
    quantity: number;
    measure: string;
}

export const Quantity: FC<QuantityProps> = (props: QuantityProps): ReactElement => {
    const {packaging, quantity, measure} = props;

    return (
        <span>
            <FormattedMessage id={`enum.packaging.${packaging}`}/> {quantity} <FormattedMessage
            id={`enum.quantity-measure.${measure}`}/>
        </span>
    );
};
