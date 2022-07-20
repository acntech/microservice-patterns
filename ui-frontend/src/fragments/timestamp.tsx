import React, { ReactElement } from 'react'
import { Popup } from 'semantic-ui-react';
import { defaultTimeLocale } from '../core/locales';
import { parseDate, parseDateTime } from '../core/utils';

interface TimestampProps {
    timestamp: string;
}

export const Timestamp = (props: TimestampProps): ReactElement => {
    const { timestamp } = props;
    const date = parseDate(timestamp, defaultTimeLocale);
    const dateTime = parseDateTime(timestamp, defaultTimeLocale);
    return (
        <Popup position="left center" content={dateTime} trigger={<span>{date}</span>} />
    );
};