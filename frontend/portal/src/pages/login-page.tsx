import * as React from 'react';
import {FC, ReactElement} from 'react';
import {Container, Form, Grid, Header, List, Message, Segment} from 'semantic-ui-react';
import {LoadingIndicatorFragment} from "../fragments";

export const LoginPage: FC = (): ReactElement => {

    if (true) {
        return <LoadingIndicatorFragment/>;
    } else {
        return (
            <Container>
                <Segment basic>
                    <Grid textAlign="center">
                        <Grid.Row>
                            <Grid.Column className="login" textAlign="center">
                                <Header as="h1">Login</Header>
                                <Form size="large" onSubmit={() => {
                                }} error={false}
                                      warning={false}>
                                    <Segment basic>
                                        <Form.Input fluid icon="user" iconPosition="left" placeholder='Customer ID'
                                                    value={{}} onChange={() => {}}/>
                                        <Form.Button primary fluid size="large">Login</Form.Button>
                                        <Message error icon="ban" content={{}}/>
                                        <Message warning icon="warning sign"
                                                 content='customer-id might come from the login cookie'/>
                                    </Segment>
                                </Form>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <Grid.Column className="login" textAlign="left">
                                <List divided selection>
                                    {
                                        [].map((customerEntity, index) => {
                                            const {customerId, firstName, lastName} = customerEntity;
                                            const active = customerId === '';

                                            return (
                                                <List.Item key={index} className="login" active={active}
                                                           onClick={() => {}}>
                                                    <List.Content>
                                                        <List.Header><h3>{firstName} {lastName}</h3></List.Header>
                                                        <List.Description>Customer
                                                            ID: {customerId}</List.Description>
                                                    </List.Content>
                                                </List.Item>
                                            );
                                        })
                                    }
                                </List>
                            </Grid.Column>
                        </Grid.Row>
                    </Grid>
                </Segment>
            </Container>
        );
    }
};
