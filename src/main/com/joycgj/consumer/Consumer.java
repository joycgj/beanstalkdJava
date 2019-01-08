package com.joycgj.consumer;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.Job;
import com.dinstone.beanstalkc.JobConsumer;

import java.util.Objects;

public class Consumer {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setServiceHost("10.179.16.48");
        config.setServicePort(11300);
        BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
        JobConsumer consumer = factory.createJobConsumer("beanstalkd-test");
        while (true) {
            Job job = consumer.reserveJob(3);
            if (Objects.isNull(job)) {
                continue;
            }
            System.out.println(job.getId());
            System.out.println(new String(job.getData()));


            /**
             * consumer代码最后注释掉了 consumer.deleteJob(job.getId()),
             * 没有将消息delete掉, 这个job将会一直从reserve状态到ready状态,
             * beanstalkd会认为consumer没有在ttr时间之内完成job, 而且这个操作的频繁执行很耗性能.
             * 大量的这种操作会导致你的CPU使用率一下就上去了, 所以consumer完成了job之后, 就将job delete掉,
             * 如果业务代码在完成job时出现异常, 也要在try catch Exception中将job给delete掉
             */
//            consumer.deleteJob(job.getId());

            /**
             * consumer.releaseJob(long id, int priority, int delay),
             * 将消息从reserved状态迁移到delay状态, 延迟（指定的延迟时间）之后job变成ready状态供消费者继续消费.
             */
            consumer.releaseJob(job.getId(), 99, 10);
        }
    }
}
