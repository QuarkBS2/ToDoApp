import { configureStore } from "@reduxjs/toolkit";
import todosReducer from '../components/todosSlice';
import metricsSlice from "../components/metricsSlice";
import filtersSlice from "../components/filtersSlice";

/**
 * The Redux store configuration.
 *
 * @typedef {Object} StoreConfig
 * @property {Object} reducer - The combined reducer object.
 */

/**
 * The Redux store.
 *
 * @type {StoreConfig}
 */
export const store = configureStore({
    reducer: {
        todos: todosReducer,
        metrics: metricsSlice,
        filters: filtersSlice,
    },
});

export default store;