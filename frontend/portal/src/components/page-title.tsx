import React, {FC, ReactElement} from 'react';
import {FormattedMessage} from "react-intl";

export interface PageTitleProps {
    id: string;
}

export const PageTitle: FC<PageTitleProps> = (props: PageTitleProps): ReactElement => {
    const {id} = props;

    return (
        <h2 className="mb-3 mb-4">
            <FormattedMessage id={id}/>
        </h2>
    );
}
