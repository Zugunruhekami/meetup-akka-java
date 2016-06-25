package meetup.akka.actor;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.persistence.UntypedPersistentActorWithAtLeastOnceDelivery;
import akka.routing.RoundRobinPool;
import com.google.common.base.MoreObjects;
import meetup.akka.dal.OrderDao;
import meetup.akka.om.NewOrder;

public class OrderProcessor extends UntypedPersistentActorWithAtLeastOnceDelivery {
  private static final int NUMBER_OF_LOGGERS = 5;
  private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  private final ActorRef orderIdGenerator;
  private final ActorPath orderLogger;

  public OrderProcessor(OrderDao orderDao) {
    orderIdGenerator = context().actorOf(Props.create(OrderIdGenerator.class), "orderIdGenerator");
    orderLogger = context().actorOf(
            new RoundRobinPool(NUMBER_OF_LOGGERS).props(Props.create(OrderLogger.class, orderDao)), "orderLogger").path();
  }

  private void generateOrderId(Object event) {
    if (event instanceof NewOrder) {
      log.info("New order received = {}", event);
      NewOrder newOrder = (NewOrder) event;
      orderIdGenerator.tell(newOrder.order, self());
    } else {
      unhandled(event);
    }
  }

  private void updateState(Object event) {
    if (event instanceof PreparedOrder) {
      PreparedOrder preparedOrder = (PreparedOrder) event;
      deliver(orderLogger, (Long deliveryId) -> new PreparedOrderForAck(deliveryId, preparedOrder));
    } else {
      unhandled(event);
    }
  }

  @Override
  public void onReceiveRecover(Object msg) throws Exception {
    generateOrderId(msg);
  }

  @Override
  public void onReceiveCommand(Object msg) throws Exception {
    if (msg instanceof NewOrder) {
      persist(msg, this::generateOrderId);
      generateOrderId(msg);

    } else if (msg instanceof PreparedOrder) {
      log.info("Prepared order received = {}", msg);
      persist(msg, this::updateState);

    } else if (msg instanceof LoggedOrder) {
      LoggedOrder loggedOrder = (LoggedOrder) msg;
      confirmDelivery(loggedOrder.deliveryId);
      log.info("LoggedOrder received = {}", msg);

    } else {
      unhandled(msg);
    }

  }

  @Override
  public String persistenceId() {
    return "orders";
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
