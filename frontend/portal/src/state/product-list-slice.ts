import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {ClientError, ClientResponse, ErrorPayload, Product, State, Status, UncategorizedStateError} from "../types";
import {RestClient} from "../core/client";
import {RootState} from "./store";

const initialState: State<Product[]> = {
    status: Status.PENDING
}

const findProducts = createAsyncThunk<ClientResponse<Product[]>, void, {
    rejectValue: ClientResponse<ErrorPayload>
}>('products/find', async (params, thunkAPI) => {
    try {
        return await RestClient.GET<Product[]>("/api/products");
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
                state.data = action.payload.body;
            })
            .addCase(findProducts.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
    }
})

const productListReducer = slice.reducer;

const productListSelector = (state: RootState) => state.products;

export {findProducts, productListReducer, productListSelector}
