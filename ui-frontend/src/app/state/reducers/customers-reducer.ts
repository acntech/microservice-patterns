import { ActionType, Customer, CustomerAction, CustomerState, EntityType, FindCustomersAction, FindCustomersActionType, GetCustomerAction, GetCustomerActionType } from '../../models';
import { INITIAL_CUSTOMER_STATE } from '../store/initial-state';

export const reducer = (state: CustomerState = INITIAL_CUSTOMER_STATE, action: CustomerAction): CustomerState => {
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

const get = (state: CustomerState = INITIAL_CUSTOMER_STATE, action: GetCustomerAction): CustomerState => {
    switch (action.type) {
        case GetCustomerActionType.LOADING: {
            const {customers} = state;
            const {loading} = action;
            return {...INITIAL_CUSTOMER_STATE, customers: customers, loading: loading};
        }

        case GetCustomerActionType.SUCCESS: {
            let {customers} = state;
            const {payload} = action;

            if (payload) {
                customers = replaceOrAppend(customers, payload);
            }

            return {...INITIAL_CUSTOMER_STATE, customers: customers};
        }

        case GetCustomerActionType.ERROR: {
            const {customers} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.CUSTOMERS, actionType: ActionType.GET};
            return {...INITIAL_CUSTOMER_STATE, customers: customers, error: error};
        }

        default: {
            return state;
        }
    }
};

const find = (state: CustomerState = INITIAL_CUSTOMER_STATE, action: FindCustomersAction): CustomerState => {
    switch (action.type) {
        case FindCustomersActionType.LOADING: {
            const {customers} = state;
            const {loading} = action;
            return {...INITIAL_CUSTOMER_STATE, customers: customers, loading: loading};
        }

        case FindCustomersActionType.SUCCESS: {
            let {customers} = state;
            const {payload} = action;

            if (payload) {
                payload.forEach(customer => {
                    customers = replaceOrAppend(customers, customer);
                });
            }

            return {...INITIAL_CUSTOMER_STATE, customers: customers};
        }

        case FindCustomersActionType.ERROR: {
            const {customers} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.CUSTOMERS, actionType: ActionType.FIND};
            return {...INITIAL_CUSTOMER_STATE, customers: customers, error: error};
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