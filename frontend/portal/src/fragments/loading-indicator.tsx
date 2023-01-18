import {FC, ReactElement} from 'react';
import {Segment} from 'semantic-ui-react';

export const LoadingIndicatorFragment: FC = (): ReactElement => {
    return (
        <Segment loading>
            <div className="loading-indicator-body"/>
        </Segment>
    );
}
