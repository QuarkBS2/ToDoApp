import {createSlice, createAsyncThunk} from '@reduxjs/toolkit';
import axios from 'axios';

const initialState = {
    items: [],
    status: 'idle',
    error: null,
}

export const getMetrics = createAsyncThunk('./getMetrics', async () => {
    const response = await axios.get(`/api/todos/metrics`);
    return response.data;
})

const MetricsSlice = createSlice({
    name: 'metrics',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
        .addCase(getMetrics.pending, (state) => {
            state.status = 'loading';
        })
        .addCase(getMetrics.fulfilled, (state, action) => {
            state.status = 'succeded';
            state.items = action.payload;

        })
        .addCase(getMetrics.rejected, (state, action) => {
            state.status = 'failed';
            state.error = action.error.message;
        });
    }
});

export default MetricsSlice.reducer;