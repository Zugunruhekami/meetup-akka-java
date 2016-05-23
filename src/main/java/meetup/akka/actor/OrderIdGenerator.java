package meetup.akka.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.base.MoreObjects;
import meetup.akka.om.Order;

import java.io.Serializable;

class OrderIdGenerator extends UntypedActor {
  private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private long seqNo;

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Order) {
      log.info("Generate id for order = {}", message);
      PreparedOrder preparedOrder = new PreparedOrder((Order) message, ++seqNo);
      sender().tell(preparedOrder, self());

    } else {
      unhandled(message);
    }
  }
}

class PreparedOrder implements Serializable {
  public final Order order;
  public final long orderId;

  public PreparedOrder(Order order, long orderId) {
    this.order = order;
    this.orderId = orderId;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("order", order)
            .add("orderId", orderId)
            .toString();
  }
}