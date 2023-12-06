import React, {FC, ReactElement} from 'react';
import {FormattedMessage} from "react-intl";

export interface QuantityPanelProps {
    packaging: string;
    quantity: number;
    measure: string;
}

export const QuantityPanel: FC<QuantityPanelProps> = (props): ReactElement => {
    const {packaging, quantity, measure} = props;

    return (
        <span>
            <FormattedMessage id={`enum.packaging.${packaging}`}/> {quantity} <FormattedMessage
            id={`enum.quantity-measure.${measure}`}/>
        </span>
    );
};
