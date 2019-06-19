package com.remoteLaboratory;

import com.remoteLaboratory.utils.LogCleanUtil;
import com.remoteLaboratory.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.remoteLaboratory.entities")
@EnableJpaRepositories(basePackages = "com.remoteLaboratory.repositories")
@EnableAutoConfiguration
public class RemoteLaboratoryApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(RemoteLaboratoryApplication.class, args);
        SpringUtil.setApplicationContext(applicationContext);
        new LogCleanUtil().execute(); // 日志清理调度
    }
}
