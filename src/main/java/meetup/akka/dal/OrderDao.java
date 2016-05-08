package meetup.akka.dal;

import com.google.common.collect.ImmutableMap;
import meetup.akka.om.Order;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao {
  @Inject
  private SqlSession sqlSession;

  public void saveOrder(Order order) {
    sqlSession.insert("meetup.akka.dal.OrderDao.saveOrder", order);
  }

  public List<Order> getOrders() {
    return sqlSession.selectList("meetup.akka.dal.OrderDao.getOrders");
  }

  public void completeBatch(long id) {
    Map<String, Object> params = ImmutableMap.of("id", id, "date", new Date());
    sqlSession.update("meetup.akka.dal.OrderDao.completeBatch", params);
  }
}
