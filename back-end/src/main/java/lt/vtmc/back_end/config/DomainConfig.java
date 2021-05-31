package lt.vtmc.back_end.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan(basePackages = "lt.vtmc.back_end.domain")
@EnableJpaRepositories(basePackages = "lt.vtmc.back_end.repos")
@EnableTransactionManagement
public class DomainConfig {
}
