import * as React from 'react';
import {Component, ReactNode} from 'react';
import {injectIntl} from 'react-intl';
import {Button, Container} from 'semantic-ui-react';

import {NotFoundError} from '../../components';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

type ComponentProps = InjectedIntlProps;

class PageNotFoundErrorContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const homeButtonText = this.props.intl.formatMessage({id: 'button.home.text'});

        return (
            <Container className="error error-not-found">
                <NotFoundError icon="blind" header={{id: 'error.page-not-found.header.title'}}
                               content={{id: 'error.page-not-found.content.text'}}>
                    <a href="/"><Button primary icon="home" size="mini" content={homeButtonText}/></a>
                </NotFoundError>
            </Container>
        );
    }
}

const IntlPageNotFoundErrorContainer = injectIntl(PageNotFoundErrorContainer);

export {IntlPageNotFoundErrorContainer as PageNotFoundErrorContainer};