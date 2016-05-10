package meetup.akka.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.base.MoreObjects;
import meetup.akka.dal.OrderDao;
import meetup.akka.om.CompleteBatch;
import meetup.akka.om.Order;

import java.util.Random;

public class Persistence extends UntypedActor {
  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private OrderDao orderDao;
  private Random random = new Random();

  public Persistence(OrderDao orderDao) {
    this.orderDao = orderDao;
  }

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof PreparedOrderForAck) {
      randomFail(message);

      PreparedOrderForAck preparedOrderForAck = (PreparedOrderForAck) message;
      PreparedOrder preparedOrder = preparedOrderForAck.preparedOrder;
      Order order = new Order(preparedOrder.orderId, preparedOrder.order);
      orderDao.saveOrder(order);
      log.info("order saved = {}", order);
      sender().tell(new PersistedOrder(preparedOrderForAck.deliveryId, order), self());

    } else if (message instanceof CompleteBatch) {
      orderDao.completeBatch(10);
      log.info("Batch completed.");

    } else {
      unhandled(message);
    }
  }

  private void randomFail(Object message) {
    random.ints(1).filter(i -> i % 2 == 0).forEach(i -> {
      throw new RuntimeException("random fail on message: " + message);
    });
  }
}

class PersistedOrder {
  public final long deliveryId;
  public final Order order;

  public PersistedOrder(long deliveryId, Order order) {
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
