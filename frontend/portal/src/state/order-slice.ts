import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {
    ClientError,
    ClientResponse,
    CreateOrderItemParams,
    CreateOrderParams,
    CreateOrderThenOrderItemParams,
    DeleteOrderItemParams,
    DeleteOrderParams,
    ErrorPayload,
    GetOrderParams,
    Order,
    SetOrderParams,
    State,
    Status,
    UncategorizedStateError,
    UpdateOrderItemParams,
    UpdateOrderParams
} from "../types";
import {RestClient} from "../core/client";
import {RootState} from "./store";

const initialState: State<Order> = {
    status: Status.PENDING
}

const setOrder = createAsyncThunk<Order | undefined, SetOrderParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('order/set', async (params) => {
    const {order} = params;
    return order;
});

const getOrder = createAsyncThunk<ClientResponse<Order>, GetOrderParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('order/get', async (params, thunkAPI) => {
    const {orderId} = params;
    try {
        return await RestClient.GET<Order>(`/api/orders/${orderId}`);
    } catch (e) {
        if (e instanceof ClientError) {
            const error = e as ClientError;
            if (!!error.response) {
                return thunkAPI.rejectWithValue(error.response);
            }
        }
        throw e;
    }
});

const createOrder = createAsyncThunk<ClientResponse<Order>, CreateOrderParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('order/create', async (params, thunkAPI) => {
    const {body} = params;
    try {
        return await RestClient.POST<Order>("/api/orders", body);
    } catch (e) {
        if (e instanceof ClientError) {
            const error = e as ClientError;
            if (!!error.response) {
                return thunkAPI.rejectWithValue(error.response);
            }
        }
        throw e;
    }
});

const updateOrder = createAsyncThunk<ClientResponse<Order>, UpdateOrderParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('order/update', async (params, thunkAPI) => {
    const {orderId} = params;
    try {
        return await RestClient.PUT<Order>(`/api/orders/${orderId}`);
    } catch (e) {
        if (e instanceof ClientError) {
            const error = e as ClientError;
            if (!!error.response) {
                return thunkAPI.rejectWithValue(error.response);
            }
        }
        throw e;
    }
});

const deleteOrder = createAsyncThunk<ClientResponse<Order>, DeleteOrderParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('order/delete', async (params, thunkAPI) => {
    const {orderId} = params;
    try {
        return await RestClient.DELETE<Order>(`/api/orders/${orderId}`);
    } catch (e) {
        if (e instanceof ClientError) {
            const error = e as ClientError;
            if (!!error.response) {
                return thunkAPI.rejectWithValue(error.response);
            }
        }
        throw e;
    }
});

const createOrderItem = createAsyncThunk<ClientResponse<Order>, CreateOrderItemParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('orderItem/create', async (params, thunkAPI) => {
    const {orderId, body} = params;
    try {
        return await RestClient.POST<Order>(`/api/orders/${orderId}/items`, body);
    } catch (e) {
        if (e instanceof ClientError) {
            const error = e as ClientError;
            if (!!error.response) {
                return thunkAPI.rejectWithValue(error.response);
            }
        }
        throw e;
    }
});

const updateOrderItem = createAsyncThunk<ClientResponse<Order>, UpdateOrderItemParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('orderItem/update', async (params, thunkAPI) => {
    const {itemId, body} = params;
    try {
        return await RestClient.PUT<Order>(`/api/items/${itemId}`, body);
    } catch (e) {
        if (e instanceof ClientError) {
            const error = e as ClientError;
            if (!!error.response) {
                return thunkAPI.rejectWithValue(error.response);
            }
        }
        throw e;
    }
});

const deleteOrderItem = createAsyncThunk<ClientResponse<Order>, DeleteOrderItemParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('orderItem/delete', async (params, thunkAPI) => {
    const {itemId} = params;
    try {
        return await RestClient.DELETE<Order>(`/api/items/${itemId}`);
    } catch (e) {
        if (e instanceof ClientError) {
            const error = e as ClientError;
            if (!!error.response) {
                return thunkAPI.rejectWithValue(error.response);
            }
        }
        throw e;
    }
});

const createOrderThenOrderItem = createAsyncThunk<ClientResponse<Order>, CreateOrderThenOrderItemParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('order/create/orderItem/create', async (params, thunkAPI) => {
    const {orderBody, orderItemBody} = params;
    try {
        const response = await RestClient.POST<Order>("/api/orders", orderBody);
        const {body: order} = response;
        if (!order) {
            return thunkAPI.rejectWithValue({
                headers: {},
                status: 3
            });
        }
        const {orderId} = order;
        return await RestClient.POST<Order>(`/api/orders/${orderId}/items`, orderItemBody);
    } catch (e) {
        if (e instanceof ClientError) {
            const error = e as ClientError;
            if (!!error.response) {
                return thunkAPI.rejectWithValue(error.response);
            }
        }
        throw e;
    }
});

const slice = createSlice({
    name: 'order',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(setOrder.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(setOrder.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload;
            })
            .addCase(setOrder.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
            .addCase(getOrder.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(getOrder.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body;
            })
            .addCase(getOrder.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
            .addCase(createOrder.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(createOrder.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body;
            })
            .addCase(createOrder.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
            .addCase(updateOrder.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(updateOrder.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body;
            })
            .addCase(updateOrder.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
            .addCase(deleteOrder.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(deleteOrder.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body;
            })
            .addCase(deleteOrder.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
            .addCase(createOrderItem.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(createOrderItem.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body;
            })
            .addCase(createOrderItem.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
            .addCase(updateOrderItem.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(updateOrderItem.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body;
            })
            .addCase(updateOrderItem.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
            .addCase(deleteOrderItem.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(deleteOrderItem.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body;
            })
            .addCase(deleteOrderItem.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
            .addCase(createOrderThenOrderItem.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(createOrderThenOrderItem.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body;
            })
            .addCase(createOrderThenOrderItem.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
    }
});

const orderReducer = slice.reducer;

const orderSelector = (state: RootState) => state.order;

export {
    setOrder,
    getOrder,
    createOrder,
    updateOrder,
    deleteOrder,
    createOrderItem,
    updateOrderItem,
    deleteOrderItem,
    createOrderThenOrderItem,
    orderReducer,
    orderSelector
}
