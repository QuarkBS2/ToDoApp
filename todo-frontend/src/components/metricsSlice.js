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

/**
 * Creates a slice for managing metrics state.
 *
 * @param {Object} options - The options for creating the slice.
 * @param {string} options.name - The name of the slice.
 * @param {Object} options.initialState - The initial state of the slice.
 * @param {Object} options.reducers - The reducers for the slice.
 * @param {Function} options.extraReducers - The extra reducers for the slice.
 * @returns {Object} - The created metrics slice.
 */
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