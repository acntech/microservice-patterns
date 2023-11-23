import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {ClientResponse, ErrorPayload, Order, State, Status} from "../types";
import {RestClient} from "../core/client";
import {RootState} from "./store";

const findOrders = createAsyncThunk<ClientResponse<Order[]>, void>('orders/find', async () => {
    return await RestClient.GET<Order[]>("/api/orders");
});

const initialState: State<Order[]> = {
    status: Status.PENDING
}

const slice = createSlice({
    name: 'orders',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findOrders.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(findOrders.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body
            })
            .addCase(findOrders.rejected, (state, action) => {
                state.status = Status.FAILED;
                state.error = action.error as ErrorPayload
            })
    }
});

const orderListReducer = slice.reducer;

const orderListSelector = (state: RootState) => state.orders;

export {findOrders, orderListReducer, orderListSelector}
