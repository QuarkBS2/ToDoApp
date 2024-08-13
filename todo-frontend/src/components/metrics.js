import React from 'react';
import { Card, Statistic, Row, Col } from 'antd';

function TodoMetrics({metrics}){
    return(
        <>
            <Row gutter={16}>
                <Col span={12}>
                    <Card>
                        <Statistic title="Average time to finish tasks" value={metrics.avgTime?.toFixed(2)} suffix="minutes"/>
                    </Card>
                </Col>

                <Col span={12}>
                    <Card title="Average time to finish tasks by priority" bordered={false}>
                        <Statistic title="Low priority" value={metrics.avgTimeLow?.toFixed(2)} suffix="minutes"/>
                        <Statistic title="Medium priority" value={metrics.avgTimeMedium?.toFixed(2)} suffix="minutes"/>
                        <Statistic title="High priority" value={metrics.avgTimeHigh?.toFixed(2)} suffix="minutes"/>
                    </Card>
                </Col>
            </Row>
        </>
    );
}

export default TodoMetrics;