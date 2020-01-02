import * as React from 'react';
import { Component, ReactNode } from 'react';
import { Container } from 'semantic-ui-react';

import { LoadingSegment, SecondaryHeader } from '../';

class LoadingIndicatorComponent extends Component<{}> {

    public render(): ReactNode {
        return (
            <Container className="loading-indicator">
                <SecondaryHeader />
                <LoadingSegment />
            </Container>
        );
    }
}

export { LoadingIndicatorComponent as LoadingIndicator };