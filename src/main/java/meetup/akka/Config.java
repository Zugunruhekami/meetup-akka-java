package meetup.akka;

import akka.actor.ActorSystem;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.inject.Named;
import javax.sql.DataSource;

@ComponentScan
@Configuration
public class Config {
  @Value("classpath*:/meetup/akka/dal/**/*.xml")
  private Resource[] mappers;

  @Bean
  public DataSource dataSource() {
    PooledDataSource dataSource = new PooledDataSource();
    dataSource.setDriver("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://192.168.99.101:3306/somedb?autoReconnect=true&useSSL=false");
    dataSource.setUsername("root");
    dataSource.setPassword("root");
    dataSource.setPoolMaximumActiveConnections(5);
    dataSource.setPoolMaximumIdleConnections(3);
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

  @Bean
  @Named("Recovery")
  public boolean recovery() {
    return false;
  }

  @Bean
  @Named("PersistenceRandomFail")
  public boolean randomFail() {
    return false;
  }

}
