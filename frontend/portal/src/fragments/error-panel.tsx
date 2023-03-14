import React, {FC, ReactElement} from 'react'
import {useIntl} from 'react-intl';
import {Message, Segment} from 'semantic-ui-react';
import {Error} from '../types';

export interface FragmentProps {
    error?: Error;
}

export const ErrorPanelFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {status} = props.error || {status: 500};

    const {formatMessage: t} = useIntl();
    const errorTitle = t({id: `error.status-${status}.title`});
    const errorContent = t({id: `error.status-${status}.content`});

    return (
        <Segment basic>
            <Message attached negative icon="exclamation triangle" header={errorTitle} content={errorContent}/>
        </Segment>
    );
};