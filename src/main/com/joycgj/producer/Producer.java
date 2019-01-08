package com.joycgj.producer;


import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.JobProducer;

public class Producer {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setServiceHost("10.179.16.48");
        config.setServicePort(11300);
        BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
        JobProducer producer = factory.createJobProducer("beanstalkd-test");
        String msg = "Hello, Beanstalkd";

        /**
         * priority: 优先级
         * delay: 延迟多长时间开始执行, 单位秒
         * ttr: 单位秒, 为consumer操作设置的reserve超时时间, 如果consumer在这个ttr时间里没有完成job并将job delete掉,
         * 那这个job就会重新被迁回ready状态，再次供消费者执行
         */
        long jobId = producer.putJob(100, 30, 5, msg.getBytes());
        System.out.println(jobId);
        producer.close();
    }
}
