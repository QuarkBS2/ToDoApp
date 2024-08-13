import React from "react";
import '@testing-library/jest-dom'
import { fireEvent, render, screen} from '@testing-library/react'
import { Provider } from "react-redux";
import store from "../app/store";
import TodoFilters from "../components/filters";

beforeAll(() => {
    global.matchMedia = global.matchMedia || function () {
        return {
          addListener: jest.fn(),
          removeListener: jest.fn(),
        };
      };
})

test('Renders filter component', () => {
    render(<Provider store={store}><TodoFilters /></Provider>)
    expect(screen.getByText('Search by word')).toBeInTheDocument();
    expect(screen.getByText('Filter by status')).toBeInTheDocument();
    expect(screen.getByText('Filter by priority')).toBeInTheDocument();
    expect(screen.getByText('Apply filters')).toBeInTheDocument();
});
