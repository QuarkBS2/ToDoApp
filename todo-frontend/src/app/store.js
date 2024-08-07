import { configureStore } from "@reduxjs/toolkit";
import todosReducer from '../components/todosSlice';

export const store = configureStore({
    reducer: {
        todos: todosReducer,
    },
});

export default store;