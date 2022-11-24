import React, {FC, ReactElement} from 'react'
import {FormattedMessage, useIntl} from 'react-intl';
import {Message} from 'semantic-ui-react';
import {ErrorPayload, SliceError} from '../types';

export const mapErrorPayload = (error: ErrorPayload): SliceError => {
    let errorCause = error;
    while (errorCause.cause) {
        errorCause = errorCause.cause;
    }
    return {
        errorId: errorCause.errorId,
        errorCode: errorCause.errorCode || 'ACNTECH.TECHNICAL.COMMON.MISSING_ERROR_RESPONSE'
    }
};

interface PanelProps {
    errorId?: string;
}

const ErrorIdPanelFragment: FC<PanelProps> = (props: PanelProps): ReactElement => {
    const {errorId} = props;
    if (errorId) {
        return (
            <Message attached='bottom' negative>
                <FormattedMessage id="error.generic.error-id" values={{errorId}}/>
            </Message>
        );
    } else {
        return <></>;
    }
};

interface FragmentProps {
    errorId?: string;
    errorCode: string;
}

export const ErrorPanelFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {errorId, errorCode} = props;

    const {formatMessage: t} = useIntl();
    const errorTitle = t({id: `error.${errorCode}.title`});
    const errorContent = t({id: `error.${errorCode}.content`});

    return (
        <>
            <Message attached negative icon="exclamation triangle" header={errorTitle} content={errorContent}/>
            <ErrorIdPanelFragment errorId={errorId} />
        </>
    );
};