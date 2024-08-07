import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchTodos } from "./components/todosSlice";
import {Layout, Typography, Flex, Card} from 'antd';
import TodoList from "./components/todoList";
import TodoFilters from "./components/filters";
import TodoMetrics from "./components/metrics";

const {Header, Content} = Layout;
const {Title} = Typography;

function App() {
  const dispatch = useDispatch();
  const todos = useSelector((state => state.todos.items));
  const todosStatus = useSelector((state) => state.todos.status);
  const error = useSelector((state) => state.todos.error);

  useEffect(() =>{
    if(todosStatus === 'idle'){
      dispatch(fetchTodos({}));
    }
  }, [todosStatus, dispatch]);

  return (
    <Layout style={{minHeight: '100vh'}}>
      <Header style={{background: '#fff'}}>
        <Title level={2}>To Do App</Title>
      </Header>
      <Flex wrap gap="middle" vertical>
        <Content style={{padding: '0 50px'}}>
          <Card title="Filters">
            <TodoFilters />
          </Card>
          {todosStatus === 'loading' && <p>Now loading... please wait</p>}
          {todosStatus === 'error' && <p>{error}</p>}
          <TodoList todos={todos}/>
          <Card title="Metrics" style={{padding: '0 25px', marginTop: 16}}>
            <TodoMetrics todos={todos}/>
          </Card>
        </Content>
      </Flex>
    </Layout>
  );
}


export default App;