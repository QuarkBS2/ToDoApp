import {createSlice, createAsyncThunk} from '@reduxjs/toolkit';
import axios from 'axios';

const initialState = {
    items: [],
    status: 'idle',
    error: null,
}

const REQUESTMAPPING = "/api/todos";

/**
 * Fetches todos based on the provided filters.
 *
 * @param {Object} filters - The filters to apply when fetching todos.
 * @param {string} filters.status - The status of the todos.
 * @param {string} filters.text - The text to search for in the todos.
 * @param {string} filters.priority - The priority of the todos.
 * @param {Array} filters.sortParams - The sorting parameters for the todos.
 * @param {string} filters.sortParams[].field - The field to sort by.
 * @param {string} filters.sortParams[].direction - The direction of the sorting.
 * @param {number} filters.page - The page number for pagination.
 * @returns {Promise<Array>} - A promise that resolves to an array of todos.
 */
export const fetchTodos = createAsyncThunk('./fetchTodos', async(filters) => {
    const params = new URLSearchParams();

    if (filters.status) {
        params.append('status', filters.status);
    } else {
        if (filters.status !== null && filters.status !== undefined){
            params.append('status', filters.status);
        }
    }
    if (filters.text){
        params.append('text', filters.text);
    }
    if (filters.priority) {
        params.append('priority', filters.priority)
    }

    if (filters.sortParams) {
        // If there is only one sort parameter
        if (filters.sortParams.length <= 1){
            const sortBy = filters.sortParams[0].field;
            const direction = filters.sortParams[0].direction;

            // Append the sortBy parameter to params
            params.append('sortBy', `${sortBy}`);

            // Append the appropriate direction parameter based on the sortBy field
            sortBy === "priority" ? params.append('directionPriority', `${direction}`) : params.append('directionDueDate', `${direction}`);
        } else {
            // If there are multiple sort parameters, set sortBy to "priorityDueDate"
            const sortBy = "priorityDueDate";

            // Check if the first sort parameter is "priority"
            if (filters.sortParams[0] === "priority") {
                // Append the directions for priority and due date accordingly
                params.append('directionPriority', filters.sortParams[0].direction);
                params.append('directionDueDate', filters.sortParams[1].direction);
            } else {
                // Append the directions in the opposite order
                params.append('directionPriority', filters.sortParams[1].direction);
                params.append('directionDueDate', filters.sortParams[0].direction);
            }

            // Append the sortBy parameter to params
            params.append('sortBy', `${sortBy}`);
        }
    }

    if(filters.page){
        params.append('page', filters.page);
    }

    const response = await axios.get(`${REQUESTMAPPING}?${params.toString()}`);
    return response.data;
});

/**
 * Adds a new todo item asynchronously.
 *
 * @param {Object} newTodo - The new todo item to be added.
 * @returns {Promise} A promise that resolves to the response data.
 */
export const addTodo = createAsyncThunk('./addTodo', async(newTodo) =>{
    const response = await axios.post(`${REQUESTMAPPING}`, newTodo);
    return response.data;
});

/**
 * Updates a todo item asynchronously.
 *
 * @param {Object} updatedTodo - The updated todo object.
 * @returns {Promise<Object>} - A promise that resolves to the updated todo data.
 */
export const updateTodo = createAsyncThunk('todos/updateTodo', async (updatedTodo) => {
    const response = await axios.put(`${REQUESTMAPPING}/${updatedTodo.id}`, updatedTodo);
    return response.data;
})

/**
 * Marks a todo as done.
 *
 * @param {Object} currentTodo - The todo object to mark as done.
 * @returns {Promise} A promise that resolves to the response data.
 */
export const statusDone = createAsyncThunk('todos/statusDone', async (currentTodo) => {
    const response = await axios.post(`${REQUESTMAPPING}/${currentTodo.id}/done`, currentTodo);
    return response.data;
})

/**
 * Updates the status of a todo item to "undone" asynchronously.
 *
 * @param {Object} currentTodo - The todo item to update.
 * @returns {Promise} A promise that resolves to the updated todo item.
 */
export const statusUndone = createAsyncThunk('todos/statusUndone', async (currentTodo) => {
    const response = await axios.put(`${REQUESTMAPPING}/${currentTodo.id}/undone`, currentTodo);
    return response.data;
})

/**
 * Represents a slice of the todos state in the Redux store.
 *
 * @typedef {Object} TodosSlice
 * @property {string} name - The name of the slice.
 * @property {Object} initialState - The initial state of the slice.
 * @property {Object} reducers - The reducers for the slice.
 * @property {Function} extraReducers - The extra reducers for the slice.
 */
const TodosSlice = createSlice({
    name: 'todos',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
        .addCase(fetchTodos.pending, (state) => {
            state.status = 'loading';
        })
        .addCase(fetchTodos.fulfilled, (state, action) => {
            state.status = 'succeded';
            state.items = action.payload;

        })
        .addCase(fetchTodos.rejected, (state, action) => {
            state.status = 'failed';
            state.error = action.error.message;
        })
        .addCase(updateTodo.fulfilled, (state, action) => {
            console.log('updateTodo.fulfilled action.payload:', action.payload);
            console.log('state.items before update:', state.items);
            if (Array.isArray(state.items)) {
                const index = state.items.findIndex(todo => todo.id === action.payload.id);
                if (index !== -1) {
                    state.items[index] = action.payload;
                }
            } else {
                console.error('state.items is not an array:', state.items);
            }
        })
        .addCase(updateTodo.rejected, (state, action) => {
            state.status = 'badRequest';
            state.error = action.error.message;
        })
        .addCase(addTodo.rejected, (state, action) => {
            state.status = 'badRequest';
            state.error = action.error.message;
        });
    }
});

export default TodosSlice.reducer;