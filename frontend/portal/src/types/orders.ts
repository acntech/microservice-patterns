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

export interface CartItem {
    itemId: string;
    productId: string;
    code: string;
    status: OrderItemStatus;
    quantity: number;
    itemPrice: number;
    totalPrice: number;
    currency: string;
}

export interface Cart {
    orderId: string;
    status: OrderStatus;
    items: CartItem[];
    created: string;
    modified: string;
}

export interface SetOrderParams {
    order?: Order;
}

export interface GetOrderParams {
    orderId: string;
}

export interface FindOrdersParams {
    customerId: string;
    status?: OrderStatus;
}

export interface CreateOrderParams {
    body: CreateOrder;
}

export interface CreateOrderThenOrderItemParams {
    orderBody: CreateOrder;
    orderItemBody: CreateOrderItem;
}

export interface UpdateOrderParams {
    orderId: string;
}

export interface DeleteOrderParams {
    orderId: string;
}

export interface CreateOrderItemParams {
    orderId: string;
    body: CreateOrderItem;
}

export interface UpdateOrderItemParams {
    itemId: string;
    body: UpdateOrderItem;
}

export interface DeleteOrderItemParams {
    itemId: string;
}
