package meetup.akka;

import akka.actor.ActorSystem;
import meetup.akka.dal.OrderDao;
import meetup.akka.om.Order;
import meetup.akka.service.OrderGateway;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.IntStream;

@Import(Config.class)
public class App {
  public static void main(String[] args) throws InterruptedException {
    SpringApplication app = new SpringApplication(App.class);
    app.setWebEnvironment(false);
    ConfigurableApplicationContext context = app.run(args);

    try {
      int orders = 10;
      placeOrders(orders, context);
      checkOrdersInStorage(orders, context);
      completeBatch(context);
    } finally {
      context.getBean(ActorSystem.class).shutdown();
    }
  }

  private static void placeOrders(int orders, ApplicationContext context) throws InterruptedException {
    OrderGateway orderGateway = context.getBean(OrderGateway.class);
    IntStream.range(0, orders).parallel().forEach(i -> orderGateway.placeOrder());
  }

  private static void checkOrdersInStorage(int expectedOrders, ApplicationContext context) throws InterruptedException {
    OrderDao orderDao = context.getBean(OrderDao.class);
    List<Order> orders = orderDao.getOrders();

    while (orders.size() < expectedOrders) {
      Thread.sleep(2_000);
      orders = orderDao.getOrders();
    }

    System.out.println("\nOrders are in storage: ");
    orders.forEach(System.out::println);
  }

  private static void completeBatch(ApplicationContext context) throws InterruptedException {
    OrderGateway orderGateway = context.getBean(OrderGateway.class);
    orderGateway.completeBatch();
    Thread.sleep(5_000);
  }
}
