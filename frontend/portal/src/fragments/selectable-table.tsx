import React, { FC } from 'react';
import { Table } from 'semantic-ui-react';

function objectValues<T>(obj: T) {
    return Object.keys(obj).map((objKey) => obj[objKey as keyof T]);
}

function objectKeys<T>(obj: T) {
    return Object.keys(obj).map((objKey) => objKey as keyof T);
}

type PrimitiveType = string | symbol | number | boolean;

function isPrimitive(value: any): value is PrimitiveType {
    return (
        typeof value === "string" ||
        typeof value === "number" ||
        typeof value === "boolean" ||
        typeof value === "symbol"
    );
}

type TableHeaders<T> = Record<keyof T, string>;

interface Props<T> {
    headers: TableHeaders<T>;
    items: T[];
}

function SelectableTable<T>(props: Props<T>): JSX.Element {
    const { headers, items } = props;

    const renderRow = (item: T, index: number) => (
        <Table.Row key={index}>
            {objectKeys(item).map((itemProp, index) => (
                <Table.Cell key={index}>{isPrimitive(item[itemProp]) ? item[itemProp] : ""}</Table.Cell>
            ))}
        </Table.Row>
    );
    
    const renderHeader = (header: string, index: number) => (
        <Table.HeaderCell key={index}>{header}</Table.HeaderCell>
    );

    return (
        <Table celled selectable>
            <Table.Header>
                <Table.Row>
                    {objectValues(headers).map(renderHeader)}
                </Table.Row>
            </Table.Header>

            <Table.Body>
                {items.map(renderRow)}
            </Table.Body>
        </Table>
    );
}

export { SelectableTable };
