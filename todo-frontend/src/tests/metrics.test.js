import React from "react";
import '@testing-library/jest-dom'
import { render, screen} from '@testing-library/react'
import { Provider } from "react-redux";
import store from "../app/store";
import TodoMetrics from "../components/metrics";

beforeAll(() => {
    global.matchMedia = global.matchMedia || function () {
        return {
          addListener: jest.fn(),
          removeListener: jest.fn(),
        };
      };
})

const mockMetrics =  {
    avgTime: 14, avgTimeLow: 13, avgTimeMedium: 12, avgTimeHigh: 15
};

test ('Renders component metrics', () => {
    const component = render(<Provider store={store}><TodoMetrics metrics={mockMetrics}/></Provider>)
    expect(screen.getByText('Average time to finish tasks')).toBeInTheDocument();
    expect(screen.getByText('Average time to finish tasks by priority')).toBeInTheDocument();
});

test ('Renders given metrics', () => {
    const component = render(<Provider store={store}><TodoMetrics metrics={mockMetrics}/></Provider>)
    expect(screen.getByText('14')).toBeInTheDocument();
    expect(screen.getByText('13')).toBeInTheDocument();
    expect(screen.getByText('12')).toBeInTheDocument();
    expect(screen.getByText('15')).toBeInTheDocument();
});