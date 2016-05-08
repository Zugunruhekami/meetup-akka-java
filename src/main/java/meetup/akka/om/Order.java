package meetup.akka.om;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Order implements Serializable {

  private long orderId = -1;
  private LocalDateTime executionDate;
  private OrderType orderType;
  private BigDecimal executionPrice;
  private String symbol;
  private int userId;
  private int quantity;

  public Order(OrderType orderType, BigDecimal executionPrice, String symbol, int userId, int quantity) {
    this(-1L, null, orderType, executionPrice, symbol, userId, quantity);
  }

  public Order(Long orderId, LocalDateTime executionDate, OrderType orderType, BigDecimal executionPrice, String symbol,
               Integer userId, Integer quantity) {
    this.orderId = orderId;
    this.executionDate = executionDate;
    this.orderType = orderType;
    this.executionPrice = executionPrice;
    this.symbol = symbol;
    this.userId = userId;
    this.quantity = quantity;
  }

  public Order(long orderId, Order order) {
    this.orderId = orderId;
    this.executionDate = order.executionDate;
    this.orderType = order.orderType;
    this.executionPrice = order.executionPrice;
    this.symbol = order.symbol;
    this.userId = order.userId;
    this.quantity = order.quantity;
  }

  public long getOrderId() {
    return orderId;
  }

  public LocalDateTime getExecutionDate() {
    return executionDate;
  }

  public OrderType getOrderType() {
    return orderType;
  }

  public BigDecimal getExecutionPrice() {
    return executionPrice;
  }

  public String getSymbol() {
    return symbol;
  }

  public int getUserId() {
    return userId;
  }

  public int getQuantity() {
    return quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return orderId == order.orderId &&
            userId == order.userId &&
            Objects.equal(executionDate, order.executionDate) &&
            orderType == order.orderType &&
            Objects.equal(executionPrice, order.executionPrice) &&
            Objects.equal(symbol, order.symbol);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(orderId, executionDate, orderType, executionPrice, symbol, userId);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).omitNullValues()
            .add("orderId", orderId)
            .add("executionDate", executionDate)
            .add("orderType", orderType)
            .add("executionPrice", executionPrice)
            .add("symbol", symbol)
            .add("userId", userId)
            .add("quantity", quantity)
            .toString();
  }
}
