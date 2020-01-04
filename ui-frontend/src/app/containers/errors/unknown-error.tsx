import * as React from 'react';
import { Component, ReactNode } from 'react';
import { injectIntl } from 'react-intl';
import { Button, Container } from 'semantic-ui-react';
import { GenericError } from '../../components/error';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

type ComponentProps = InjectedIntlProps;

class UnknownErrorContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const homeButtonText = this.props.intl.formatMessage({id: 'button.home.text'});

        return (
            <Container className="error unknown-error">
                <GenericError icon="frown outline" header={{id: 'error.unknown.header.title'}} content={{id: 'error.unknown.content.text'}}>
                    <a href="/"><Button primary icon="home" size="mini" content={homeButtonText} /></a>
                </GenericError>
            </Container>
        );
    }
}

const IntlUnknownErrorContainer = injectIntl(UnknownErrorContainer);

export { IntlUnknownErrorContainer as UnknownErrorContainer };