package meetup.akka.om;

public class NewOrder {
  public final Order order;

  public NewOrder(Order order) {
    this.order = order;
  }
}
