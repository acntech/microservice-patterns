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
    let icon = "exclamation triangle";
    switch (status) {
        case 401:
            icon = "minus circle";
            break;
    }

    return (
        <Segment basic>
            <Message attached negative icon={icon} header={errorTitle} content={errorContent}/>
        </Segment>
    );
};