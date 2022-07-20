import React, {ReactElement} from 'react'
import {Container, Message, Segment} from 'semantic-ui-react';

export interface PageProps {
    error?: Error;
}

export const UncaughtErrorPage = (props: PageProps): ReactElement => {

    const message = props.error?.message || 'No error object was thrown';

    return (
        <Container>
            <Segment>
                <Message negative icon="heartbeat" header="Uncaught error occurred" content={message}/>
            </Segment>
        </Container>
    );
};
