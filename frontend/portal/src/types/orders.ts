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

export interface OrderItem {
    itemId: string;
    orderId: string;
    productId: string;
    quantity: number;
    status: OrderItemStatus;
    created: string;
    modified: string;
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
