import React, { useEffect } from "react";
import { useDispatch, useSelector, Provider } from "react-redux";
import { fetchTodos} from "./components/todosSlice";
import { getMetrics } from "./components/metricsSlice";
import {Layout, Typography, Flex, Card, notification} from 'antd';
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
  const [api, contextHolder] = notification.useNotification();

  const NETWORKERROR = 'Network Error';
  const BADREQUESTERROR = 'Bad Request';
  const NETWORKERRORMESSAGE = 'Could not fetch the requested information. Please try again later.';
  const BADREQUESTMESSAGE= 'Could not add the requested information. Please check your input and try again.';

  const openNotificationWithIcon = (type, error, message) => {
    api[type]({
      message: error,
      description: message,
      showProgress: true,
      pauseOnHover: true,
    });
  };

  useEffect(() =>{
    if(todosStatus === 'idle'){
      dispatch(fetchTodos({}));
      dispatch(getMetrics());
    }
  }, [todosStatus, dispatch]);

  useEffect(() => {
    if (todosStatus === 'failed') {
      openNotificationWithIcon('error', NETWORKERROR, NETWORKERRORMESSAGE);
      return;
    }
    if(todosStatus === 'badRequest'){
      openNotificationWithIcon('error', BADREQUESTERROR, BADREQUESTMESSAGE);
      return;
    }
  }, [todosStatus, error]);

  return (
    <Layout style={{minHeight: '100vh'}}>
      {contextHolder}
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