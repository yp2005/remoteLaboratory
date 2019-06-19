//package com.remoteLaboratory.config;
//
//import com.remoteLaboratory.general.common.config.Constants;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * MQ配置
// *
// * @Author: yupeng
// */
//@Configuration
//public class RabbitConfig {
//    private static Logger log = LoggerFactory.getLogger(RabbitConfig.class);
//
//    @Bean
//    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }
//
//    @Bean
//    public Queue queueParkingSpaceStatistics(RabbitAdmin rabbitAdmin) {
//        log.info("创建MQ队列：" + Constants.PARKING_SPACE_STATISTICS_QUEUE_NAME);
//        Queue queue= new Queue(Constants.PARKING_SPACE_STATISTICS_QUEUE_NAME,false,false,false);
//        rabbitAdmin.declareQueue(queue);
//        return queue;
//    }
//}
