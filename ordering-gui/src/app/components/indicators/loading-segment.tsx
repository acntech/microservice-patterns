import * as React from 'react';
import { Component, ReactNode } from 'react';
import { Segment } from 'semantic-ui-react';

class LoadingSegmentComponent extends Component<{}> {

    public render(): ReactNode {
        return (
            <Segment loading>
                <div className='loading-indicator-body' />
            </Segment>
        );
    }
}

export { LoadingSegmentComponent as LoadingSegment };