package meetup.akka.actor;

import akka.actor.UntypedActor;
import com.google.common.base.MoreObjects;
import meetup.akka.om.Order;

import java.io.Serializable;

class OrderIdGenerator extends UntypedActor {
  private long seqNo;

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Order) {
      sender().tell(new PreparedOrder((Order) message, ++seqNo), self());
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