import {ReactNode} from "react";

export type NotificationSeverity = 'INFO' | 'WARNING' | 'ERROR' | 'SUCCESS' | 'FAILED';
export type NotificationContent = string | ReactNode;

export interface Notification {
    id: string;
    severity: NotificationSeverity;
    title: string;
    content?: NotificationContent;
    permanent?: boolean;
    created: string;
}
