package meetup.akka.service;

import meetup.akka.om.Order;
import meetup.akka.om.OrderType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OrderUtil {
  private static final List<String> SYMBOLS = Arrays.asList("APPL", "GOOG", "IBM", "YAH");
  private static final Random random = new Random();

  public static Order generateRandomOrder() {
    return new Order(
            OrderType.values()[random.nextInt(OrderType.values().length)],
            BigDecimal.valueOf(random.nextDouble() * 100),
            SYMBOLS.get(random.nextInt(SYMBOLS.size())),
            Math.abs(random.nextInt()),
            Math.abs(random.nextInt(500))
    );
  }
}
