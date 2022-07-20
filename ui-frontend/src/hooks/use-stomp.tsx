import { useCallback, useEffect, useRef, useState } from 'react'

import { StompClient, StompClientState } from '../core/client';

export type StompState = StompClientState;
export interface UseStompProps {
  path: string;
  destination: string;
  heartbeatIncoming?: number;
  heartbeatOutgoing?: number;
}

export interface UseStompHook<T> {
  doPending: () => void;
  doConnect: () => void;
  doSubscribe: () => void;
  state: StompState;
  message?: T;
  sendMessage: (message: any) => void;
}

export const useStomp = <T extends any>(props: UseStompProps): UseStompHook<T> => {

  const { destination } = props;
  const [connect, setConnect] = useState<boolean>(false);
  const [subscribe, setSubscribe] = useState<boolean>(false);
  const [state, setState] = useState<StompState>('IDLE');
  const [message, setMessage] = useState<T>();
  const stompClientRef = useRef<StompClient<T>>();

  const doPending = useCallback(() => {
    setState('PENDING');
  }, []);

  const doConnect = useCallback(() => {
    setConnect(true);
  }, []);

  const doSubscribe = useCallback(() => {
    setSubscribe(true);
  }, []);

  const onConnect = useCallback(() => {
    setState('CONNECTED');
  }, []);

  const onDisconnect = useCallback(() => {
    setConnect(false);
    setSubscribe(false);
    setState('DISCONNECTED');
  }, []);

  const onCommunicationError = useCallback(() => {
    setConnect(false);
    setSubscribe(false);
    setState('DISCONNECTED');
  }, []);

  const onConnectionError = useCallback(() => {
    setConnect(false);
    setSubscribe(false);
    setState('DISCONNECTED');
  }, []);

  const onMessage = useCallback((message?: T) => {
    setMessage(message);
  }, []);

  useEffect(() => {
    if (state !== 'CONNECTED' && connect) {
      if (!stompClientRef.current) {
        stompClientRef.current = new StompClient<T>({ ...props, onConnect, onDisconnect, onCommunicationError, onConnectionError });
      }
      stompClientRef.current.connect();
    }
  }, [connect]);

  useEffect(() => {
    if (state === 'CONNECTED' && subscribe && stompClientRef.current) {
      stompClientRef.current.subscribe(destination, onMessage);
    }
  }, [subscribe]);

  const sendMessage = useCallback((message: any) => {
    if (state === 'CONNECTED' && stompClientRef.current) {
      stompClientRef.current.send(destination, message)
    } else {
      throw new Error('Send error: StompClient is disconnected')
    }
  }, []);

  return { doPending, doConnect, doSubscribe, state, message, sendMessage };
};
