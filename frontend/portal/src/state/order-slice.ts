import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {
    ClientResponse,
    CreateOrderItemParams,
    CreateOrderParams,
    ErrorPayload,
    GetOrderParams,
    Order,
    SetOrderParams,
    State,
    Status,
    UpdateOrderItemParams
} from "../types";
import {RestClient} from "../core/client";
import {RootState} from "./store";

const initialState: State<Order> = {
    status: Status.PENDING
}

const setOrder = createAsyncThunk<Order, SetOrderParams>('order/set', async (params) => {
    const {order} = params;
    return order;
});

const getOrder = createAsyncThunk<ClientResponse<Order>, GetOrderParams>('order/get', async (params) => {
    const {orderId} = params;
    return await RestClient.GET<Order>(`/api/orders/${orderId}`);
});

const createOrder = createAsyncThunk<ClientResponse<Order>, CreateOrderParams>('order/create', async (params) => {
    const {body} = params;
    return await RestClient.POST<Order>("/api/orders", body);
});

const createOrderItem = createAsyncThunk<ClientResponse<Order>, CreateOrderItemParams>('orderItem/create', async (params) => {
    const {orderId, body} = params;
    return await RestClient.POST<Order>(`/api/orders/${orderId}/items`, body);
});

const updateOrderItem = createAsyncThunk<ClientResponse<Order>, UpdateOrderItemParams>('orderItem/update', async (params) => {
    const {itemId, body} = params;
    return await RestClient.PUT<Order>(`/api/items/${itemId}`, body);
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
                state.error = action.error as ErrorPayload;
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
                state.error = action.error as ErrorPayload;
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
                state.error = action.error as ErrorPayload;
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
                state.error = action.error as ErrorPayload;
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
                state.error = action.error as ErrorPayload;
            })
    }
});

const orderReducer = slice.reducer;

const orderSelector = (state: RootState) => state.order;

export {setOrder, getOrder, createOrder, createOrderItem, updateOrderItem, orderReducer, orderSelector}
