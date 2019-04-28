import * as React from 'react';
import { Component, ReactNode } from 'react';
import { Container } from 'semantic-ui-react';

import { LoadingSegment, PrimaryHeader, SecondaryHeader } from '../';

class LoadingIndicatorComponent extends Component<{}> {

    public render(): ReactNode {
        return (
            <Container className='loading-indicator'>
                <PrimaryHeader />
                <SecondaryHeader />
                <LoadingSegment />
            </Container>
        );
    }
}

export { LoadingIndicatorComponent as LoadingIndicator };