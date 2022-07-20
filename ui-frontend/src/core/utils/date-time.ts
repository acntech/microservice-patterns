import moment from 'moment';

export const parseTime = (timestamp: string, locale: string): string => new Date(timestamp).toLocaleTimeString(locale);

export const parseDate = (timestamp: string, locale: string): string => new Date(timestamp).toLocaleDateString(locale);

export const parseDateTime = (timestamp: string, locale: string): string => {
    const time = parseTime(timestamp, locale);
    const date = parseDate(timestamp, locale);
    return `${date} ${time}`;
};

export const createDateTime = (): string => {
    return moment().format();
}