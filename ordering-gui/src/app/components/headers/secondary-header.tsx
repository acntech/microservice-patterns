import * as React from 'react';
import { Component, FunctionComponent, ReactNode } from 'react';
import { Header, Segment } from 'semantic-ui-react';

import { Notifications } from '../';

interface ComponentProps {
    title?: string;
    subtitle?: string;
    children?: string | ReactNode;
}

class SecondaryHeaderComponent extends Component<ComponentProps> {

    public render(): ReactNode {
        const {title, subtitle, children} = this.props;

        return (
            <Segment basic className="secondary-header">
                <HeaderFragment title={title} subtitle={subtitle} children={children} />
                <Notifications />
            </Segment>
        );
    }
}

interface HeaderFragmentProps {
    title?: string;
    subtitle?: string;
    children?: string | ReactNode;
}

const HeaderFragment: FunctionComponent<HeaderFragmentProps> = (props) => {
    const {title, subtitle, children} = props;

    return (
        <Segment basic>
            <Header as='h2' floated='left'>{children ? children : title}</Header>
            {subtitle ? <Header.Subheader>{subtitle}</Header.Subheader> : null}
        </Segment>
    );
};

export { SecondaryHeaderComponent as SecondaryHeader };