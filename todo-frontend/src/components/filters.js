import React from "react";
import { useDispatch } from "react-redux";
import {fetchTodos} from './todosSlice';
import {Form, Input, Select, Button, Flex} from 'antd';

const {Option} = Select;

function TodoFilters(){
    const [form] = Form.useForm();
    const dispatch = useDispatch();

    const onFinish = (values) => {
        dispatch(fetchTodos({
            status: values.status === '' ? null : values.status === 'done',
            text: values.text === '' ? null : values.text,
            priority: values.priority === '' ? null : parseInt(values.priority),
            sortBy: values.sortBy,
            direction: values.direction,
        }));
    };

    return (
        <>
            <Form 
                form={form} 
                layout="horizontal" 
                onFinish={onFinish}
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
