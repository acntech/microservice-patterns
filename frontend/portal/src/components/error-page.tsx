import React, {FC, ReactElement} from 'react'
import {FormattedMessage, useIntl} from 'react-intl';
import {Alert, Breadcrumb, Container} from "react-bootstrap";
import {Error} from '../types';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse} from "@fortawesome/free-solid-svg-icons";

export interface ErrorPageProps {
    error?: Error;
}

export const ErrorPage: FC<ErrorPageProps> = (props: ErrorPageProps): ReactElement => {
    const {status} = props.error || {status: 0};
    const {formatMessage: t} = useIntl();
    let icon = "exclamation triangle";
    switch (status) {
        case 401:
            icon = "minus circle";
            break;
    }

    return (
        <Container as="main">
            <Breadcrumb className="mb-3">
                <Breadcrumb.Item href="/">
                    <FontAwesomeIcon icon={faHouse}/>
                </Breadcrumb.Item>
                <Breadcrumb.Item active>
                    <FormattedMessage id="title.error"/>
                </Breadcrumb.Item>
            </Breadcrumb>
            <Alert variant="danger">
                <Alert.Heading><FormattedMessage id={`error.status-${status}.title`}/></Alert.Heading>
                <p><FormattedMessage id={`error.status-${status}.content`}/></p>
            </Alert>
        </Container>
    );
};