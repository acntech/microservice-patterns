import React, {FC, ReactElement} from 'react';
import {Button, ButtonGroup} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faMinus, faPlus} from "@fortawesome/free-solid-svg-icons";

export interface AmountSelectorProps extends React.HTMLAttributes<HTMLElement> {
    amount: number;
    onIncrease: () => void;
    onDecrease: () => void;
}

export const AmountSelector: FC<AmountSelectorProps> = (props): ReactElement => {
    const {hidden, amount, onIncrease, onDecrease} = props;

    return (
        <ButtonGroup hidden={hidden}>
            <Button variant="secondary" size="sm" onClick={onIncrease}>
                <FontAwesomeIcon icon={faPlus}/>
            </Button>
            <Button variant="light" size="sm" disabled={true}>{amount}</Button>
            <Button variant="secondary" size="sm" onClick={onDecrease}>
                <FontAwesomeIcon icon={faMinus}/>
            </Button>
        </ButtonGroup>
    );
}
