import React, {useState} from "react";
import {Table, Button, Modal, DatePicker, Input, Checkbox, Typography} from 'antd';
import { useDispatch } from "react-redux";
import {addTodo, statusDone, statusUndone, updateTodo} from './todosSlice';

const priorityLabels = ['', 'Low', 'Medium', 'High']
const dayjs = require('dayjs')

function TodoList({todos}){
    const [isAddModalVisible, setIsAddModalVisible] = useState(false);
    const [isEditModalVisible, setIsEditModalVisible] = useState(false);
    const [currentTodo, setCurrentTodo] = useState(null);
    const [dueDate, setDueDate] = useState(null);
    const [status, setStatus] = useState(false);
    const [text, setText] = useState('');
    const [newDueDate, setNewDueDate] = useState(null)
    const [pagination, setPagination] = useState({current: 1, pageSize: 10});
    const dispatch = useDispatch();

    const showEditModal = (todo) => {
        setCurrentTodo(todo);
        setStatus(todo.status);
        setDueDate(dayjs(todo.dueDate));
        setText(todo.text);
        setIsEditModalVisible(true);
    }

    const handleEditOk = () => {
        dispatch(updateTodo({
            ...currentTodo,
            text: text,
            dueDate: dueDate,
            status: status,
        }));
        setIsEditModalVisible(false);
    };

    const handleEditCancel = () => {
        setIsEditModalVisible(false);
    };

    const showAddModal = () => {
        setIsAddModalVisible(true);
    }

    const handleAddOk = () => {
        dispatch(addTodo({
            text: text,
            dueDate: dueDate,
            status: false,
        }));
        setText('')
        setDueDate('')
        setIsAddModalVisible(false);
        window.location.reload();
    };

    const handleAddCancel = () => {
        setText('')
        setDueDate('')
        setIsAddModalVisible(false);
    }

    const handleStatusChecked = (e, todo, checked) => {
        e.checked = e.target.checked
        if (checked){
            dispatch(statusDone({
                ...todo,
                status: true,
            }));
        } else {
            dispatch(statusUndone({
                ...todo,
                status:false
            }));
        }
        window.location.reload();
    }

    const columns = [
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (text, todo) => (
                <Checkbox
                    defaultChecked={todo.status}
                    onChange={(e) => handleStatusChecked(e, todo, e.target.checked)}
                />
            ),
        },
        {
            title: 'Task description',
            dataIndex: 'text',
            key: 'text',
        },
        {
            title: 'Priority',
            dataIndex: 'priority',
            key: 'priority',
            defaultSortOrder: 'descend',
            sorter: {
                compare: (a, b) => a.priority - b.priority,
                multiple: 1,
            },
            render: (priority) => (
                priorityLabels[priority]
            ),
        },
        {
            title: 'Due Date',
            dataIndex: 'dueDate',
            key: 'dueDate',
            defaultSortOrder: 'descend',
            sorter: {
                compare: (a, b) => a.priority - b.priority,
                multiple: 1,
            },
        },
        {
            title: 'Creation Date',
            dataIndex: 'creationDate',
            key: 'creationDate',
        },
        {
            title: 'Done Date',
            dataIndex: 'doneDate',
            key: 'doneDate',
        },
        {
            title: 'Action',
            dataIndex: 'action',
            render: (text, todo) => (
                <Button onClick={() => showEditModal(todo)}>Edit</Button>
            ),
        },
    ];

    const handlePagination = (pagination) => {
        setPagination(pagination);
    }

    return (
        <>
            <Button type='primary' onClick={(showAddModal)} style={{marginBottom: 16, marginTop: 16}}>
                + New To Do
            </Button>
            <Table 
                columns={columns}
                dataSource={todos.map(todo => ({...todo, key: todo.id}))}
                showSorterTooltip={{ target: 'sorter-icon' }}
                pagination={pagination}
                onChange={handlePagination}
                rowClassName={
                    (record, index) => 
                        record.priority === 1 ? 'priority-low' :  
                            (record.priority === 2 ? 'priority-medium' : 
                                (record.priority === 3 ? 'priority-high' : '')
                            )
                }
                
            />
            <Modal
                title="Edit To Do"
                open={isEditModalVisible}
                onOk={handleEditOk}
                onCancel={handleEditCancel}
                destroyOnClose={true}
            >
                <Input
                    defaultValue={text}
                    value={text}
                    onChange={(e) => setText(e.target.value)}
                />
                <DatePicker 
                    value={dueDate}
                    onChange={(date) => setDueDate(date)}
                />
            </Modal>
            <Modal 
                title='Add New To Do'
                open={isAddModalVisible}
                onOk={handleAddOk}
                onCancel={handleAddCancel}
                destroyOnClose={true}
            >
                <div>
                    <Typography.Title level={5}>Task description</Typography.Title>
                    <Input
                        placeholder="Enter text"
                        value={text}
                        onChange={(e) => setText(e.target.value)}
                    />
                </div>
                <div style={{marginTop: 10}}>
                    <Typography.Title level={5}>Due date</Typography.Title>
                    <DatePicker 
                        value={newDueDate}
                        onChange={(date) => setDueDate(date)}
                    />
                </div>
            </Modal>
        </>
    );
}

export default TodoList;