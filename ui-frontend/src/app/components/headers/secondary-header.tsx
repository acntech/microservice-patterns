import * as React from 'react';
import {Component, FunctionComponent, ReactNode} from 'react';
import {FormattedMessage} from 'react-intl';
import {Header, Segment} from 'semantic-ui-react';

import {Notifications} from '../';

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
                <HeaderFragment title={title} subtitle={subtitle} children={children}/>
                <Notifications/>
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
            <TitleFragment title={title} children={children}/>
            <SubtitleFragment subtitle={subtitle}/>
        </Segment>
    );
};

const TitleFragment: FunctionComponent<HeaderFragmentProps> = (props) => {
    const {title, children} = props;
    if (children) {
        return <Header as="h2" floated="left">{children}</Header>;
    } else if (title) {
        return <Header as="h2" floated="left"><FormattedMessage id={title}/></Header>;
    } else {
        return null;
    }
};

const SubtitleFragment: FunctionComponent<HeaderFragmentProps> = (props) => {
    const {subtitle} = props;
    return (subtitle ? <Header.Subheader><FormattedMessage id={subtitle}/></Header.Subheader> : null);
};

export {SecondaryHeaderComponent as SecondaryHeader};