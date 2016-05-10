package meetup.akka.om;

import java.io.Serializable;

public class NewOrder implements Serializable {
  public final Order order;

  public NewOrder(Order order) {
    this.order = order;
  }
}
