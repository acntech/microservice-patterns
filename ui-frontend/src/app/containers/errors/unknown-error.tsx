import * as React from 'react';
import {Component, ReactNode} from 'react';
import {injectIntl} from 'react-intl';
import {Button, Container, Message, Segment} from 'semantic-ui-react';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

type ComponentProps = InjectedIntlProps;

class UnknownErrorContainer extends Component<ComponentProps> {
    public render(): ReactNode {
        const headerText = this.props.intl.formatMessage({id: 'error.unknown.header.title'});
        const contentText = this.props.intl.formatMessage({id: 'error.unknown.content.text'});
        const homeButtonText = this.props.intl.formatMessage({id: 'button.home.text'});

        return (
            <Container className="error error-unknown">
                <Segment basic>
                    <Message negative icon="frown outline" header={headerText} content={contentText}/>
                    <a href="/"><Button primary icon="home" size="mini" content={homeButtonText}/></a>
                </Segment>
            </Container>
        );
    }
}

const IntlUnknownErrorContainer = injectIntl(UnknownErrorContainer);

export {IntlUnknownErrorContainer as UnknownErrorContainer};