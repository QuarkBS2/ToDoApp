import React, { useState, useEffect } from 'react';
import { Card, Statistic, Row, Col } from 'antd';

function TodoMetrics({todos}){
    const [data, setData] = useState(null);
    useEffect(() => {
        const storedData = localStorage.getItem('myData');
        if (storedData !== '[]' && storedData) {
          todos = JSON.parse(storedData);
        } else {
            localStorage.clear();
        }
      }, []);

    let nullCounter = todos.filter(todo => todo.elapsedTime === null).length;
    let allTotal = 0;
    let allLow = 0
    let allMedium = 0;
    let allHigh = 0;

    todos.forEach(todo => {
        if(todo.elapsedTime){
            if(todo.priority === 1){
                allLow += todo.elapsedTime;
            }
            if(todo.priority === 2){
                allMedium += todo.elapsedTime;
            }
            if(todo.priority === 3){
                allHigh += todo.elapsedTime;
            }
            allTotal += todo.elapsedTime;
        }
    });

    allTotal = (allTotal / (todos.length - nullCounter)) / 60;
    allLow = (allLow / (todos.filter(todo => todo.priority === 1).filter(todo => todo.elapsedTime !== null).length)) / 60;
    allMedium = (allMedium / (todos.filter(todo => todo.priority === 2).filter(todo => todo.elapsedTime !== null).length)) / 60;
    allHigh = (allHigh / (todos.filter(todo => todo.priority === 3).filter(todo => todo.elapsedTime !== null).length)) / 60;

    if(!localStorage.getItem('myData')){
        const newData = todos
        setData(newData);
        localStorage.setItem('myData', JSON.stringify(newData));
    }
    
    return(
        <>
            <Row gutter={16}>
                <Col span={12}>
                    <Card>
                        <Statistic title="Average time to finish tasks" value={allTotal.toFixed(2)} suffix="minutes"/>
                    </Card>
                </Col>

                <Col span={12}>
                    <Card title="Average time to finish tasks by priority" bordered={false}>
                        <Statistic title="Low priority" value={allLow.toFixed(2)} suffix="minutes"/>
                        <Statistic title="Medium priority" value={allMedium.toFixed(2)} suffix="minutes"/>
                        <Statistic title="High priority" value={allHigh.toFixed(2)} suffix="minutes"/>
                    </Card>
                </Col>
            </Row>
        </>
    );
}

export default TodoMetrics;