import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {ClientError, ClientResponse, ErrorPayload, SessionContext, State, Status, UncategorizedStateError} from "../types";
import {RestClient} from "../core/client";
import {RootState} from "./store";

const getSession = createAsyncThunk<
    ClientResponse<SessionContext>, void, {
    rejectValue: ClientResponse<ErrorPayload>
}>('session/get', async (params, thunkAPI) => {
    try {
        return await RestClient.GET<SessionContext>("/api/session");
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

const initialState: State<SessionContext> = {
    status: Status.PENDING
}

const slice = createSlice({
    name: 'session',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(getSession.pending, (state) => {
                state.status = Status.LOADING;
            })
            .addCase(getSession.fulfilled, (state, action) => {
                state.status = Status.SUCCESS;
                state.data = action.payload.body;
            })
            .addCase(getSession.rejected, (state, action) => {
                state.status = Status.FAILED;
                if (!!action.payload) {
                    state.error = action.payload.body;
                } else {
                    state.error = UncategorizedStateError
                }
            })
    }
});

const sessionReducer = slice.reducer;

const sessionSelector = (state: RootState) => state.session;

export {getSession, sessionReducer, sessionSelector}
