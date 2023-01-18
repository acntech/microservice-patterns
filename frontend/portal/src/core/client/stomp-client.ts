import { Client, Frame, Message, StompSubscription } from '@stomp/stompjs';

export interface StompClientConfig {
    path: string
    onConnect: (receipt?: Frame) => void;
    onDisconnect: () => void;
    onCommunicationError: (error: Frame) => void;
    onConnectionError: (event: Event) => void;
    reconnectDelay?: number,
    heartbeatIncoming?: number;
    heartbeatOutgoing?: number;
    connectionTimeout?: number;
    debug?: (message: string) => void;
}

const defaultStompClientConfig = {
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    connectionTimeout: 0,
    debug: () => { }
}

export type StompMessageHandler<T = any> = (message?: T) => void;

export type StompClientState = 'IDLE' | 'PENDING' | 'CONNECTED' | 'DISCONNECTED';

export class StompClient<T = any> {

    private client: Client;
    private subscription?: StompSubscription;

    public connect() {
        this.client.activate();
    }

    public disconnect() {
        this.client.deactivate();
    }

    public connected(): boolean {
        return this.client.connected;
    }

    public subscribe(destination: string, handler: StompMessageHandler<T>, headers?: any) {
        if (!this.subscription) {
            const callback = function (message: Message) {
                const { body } = message;
                if (body) {
                    try {
                        const message: T = JSON.parse(body);
                        handler(message);
                    } catch (e) {
                        handler();
                    }
                } else {
                    handler();
                }
            }
            this.subscription = this.client.subscribe(destination, callback, headers);
        } else {
            const { id } = this.subscription;
            console.warn(`STOMP CLIENT: Already subscribed to destination ${id}`);
        }
        return this;
    }

    public unsubscribe() {
        if (this.subscription) {
            const { id } = this.subscription;
            this.subscription.unsubscribe();
        } else {
            console.warn('STOMP CLIENT: Not subscribed to any destination');
        }
    }

    public send(destination: string, message: any) {
        if (this.connected()) {
            const body = JSON.stringify(message);
            this.client.publish({ destination, body })
        } else {
            throw new Error('Send error: STOMP CLIENT is disconnected')
        }
    }

    private static parseProtocol(): string {
        const isSecure = /^https/.test(window.location.protocol);
        return isSecure ? 'wss' : 'ws';
    }

    private static parsePort(): string {
        return window.location.port ? `:${window.location.port}` : '';
    }

    private static parseUrl(path: string): string {
        const protocol = StompClient.parseProtocol();
        const port = StompClient.parsePort();
        return `${protocol}://${window.location.hostname}${port}${path}`;
    }

    constructor(props: StompClientConfig) {
        const config = { ...defaultStompClientConfig, ...props }
        const { path, connectionTimeout, reconnectDelay, heartbeatIncoming, heartbeatOutgoing, onConnect, onDisconnect, onCommunicationError, onConnectionError, debug } = config;
        const url = StompClient.parseUrl(path);
        this.client = new Client({
            brokerURL: url,
            debug: debug,
            connectionTimeout: connectionTimeout,
            reconnectDelay: reconnectDelay,
            heartbeatIncoming: heartbeatIncoming,
            heartbeatOutgoing: heartbeatOutgoing,
        });
        this.client.onConnect = onConnect;
        this.client.onDisconnect = onDisconnect;
        this.client.onStompError = onCommunicationError;
        this.client.onWebSocketError = onConnectionError;
    }
}
