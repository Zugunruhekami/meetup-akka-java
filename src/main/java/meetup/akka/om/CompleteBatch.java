package meetup.akka.om;

public class CompleteBatch {
  public final long upToId;

  public CompleteBatch(long upToId) {
    this.upToId = upToId;
  }
}
