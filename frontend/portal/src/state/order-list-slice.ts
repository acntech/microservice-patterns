import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {
    ClientError,
    ClientResponse,
    ErrorPayload,
    FindOrdersParams,
    Order,
    State,
    Status,
    UncategorizedStateError
} from "../types";
import {RestClient} from "../core/client";
import {RootState} from "./store";

const findOrders = createAsyncThunk<ClientResponse<Order[]>, FindOrdersParams, {
    rejectValue: ClientResponse<ErrorPayload>
}>('orders/find', async (params, thunkAPI) => {
    const {customerId, status} = params;
    let query = `customerId=${customerId}`
    if (!!status) {
        query += `&status=${status}`;
    }
    try {
        return await RestClient.GET<Order[]>(`/api/orders?${query}`);
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
                state.data = action.payload.body;
            })
            .addCase(findOrders.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
    }
});

const orderListReducer = slice.reducer;

const orderListSelector = (state: RootState) => state.orders;

export {findOrders, orderListReducer, orderListSelector}
