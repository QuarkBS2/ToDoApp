import { configureStore } from "@reduxjs/toolkit";
import todosReducer from '../components/todosSlice';
import metricsSlice from "../components/metricsSlice";

export const store = configureStore({
    reducer: {
        todos: todosReducer,
        metrics: metricsSlice,
    },
});

export default store;