import { ActionType, Customer, CustomerAction, CustomerState, EntityType, FindCustomersAction, FindCustomersActionType, GetCustomerAction, GetCustomerActionType } from '../../models';
import { initialCustomerState } from '../store/initial-state';

export const reducer = (state: CustomerState = initialCustomerState, action: CustomerAction): CustomerState => {
    switch (action.type) {
        case GetCustomerActionType.LOADING:
        case GetCustomerActionType.SUCCESS:
        case GetCustomerActionType.ERROR:
            return get(state, action);
        case FindCustomersActionType.LOADING:
        case FindCustomersActionType.SUCCESS:
        case FindCustomersActionType.ERROR:
            return find(state, action);
        default:
            return state;
    }
};

const get = (state: CustomerState = initialCustomerState, action: GetCustomerAction): CustomerState => {
    switch (action.type) {
        case GetCustomerActionType.LOADING: {
            const {customers} = state;
            const {loading} = action;
            return {...initialCustomerState, customers: customers, loading: loading};
        }

        case GetCustomerActionType.SUCCESS: {
            let {customers} = state;
            const {payload} = action;

            if (payload) {
                customers = replaceOrAppend(customers, payload);
            }

            return {...initialCustomerState, customers: customers};
        }

        case GetCustomerActionType.ERROR: {
            const {customers} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.CUSTOMERS, actionType: ActionType.GET};
            return {...initialCustomerState, customers: customers, error: error};
        }

        default: {
            return state;
        }
    }
};

const find = (state: CustomerState = initialCustomerState, action: FindCustomersAction): CustomerState => {
    switch (action.type) {
        case FindCustomersActionType.LOADING: {
            const {customers} = state;
            const {loading} = action;
            return {...initialCustomerState, customers: customers, loading: loading};
        }

        case FindCustomersActionType.SUCCESS: {
            let {customers} = state;
            const {payload} = action;

            if (payload) {
                payload.forEach(customer => {
                    customers = replaceOrAppend(customers, customer);
                });
            }

            return {...initialCustomerState, customers: customers};
        }

        case FindCustomersActionType.ERROR: {
            const {customers} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.CUSTOMERS, actionType: ActionType.FIND};
            return {...initialCustomerState, customers: customers, error: error};
        }

        default: {
            return state;
        }
    }
};

const replaceOrAppend = (customers: Customer[], customer: Customer) => {
    const index = customers.map(c => c.customerId).indexOf(customer.customerId);

    if (index !== -1) {
        customers[index] = customer;
    } else {
        customers = customers.concat(customer);
    }

    return customers;
};