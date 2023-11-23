import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {ClientResponse, ErrorPayload, Product, State, Status} from "../types";
import {RestClient} from "../core/client";
import {RootState} from "./store";

const initialState: State<Product[]> = {
    status: Status.PENDING
}

const findProducts = createAsyncThunk<ClientResponse<Product[]>, void>('products/find', async () => {
    return await RestClient.GET<Product[]>("/api/products");
});

const slice = createSlice({
    name: 'products',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findProducts.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(findProducts.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body
            })
            .addCase(findProducts.rejected, (state, action) => {
                state.status = Status.FAILED;
                state.error = action.error as ErrorPayload
            })
    }
})

const productListReducer = slice.reducer;

const productListSelector = (state: RootState) => state.products;

export {findProducts, productListReducer, productListSelector}
