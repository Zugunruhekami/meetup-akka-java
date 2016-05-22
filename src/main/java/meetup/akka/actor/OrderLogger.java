package meetup.akka.actor;

import com.google.common.base.MoreObjects;
import meetup.akka.dal.OrderDao;
import meetup.akka.om.Order;

import java.util.Random;

class OrderLogger {
  //private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private OrderDao orderDao;
  private Random random = new Random();

  public OrderLogger(OrderDao orderDao) {
    this.orderDao = orderDao;
  }

  private void randomFail(Object message) {
    random.ints(1).filter(i -> i % 2 == 0).forEach(i -> {
      throw new RuntimeException("random fail on message: " + message);
    });
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
