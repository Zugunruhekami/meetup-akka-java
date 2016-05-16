package meetup.akka;

import akka.actor.ActorSystem;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class Config {
  @Value("classpath*:/meetup/akka/dal/**/*.xml")
  private Resource[] mappers;
  @Value("${jdbc.driver}")
  private String jdbcDriver;
  @Value("${jdbc.url}")
  private String jdbcUrl;
  @Value("${jdbc.username}")
  private String username;
  @Value("${jdbc.password}")
  private String password;
  @Value("${jdbc.poolMaximumActiveConnections}")
  private int poolMaximumActiveConnections;
  @Value("${jdbc.poolMaximumIdleConnections}")
  private int poolMaximumIdleConnections;

  @Bean
  public DataSource dataSource() {
    PooledDataSource dataSource = new PooledDataSource();
    dataSource.setDriver(jdbcDriver);
    dataSource.setUrl(jdbcUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setPoolMaximumActiveConnections(poolMaximumActiveConnections);
    dataSource.setPoolMaximumIdleConnections(poolMaximumIdleConnections);
    return dataSource;
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    return new SqlSessionFactoryBean() {
      {
        setDataSource(dataSource());
        setMapperLocations(mappers);
        setTypeHandlersPackage("meetup.akka.dal");
      }
    }.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSessionTemplate() throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory());
  }

  @Bean
  public ActorSystem actorSystem() {
    return ActorSystem.create("AkkaJavaSpring");
  }
}
