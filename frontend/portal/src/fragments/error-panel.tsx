import React, {FC, ReactElement} from 'react'
import {FormattedMessage, useIntl} from 'react-intl';
import {Message, Segment} from 'semantic-ui-react';
import {ErrorPayload} from '../types';

export interface FragmentProps {
    errorId?: string;
    errorCode: string;
}

export const mapErrorPayload = (error?: ErrorPayload): FragmentProps => {
    const defaultErrorCause = 'ACNTECH.TECHNICAL.COMMON.MISSING_ERROR_RESPONSE';
    let errorCause = error || {errorCode: defaultErrorCause};
    while (errorCause.cause) {
        errorCause = errorCause.cause;
    }
    return {
        errorId: errorCause.errorId,
        errorCode: errorCause.errorCode || defaultErrorCause
    }
};

export const ErrorPanelFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {errorId, errorCode} = props;

    const {formatMessage: t} = useIntl();
    const errorTitle = t({id: `error.${errorCode}.title`});
    const errorContent = t({id: `error.${errorCode}.content`});

    if (errorId) {
        return (
            <Segment basic>
                <Message attached negative icon="exclamation triangle" header={errorTitle} content={errorContent}/>
                <Message attached='bottom' negative>
                    <FormattedMessage id="error.generic.error-id" values={{errorId}}/>
                </Message>
            </Segment>
        );
    } else {
        return (
            <Segment basic>
                <Message attached negative icon="exclamation triangle" header={errorTitle} content={errorContent}/>
            </Segment>
        );
    }
};