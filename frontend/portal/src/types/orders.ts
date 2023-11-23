import {Variant} from "react-bootstrap/types";

export enum OrderItemStatus {
    PENDING = 'PENDING',
    RESERVED = 'RESERVED',
    CONFIRMED = 'CONFIRMED',
    CANCELED = 'CANCELED',
    REJECTED = 'REJECTED',
    FAILED = 'FAILED'
}

export enum OrderStatus {
    PENDING = 'PENDING',
    CONFIRMED = 'CONFIRMED',
    CANCELED = 'CANCELED',
    REJECTED = 'REJECTED'
}

export interface CreateOrderItem {
    productId: string;
    quantity: number;
}

export interface UpdateOrderItem {
    productId: string;
    quantity: number;
}

export interface OrderItem {
    itemId: string;
    orderId: string;
    productId: string;
    quantity: number;
    status: OrderItemStatus;
    created: string;
    modified: string;
}

export interface EnrichedOrderItem extends OrderItem {
    name?: string;
    price?: number;
    currency?: string;
    statusColor: Variant;
}

export interface CreateOrder {
    customerId: string;
    name: string;
    description?: string;
}

export interface Order {
    orderId: string;
    customerId: string;
    name: string;
    description?: string;
    status: OrderStatus;
    items: OrderItem[];
    created: string;
    modified: string;
}

export interface EnrichedOrder extends Order {
    enrichedItems: EnrichedOrderItem[];
    statusColor: Variant;
}

export interface SetOrderParams {
    order: Order;
}

export interface GetOrderParams {
    orderId: string;
}

export interface CreateOrderParams {
    body: CreateOrder;
}

export interface CreateOrderItemParams {
    orderId: string;
    body: CreateOrderItem;
}

export interface UpdateOrderItemParams {
    itemId: string;
    body: UpdateOrderItem;
}
