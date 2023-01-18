export enum Currency {
    USD = '$',
    EUR = '€',
    GBP = '£',
}

export interface Product {
    productId: string;
    name: string;
    description: string;
    stock: number;
    price: number;
    currency: Currency;
    created: string;
    modified: string;
}

export interface ProductQuery {
    name: string;
}

