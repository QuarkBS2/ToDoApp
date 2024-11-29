import React, {useState, useEffect, useCallback} from "react";
import {Table, Button, Modal, DatePicker, Input, Checkbox, Form} from 'antd';
import { useDispatch, useSelector } from "react-redux";
import {fetchTodos, addTodo, statusDone, statusUndone, updateTodo} from './todosSlice';
import { getMetrics } from "./metricsSlice";

const priorityLabels = ['', 'Low', 'Medium', 'High']
const dayjs = require('dayjs')

const useFetchTodos = (filters, currentPage) => {
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(fetchTodos({ ...filters, page: currentPage }));
    }, [dispatch, filters, currentPage]);

    const fetchInfo = useCallback(async (current, sortParams) => {
        try {
            await dispatch(fetchTodos({
                ...filters,
                page: current,
                sortParams,
            }));
            dispatch(getMetrics());
        } catch (error) {
            console.error('Failed to fetch todos:', error);
        }
    }, [dispatch, filters]);

    return { fetchInfo };
};

/**
 * Renders a list of todos with the ability to add, edit, and delete todos.
 *
 * @component
 * @param {Object} props - The component props.
 * @param {Array} props.todos - The list of todos to display.
 * @returns {JSX.Element} The rendered TodoList component.
 */
function TodoList({todos}){
    const [isAddModalVisible, setIsAddModalVisible] = useState(false);
    const [isEditModalVisible, setIsEditModalVisible] = useState(false);
    const [currentTodo, setCurrentTodo] = useState(null);
    const [dueDate, setDueDate] = useState(null);
    const [text, setText] = useState('');
    const [addForm] = Form.useForm();
    const [editForm] = Form.useForm();
    const dispatch = useDispatch();
    const [currentPage, setCurrentPage] = useState(1);
    const filters = useSelector((state) => state.filters);
    const { fetchInfo } = useFetchTodos(filters, currentPage);

    const handleTableChange = useCallback((pagination, filters, sorter) => {
        const sorters = Array.isArray(sorter) ? sorter : [sorter];
        if (sorters.length === 1) {
            if (sorters[0].order === undefined) {
            fetchInfo(currentPage);
            return;
            }
        }
        const sortParams = sorters.map(s => ({
            field: s.field,
            direction: s.order === 'ascend' ? 'asc' : 'desc'
        }));
        fetchInfo(currentPage, sortParams);
    }, [fetchInfo, currentPage]);

    const showEditModal = (todo) => {
        setCurrentTodo(todo);
        setDueDate(dayjs(todo.dueDate));
        setText(todo.text);
        setIsEditModalVisible(true);
    }

    const handleEditOk = async () => {
        try{
            await editForm.validateFields();
            if (!currentTodo) {
                console.error("No todo selected for editing");
                return;
            }

            await dispatch(updateTodo({
                ...currentTodo,
                text: text,
                dueDate: dueDate,
            }));
            setText('')
            setDueDate(null)
            setIsEditModalVisible(false);
            onChangePage(currentPage);
        } catch (error) {
            console.error('Validation failed:', error);
        }
    };

    const handleEditCancel = () => {
        setText('')
        setDueDate(null)
        setIsEditModalVisible(false);
    };

    const showAddModal = () => {
        setIsAddModalVisible(true);
    }

    const handleAddOk = async () => {
        try{
            await addForm.validateFields();
            await dispatch(addTodo({
                text: text,
                dueDate: dueDate,
                status: false,
            }));
            setText('')
            setDueDate(null)
            setIsAddModalVisible(false);
            onChangePage(currentPage);
        } catch (error) {
            console.error('Validation failed:', error);
        }

    };

    const handleAddCancel = () => {
        setText('')
        setDueDate(null)
        setIsAddModalVisible(false);
    }

    const handleStatusChecked = (e, todo, checked) => {
        e.checked = e.target.checked
        dispatch(checked ? statusDone({ ...todo, status: true }) : statusUndone({ ...todo, status: false }));
        onChangePage(currentPage);
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
            sorter: { multiple: 2 },
            render: (priority) => (priorityLabels[priority]),
        },
        {
            title: 'Due Date',
            dataIndex: 'dueDate',
            key: 'dueDate',
            sorter: { multiple: 2 },
        },
        {
            title: 'Creation Date',
            dataIndex: 'creationDate',
            key: 'creationDate',
            render: (text) => text ? dayjs(text).format('YY-MM-DD HH:mm') : "--:--:--",
        },
        {
            title: 'Done Date',
            dataIndex: 'doneDate',
            key: 'doneDate',
            render: (text) => text ? dayjs(text).format('YY-MM-DD HH:mm') : "--:--:--",
        },
        {
            title: 'Action',
            dataIndex: 'action',
            render: (text, todo) => (
                <Button onClick={() => showEditModal(todo)}>Edit</Button>
            ),
        },
    ];

    const onChangePage = (current) => {
        fetchInfo(current);
        setCurrentPage(current);
      }

    return (
        <>
            <Button type='primary' onClick={(showAddModal)} style={{marginBottom: 16, marginTop: 16}}>
                + New To Do
            </Button>
            <Table
                columns={columns}
                pagination={{
                    position: 'topRight',
                    defaultCurrent: 1,
                    total: todos.total,
                    showTotal: (total) => `Total ${total} items`,
                    onChange: (current) => onChangePage(current)

                  }}
                dataSource={todos?.todosList?.map(todo => ({...todo, key: todo.id}))}
                onChange={handleTableChange}
                showSorterTooltip={{ target: 'sorter-icon' }}
                rowClassName={
                    (record) =>
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
                <Form form={editForm} layout="vertical" clearOnDestroy="true">
                    <Form.Item
                        label="Task description"
                        name="text"
                        initialValue={text}
                        rules={[{ required: true, message: 'Please enter the task description' }]}
                    >
                        <Input
                            placeholder="Enter text"
                            value={text}
                            onChange={(e) => setText(e.target.value)}
                        />
                    </Form.Item>
                    <Form.Item
                        label="Due date"
                        name="dueDate"
                        initialValue={dueDate}
                        rules={[{ required: true, message: 'Please select a due date' }]}
                    >
                        <DatePicker
                            value={dueDate}
                            onChange={(date) => setDueDate(date)}
                        />
                    </Form.Item>
                </Form>
            </Modal>
            <Modal
                title='Add New To Do'
                open={isAddModalVisible}
                onOk={handleAddOk}
                onCancel={handleAddCancel}
                destroyOnClose={true}
            >
                <Form form={addForm} layout="vertical" clearOnDestroy="true">
                    <Form.Item
                        label="Task description"
                        name="text"
                        rules={[{ required: true, message: 'Please enter the task description' }]}
                    >
                        <Input
                        placeholder="Enter text"
                        value={text}
                        onChange={(e) => setText(e.target.value)}
                        />
                    </Form.Item>
                    <Form.Item
                        label="Due date"
                        name="dueDate"
                        rules={[{ required: true, message: 'Please select a due date' }]}
                    >
                        <DatePicker
                        value={dueDate}
                        onChange={(date) => setDueDate(date)}
                        />
                    </Form.Item>
                </Form>
            </Modal>
        </>
    );
}

export default TodoList;