import React, { FC, ReactElement } from 'react'
import { useIntl } from 'react-intl';
import { Container, Message, Segment } from 'semantic-ui-react';

export const NotFoundErrorPage: FC = (): ReactElement => {

  const { formatMessage: t } = useIntl();

  return (
    <Container>
      <Segment>
        <Message negative icon="blind" header={t({ id: 'page.error-not-found.title' })} content={t({ id: 'page.error-not-found.content' })} />
      </Segment>
    </Container>
  );
};
