import {FC, ReactElement} from 'react';
import {Container, Spinner} from "react-bootstrap";

export const LoadingPage: FC = (): ReactElement => {
    return (
        <Container as="main">
            <Spinner animation="border" role="status">
                <span className="visually-hidden">Loading...</span>
            </Spinner>
        </Container>
    );
}
