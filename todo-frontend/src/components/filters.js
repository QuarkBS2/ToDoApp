import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchTodos } from './todosSlice';
import { setFilters } from './filtersSlice';
import {Form, Input, Select, Button, Flex} from 'antd';

const {Option} = Select;

/**
 * Renders a component for applying filters to a todo list.
 *
 * @returns {JSX.Element} The rendered component.
 */
function TodoFilters(){
    const dispatch = useDispatch();
    const filters = useSelector((state) => state.filters);

    const handleApplyFilters = (values) => {
        if (values.status === 'done') {
                values.status = true;
            } else if (values.status === 'undone') {
                values.status = false;
            } else {
                values.status = '';
          }
        dispatch(setFilters(values));
        dispatch(fetchTodos(values));
    };

    return (
        <>
            <Form
                initialValues={filters}
                onFinish={handleApplyFilters}
                layout="horizontal"
                wrapperCol={{
                    span: 16,
                }}
            >
                <Form.Item name="text" label="Search by word">
                    <Input placeholder="Search task"/>
                </Form.Item>
                
                <Form.Item name="status" initialValue="" label="Filter by status">
                    <Select style={{width: 120}}>
                        <Option value="">All</Option>
                        <Option value="done">Done</Option>
                        <Option value="undone">Undone</Option>
                    </Select>
                </Form.Item>
                
                <Form.Item name="priority" initialValue="" label="Filter by priority">
                    <Select style={{width: 120}}>
                        <Option value="">All</Option>
                        <Option value="1">Low</Option>
                        <Option value="2">Medium</Option>
                        <Option value="3">High</Option>
                    </Select>
                </Form.Item>

                <Flex
                    vertical
                    align="flex-end"
                    justify="space-between"
                    style={{
                        padding: 25,
                    }}
                >
                    <Button type="primary" htmlType="submit" >Apply filters</Button>
                </Flex>
            </Form>
        </>
    );
}

export default TodoFilters;
