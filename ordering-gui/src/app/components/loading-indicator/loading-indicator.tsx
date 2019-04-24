import * as React from 'react';
import { Component, ReactNode } from 'react';
import { Container, Segment } from 'semantic-ui-react'

import { PrimaryHeader, SecondaryHeader } from '../';

class LoadingIndicatorComponent extends Component<{}> {

    public render(): ReactNode {
        return (
            <Container className='loading-indicator'>
                <PrimaryHeader />
                <SecondaryHeader />
                <Segment loading>
                    <div className='loading-indicator-body' />
                </Segment>
            </Container>
        );
    }
}

export { LoadingIndicatorComponent as LoadingIndicator };