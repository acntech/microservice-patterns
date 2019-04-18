import * as React from 'react';
import { Component, ReactNode } from 'react';
import { Header, Icon, Segment } from 'semantic-ui-react'
import { Link } from 'react-router-dom';

interface ComponentProps {
    title?: string;
    subtitle?: string;
}

class PrimaryHeaderComponent extends Component<ComponentProps> {

    public render(): ReactNode {
        const browserTitle = this.browserTitle();
        const headerTitle = this.headerTitle();

        document.title = browserTitle;

        return (
            <Segment basic className="primary-header">
                <Header as='h1'>
                    <Link to="/">
                        <Icon name='box' />{headerTitle}
                    </Link>
                </Header>
            </Segment>
        );
    }

    private browserTitle() {
        const { title, subtitle } = this.props;
        const formattedHeaderTitle = title ? title : 'Orders';
        if (subtitle) {
            return `${subtitle} - ${formattedHeaderTitle}`;
        } else {
            return formattedHeaderTitle;
        }
    }

    private headerTitle() {
        const { title, subtitle } = this.props;
        const formattedHeaderTitle = title ? title : 'Orders';
        if (subtitle) {
            return `${formattedHeaderTitle} - ${subtitle}`;
        } else {
            return formattedHeaderTitle;
        }
    }
}

export { PrimaryHeaderComponent as PrimaryHeader };