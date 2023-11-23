import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {ClientResponse, ErrorPayload, SessionContext, State, Status} from "../types";
import {RestClient} from "../core/client";
import {RootState} from "./store";

const getSession = createAsyncThunk<ClientResponse<SessionContext>, void>('session/get', async () => {
    return await RestClient.GET<SessionContext>("/api/session");
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
                state.data = action.payload.body
            })
            .addCase(getSession.rejected, (state, action) => {
                state.status = Status.FAILED;
                state.error = action.error as ErrorPayload
            })
    }
});

const sessionReducer = slice.reducer;

const sessionSelector = (state: RootState) => state.session;

export {getSession, sessionReducer, sessionSelector}
