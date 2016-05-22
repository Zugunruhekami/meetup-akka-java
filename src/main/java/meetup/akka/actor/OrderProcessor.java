package meetup.akka.actor;

import com.google.common.base.MoreObjects;
import meetup.akka.dal.OrderDao;

public class OrderProcessor {
  //private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  public OrderProcessor(OrderDao orderDao) {

  }

  private void generateOrderId(Object event) {

  }

  private void updateState(Object event) {

  }
}

class PreparedOrderForAck {
  public final long deliveryId;
  public final PreparedOrder preparedOrder;

  public PreparedOrderForAck(long deliveryId, PreparedOrder preparedOrder) {
    this.deliveryId = deliveryId;
    this.preparedOrder = preparedOrder;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("deliveryId", deliveryId)
            .add("preparedOrder", preparedOrder)
            .toString();
  }
}
