export interface Customer {
    customerId: string;
    firstName: string;
    lastName: string;
    address: string;
    created: string;
    modified: string;
}

export interface CustomerQuery {
    firstName: string;
    lastName: string;
}
