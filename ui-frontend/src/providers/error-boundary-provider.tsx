import React, { ReactElement, ReactNode } from 'react'
import { ErrorBoundary } from 'react-error-boundary'
import { UncaughtErrorPage } from '../pages';

export interface ErrorBoundaryProps {
    children: ReactNode;
}

const errorHandler = (error: Error, info: { componentStack: string }) => {

};

export const ErrorBoundaryProvider = (props: ErrorBoundaryProps): ReactElement => {
    return (
        <ErrorBoundary FallbackComponent={UncaughtErrorPage} onError={errorHandler}>{props.children}</ErrorBoundary>
    );
};