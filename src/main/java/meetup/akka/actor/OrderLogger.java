package meetup.akka.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.base.MoreObjects;
import meetup.akka.dal.OrderDao;
import meetup.akka.om.Order;

import java.util.Random;

class OrderLogger extends UntypedActor {
  private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private final OrderDao orderDao;
  private final Random random = new Random();

  public OrderLogger(OrderDao orderDao) {
    this.orderDao = orderDao;
  }

  private void randomFail(Object message) {
    random.ints(1).filter(i -> i % 2 == 0).forEach(i -> {
      throw new RuntimeException("random fail on message: " + message);
    });
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof PreparedOrderForAck) {
      log.info("Received PreparedOrderForAck = {}", message);

      randomFail(message);

      PreparedOrderForAck preparedOrderForAck = (PreparedOrderForAck) message;
      Order order = new Order(preparedOrderForAck.preparedOrder.orderId, preparedOrderForAck.preparedOrder.order);
      orderDao.saveOrder(order);

      sender().tell(new LoggedOrder(preparedOrderForAck.deliveryId, order), self());
      log.info("");
    }
  }
}

class LoggedOrder {
  public final long deliveryId;
  public final Order order;

  public LoggedOrder(long deliveryId, Order order) {
    this.deliveryId = deliveryId;
    this.order = order;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).omitNullValues()
            .add("deliveryId", deliveryId)
            .add("order", order)
            .toString();
  }
}
