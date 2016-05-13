package meetup.akka.om;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

public class NewOrder implements Serializable {
  public final Order order;

  public NewOrder(Order order) {
    this.order = order;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).omitNullValues()
            .add("order", order)
            .toString();
  }
}
