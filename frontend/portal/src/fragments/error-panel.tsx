import React, {FC, ReactElement} from 'react'
import {FormattedMessage, useIntl} from 'react-intl';
import {Alert, Container} from "react-bootstrap";
import {Error} from '../types';

export interface FragmentProps {
    error?: Error;
}

export const ErrorPanelFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {status} = props.error || {status: 500};
    const {formatMessage: t} = useIntl();
    let icon = "exclamation triangle";
    switch (status) {
        case 401:
            icon = "minus circle";
            break;
    }

    return (
        <Container as="main">
            <Alert variant="danger">
                <Alert.Heading><FormattedMessage id={`error.status-${status}.title`}/></Alert.Heading>
                <p><FormattedMessage id={`error.status-${status}.content`}/></p>
            </Alert>
        </Container>
    );
};