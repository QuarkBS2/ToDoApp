import { render, screen } from '@testing-library/react';
import App from './App';
import { Provider } from "react-redux";
import store from './app/store';

beforeAll(() => {
    global.matchMedia = global.matchMedia || function () {
        return {
          addListener: jest.fn(),
          removeListener: jest.fn(),
        };
      };
})

test('renders learn react link', () => {
    const component = render(<Provider store={store}><App /></Provider>);
    expect(screen.getByText('To Do App')).toBeInTheDocument();
    
});
