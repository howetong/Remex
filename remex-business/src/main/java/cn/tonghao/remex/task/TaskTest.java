package cn.tonghao.remex.task;


import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by tonghao on 2017/6/29.
 */
public class TaskTest {

    private static final Logger logger = LoggerFactory.getLogger(TaskTest.class);


    public void execute() throws JobExecutionException {
        logger.info("TaskTest在时间"+new Date()+"调度了任务");
    }
}
