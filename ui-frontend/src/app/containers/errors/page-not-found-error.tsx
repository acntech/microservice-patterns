import * as React from 'react';
import { Component, ReactNode } from 'react';
import { injectIntl } from 'react-intl';
import { Button, Container } from 'semantic-ui-react';
import { GenericError } from '../../components';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

type ComponentProps = InjectedIntlProps;

class PageNotFoundErrorContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const homeButtonText = this.props.intl.formatMessage({id: 'button.home.text'});

        return (
            <Container className="error page-not-found-error">
                <GenericError icon="blind" header={{id: 'error.page-not-found.header.title'}} content={{id: 'error.page-not-found.content.text'}}>
                    <a href="/"><Button primary icon="home" size="mini" content={homeButtonText} /></a>
                </GenericError>
            </Container>
        );
    }
}

const IntlPageNotFoundErrorContainer = injectIntl(PageNotFoundErrorContainer);

export { IntlPageNotFoundErrorContainer as PageNotFoundErrorContainer };