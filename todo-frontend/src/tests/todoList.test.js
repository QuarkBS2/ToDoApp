import React from "react";
import '@testing-library/jest-dom'
import { render, screen, fireEvent} from '@testing-library/react'
import TodoList from "../components/todoList";
import { Provider } from "react-redux";
import store from "../app/store";
import { prettyDOM} from '@testing-library/dom'
import { Checkbox } from "antd";

const original = window.location;

beforeAll(() => {
    global.matchMedia = global.matchMedia || function () {
        return {
          addListener: jest.fn(),
          removeListener: jest.fn(),
        };
      };

      Object.defineProperty(window, 'location', {
        configurable: true,
        value: { reload: jest.fn() },
      });
})

afterAll(() => {
  Object.defineProperty(window, 'location', { configurable: true, value: original });
});

const mockTodos = [
  {status: true, id: 1, text: 'TEST TODO A', priority: 1, dueDate: '2024-12-30', creationDate: '2024-08-09T15:11:11.512333', doneDate: '2024-08-10T15:11:11.512333'},
  {status: false, id: 2, text: 'TEST TODO B', priority: 2, dueDate: '2024-08-12', creationDate: '2024-08-09T15:11:11.512333', doneDate: ''},
];

test ('Renders components of todos in the table', () => {
    const component = render(<Provider store={store}><TodoList todos={mockTodos}/></Provider>);

    expect(screen.getByText('TEST TODO A')).toBeInTheDocument();
    expect(screen.getByText('TEST TODO B')).toBeInTheDocument();
    expect(screen.getByText('+ New To Do')).toBeInTheDocument();
    const editButtons = screen.getAllByText('Edit')
    expect(editButtons[0]).toBeInTheDocument();

    component.getByText(mockTodos[0].text);
});

test ('Testing add button and its modal', async () => {
  const component = render(<Provider store={store}><TodoList todos={mockTodos}/></Provider>);
  const addButton = screen.getByText('+ New To Do');

  fireEvent.click(addButton);
  let modal = await screen.findByText('Add New To Do');
  expect(modal).toBeInTheDocument();

  //Testing Cancel button
  fireEvent.click(screen.getByText('Cancel'));
  await expect(modal).not.toBeInTheDocument();

  //Testing Ok button
  fireEvent.click(addButton);
  modal = await screen.findByText('Add New To Do');
  expect(modal).toBeInTheDocument();

  fireEvent.click(screen.getByText('OK'));

  expect(window.location.reload).toHaveBeenCalled();

});

test ('Testing edit button and its modal', async () => {
  const component = render(<Provider store={store}><TodoList todos={mockTodos}/></Provider>);
  const editButton = screen.getAllByText('Edit');

  fireEvent.click(editButton[0]);
  let modal = await screen.findByText('Edit To Do');
  expect(modal).toBeInTheDocument();

  //Testing Cancel button
  fireEvent.click(screen.getByText('Cancel'));
  await expect(modal).not.toBeInTheDocument();

  //Testing Ok button
  fireEvent.click(editButton[0]);
  modal = await screen.findByText('Edit To Do');
  expect(modal).toBeInTheDocument();

  fireEvent.click(screen.getByText('OK'));
  await expect(modal).not.toBeInTheDocument();
});

test('Testing To Do checkbox', () => {
  render(<Provider store={store}><TodoList todos={mockTodos} /></Provider>)
  const checkbox = screen.getAllByRole('checkbox');

  expect(checkbox[0]).not.toBeChecked();
  
  fireEvent.click(checkbox[0]);
  expect(checkbox[0]).toBeChecked();

});