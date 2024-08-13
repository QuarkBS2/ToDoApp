import React, { useEffect } from "react";
import { useDispatch, useSelector, Provider } from "react-redux";
import { fetchTodos} from "./components/todosSlice";
import { getMetrics } from "./components/metricsSlice";
import {Layout, Typography, Flex, Card} from 'antd';
import TodoList from "./components/todoList";
import TodoFilters from "./components/filters";
import TodoMetrics from "./components/metrics";
import store from "./app/store";

const {Header, Content} = Layout;
const {Title} = Typography;

const AppWrapper = () => {
  return (
    <Provider store={store}>
      <App />
    </Provider>
  )
}

function App() {
  const dispatch = useDispatch();
  const todos = useSelector((state => state.todos.items));
  const todosStatus = useSelector((state) => state.todos.status);
  const error = useSelector((state) => state.todos.error);
  const metrics = useSelector((state) => state.metrics.items);

  useEffect(() =>{
    if(todosStatus === 'idle'){
      dispatch(fetchTodos({}));
      dispatch(getMetrics());
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
            <TodoMetrics metrics={metrics}/>
          </Card>
        </Content>
      </Flex>
    </Layout>
  );
}


export default AppWrapper;