import * as React from 'react';
import { Component, ReactNode } from 'react';
import { injectIntl } from 'react-intl';
import { Message, Segment } from 'semantic-ui-react';
import { Translation } from '../../models/types';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

interface ComponentParamProps {
    icon: string;
    header: Translation;
    content: Translation;
    children?: ReactNode;
}

type ComponentProps = ComponentParamProps & InjectedIntlProps;

class NotFoundErrorComponent extends Component<ComponentProps> {

    public render(): ReactNode {
        const {icon, header, content, children} = this.props;
        const headerText = this.props.intl.formatMessage({id: header.id, defaultMessage: header.defaultMessage}, header.values);
        const contentText = this.props.intl.formatMessage({id: content.id, defaultMessage: content.defaultMessage}, content.values);

        return (
            <Segment basic className="error error-not-found">
                <Message negative icon={icon} header={headerText} content={contentText} />
                {children}
            </Segment>
        );
    }
}

const IntlNotFoundErrorComponent = injectIntl(NotFoundErrorComponent);

export { IntlNotFoundErrorComponent as NotFoundError };