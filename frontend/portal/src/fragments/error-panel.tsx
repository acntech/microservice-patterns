import React, {FC, ReactElement} from 'react'
import {FormattedMessage, useIntl} from 'react-intl';
import {Message, Segment} from 'semantic-ui-react';
import {ErrorPayload, PageResponse} from '../types';

export interface FragmentProps {
    errorId?: string;
    errorCode: string;
}

const mapErrorCode = (error?: PageResponse<ErrorPayload>): string => {
    const errorCode = error?.body?.errorCode
    if (errorCode) {
        return errorCode;
    } else if (error?.status === 401) {
        return 'ACNTECH.TECHNICAL.SECURITY.NOT_AUTHENTICATED';
    } else {
        return 'ACNTECH.TECHNICAL.COMMON.UNRECOGNIZED_ERROR';
    }
};

export const mapErrorPayload = (error?: PageResponse<ErrorPayload>): FragmentProps => {
    const errorCode = mapErrorCode(error);
    let errorCause = error?.body || {errorCode};
    while (errorCause.cause) {
        errorCause = errorCause.cause;
    }
    return {
        errorId: errorCause.errorId,
        errorCode: errorCause.errorCode || errorCode
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