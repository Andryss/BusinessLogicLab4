package ru.andryss.rutube.configuration;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.spring.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableRetry
public class TransactionConfiguration {

    @Bean
    public DataSource camundaBpmDataSource() {
        return new AtomikosDataSourceBean();
    }

    @Bean
    public PlatformTransactionManager camundaBpmTransactionManager() {
        JtaTransactionManager transactionManager = new JtaTransactionManager();
        transactionManager.setUserTransaction(new UserTransactionImp());
        transactionManager.setTransactionManager(new UserTransactionManager());
        transactionManager.setAllowCustomIsolationLevels(true);
        return transactionManager;
    }
}
