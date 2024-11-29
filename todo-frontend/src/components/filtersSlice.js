import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  text: '',
  sortBy: '',
  direction: '',
  page: 1,
};

/**
 * Represents a slice of filters in the Redux store.
 *
 * @typedef {Object} FilterSlice
 * @property {string} name - The name of the slice.
 * @property {Object} initialState - The initial state of the slice.
 * @property {Object} reducers - The reducers for the slice.
 * @property {Function} reducers.setFilters - The reducer function for setting filters.
 */
const filterSlice = createSlice({
  name: 'filters',
  initialState,
  reducers: {
    setFilters(state, action) {
      return { ...state, ...action.payload };
    },
  },
});

export const { setFilters } = filterSlice.actions;
export default filterSlice.reducer;