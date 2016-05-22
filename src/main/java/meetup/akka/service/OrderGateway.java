package meetup.akka.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import meetup.akka.dal.OrderDao;
import meetup.akka.om.Order;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class OrderGateway {
  private ActorSystem system;
  private ActorRef orderProcessor;

  @Inject
  public OrderGateway(ActorSystem system, ApplicationContext applicationContext) {
    this.system = system;
    init(applicationContext.getBean(OrderDao.class));
  }

  private void init(OrderDao orderDao) {

  }

  public Order placeOrder() {
    Order order = OrderUtil.generateRandomOrder();

    return order;
  }

  public void completeBatch() {

  }
}
