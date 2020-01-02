import * as React from 'react';
import { Component, ErrorInfo, ReactNode } from 'react';
import { Container, Segment } from 'semantic-ui-react';

import { UnknownErrorContainer } from '../../containers';
import { ErrorCode, Message, SeverityType, Translation } from '../../models';

interface ComponentState {
    hasError?: boolean;
}

class ErrorHandler {

    public static handleError(defaultTitle: Translation, defaultContent?: Translation, error?: any): Message {
        console.log('ERROR', error);
        if (!error) {
            return {
                severity: SeverityType.ERROR,
                title: defaultTitle,
                content: {id: 'error.unknown.content.text'}
            };
        } else if (typeof error === 'string') {
            return {
                severity: SeverityType.ERROR,
                title: defaultTitle,
                content: error
            };
        } else {
            const {errorCode, body} = error && error.response;
            const {message} = body && body;
            const title = errorCode ? {id: 'error-code.' + errorCode + '.title.text'} : defaultTitle;
            switch (errorCode) {
                case ErrorCode.SERVER_REDIRECT_RESPONSE:
                    const content = {id: 'error-code.' + errorCode + '.content.text'};
                    return {
                        severity: SeverityType.WARNING,
                        title: title,
                        content: content
                    };
                default:
                    return {
                        severity: SeverityType.ERROR,
                        title: title,
                        content: message
                    };
            }
        }
    }
}

class ErrorHandlerProvider extends Component<{}, ComponentState> {

    constructor(props) {
        super(props);
        this.state = {hasError: false};
    }

    public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
        this.setState({hasError: true});
    }

    public render(): ReactNode {
        const {hasError} = this.state;
        const {children} = this.props;
        if (hasError) {
            return (
                <Container>
                    <Segment vertical>
                        <UnknownErrorContainer />
                    </Segment>
                </Container>
            );
        } else {
            return children;
        }
    }
}

export { ErrorHandler, ErrorHandlerProvider };