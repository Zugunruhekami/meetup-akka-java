package meetup.akka.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import meetup.akka.actor.OrderProcessor;
import meetup.akka.dal.OrderDao;
import meetup.akka.om.CompleteBatch;
import meetup.akka.om.NewOrder;
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
    orderProcessor = system.actorOf(Props.create(OrderProcessor.class, orderDao), "orderProcessor");
  }

  public Order placeOrder() {
    Order order = OrderUtil.generateRandomOrder();
    orderProcessor.tell(new NewOrder(order), ActorRef.noSender());
    return order;
  }

  public void completeBatch() {
    orderProcessor.tell(new CompleteBatch(), ActorRef.noSender());
  }
}
