export interface Product {
    productId: string;
    code: string;
    name: string;
    description: string;
    stock: number;
    packaging: string;
    quantity: number;
    measure: string;
    price: number;
    currency: string;
    created: string;
    modified: string;
}

export interface ProductQuery {
    name: string;
}

export interface Quantity {
    quantity: number;
    measure: string;
}

export interface Price {
    price: number;
    currency: string;
}
