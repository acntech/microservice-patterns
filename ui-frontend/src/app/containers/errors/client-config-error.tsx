import * as React from 'react';
import { Component, ReactNode } from 'react';
import { injectIntl } from 'react-intl';
import { Container } from 'semantic-ui-react';
import { GenericError } from '../../components';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

type ComponentProps = InjectedIntlProps;

class ClientConfigErrorContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        return (
            <Container className="error client-config-error">
                <GenericError icon="cog" header={{id: 'error.client-config.header.title'}} content={{id: 'error.client-config.content.text'}} />
            </Container>
        );
    }
}

const IntlClientConfigErrorContainer = injectIntl(ClientConfigErrorContainer);

export { IntlClientConfigErrorContainer as ClientConfigErrorContainer };