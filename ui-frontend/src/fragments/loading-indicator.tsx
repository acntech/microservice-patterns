import * as React from 'react';
import {FC, ReactElement} from 'react';
import {Container, Segment} from 'semantic-ui-react';

export const LoadingIndicatorFragment: FC = (): ReactElement => {
    return (
        <Container>
            <Segment loading>
                <div className="loading-indicator-body"/>
            </Segment>
        </Container>
    );
}
