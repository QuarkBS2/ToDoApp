import {createSlice, createAsyncThunk} from '@reduxjs/toolkit';
import axios from 'axios';

const initialState = {
    items: [],
    status: 'idle',
    error: null,
}

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
    if(filters.sortBy){
        params.append('sortBy', filters.sortBy);
    }
    if(filters.direction) {
        params.append('direction', filters.direction);
    }
    
    const response = await axios.get(`/api/todos?${params.toString()}`);
    return response.data;
});

export const addTodo = createAsyncThunk('./addTodo', async(newTodo) =>{
    const response = await axios.post(`/api/todos`, newTodo);
    return response.data;
});

export const updateTodo = createAsyncThunk('todos/updateTodo', async (updatedTodo) => {
    const response = await axios.put(`/api/todos/${updatedTodo.id}`, updatedTodo);
    return response.data;
})

export const statusDone = createAsyncThunk('todos/statusDone', async (currentTodo) => {
    const response = await axios.post(`/api/todos/${currentTodo.id}/done`, currentTodo);
    return response.data;
})

export const statusUndone = createAsyncThunk('todos/statusUndone', async (currentTodo) => {
    const response = await axios.put(`/api/todos/${currentTodo.id}/undone`, currentTodo);
    return response.data;
})

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
            const index = state.items.findIndex(todo => todo.id === action.payload.id);
            if(index !== -1){
                state.items[index] = action.payload;
            }
        });
    }
});

export default TodosSlice.reducer;