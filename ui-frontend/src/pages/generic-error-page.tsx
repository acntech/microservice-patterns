import React, {FC, ReactElement} from 'react'
import {Container} from 'semantic-ui-react';
import {ErrorPanelFragment} from "../fragments";

interface PageProps {
    errorId?: string;
    errorCode: string;
}

export const GenericErrorPage: FC<PageProps> = (props: PageProps): ReactElement => {
    const {errorId, errorCode} = props;

    return (
        <Container>
            <ErrorPanelFragment errorId={errorId} errorCode={errorCode}/>
        </Container>
    );
};
