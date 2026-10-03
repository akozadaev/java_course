package course.demo;
public class App {
    interface DeliveryPolicy { long cost(long orderTotal); }
    record Courier(long basePrice) implements DeliveryPolicy { public long cost(long total){return total>=5000?0:basePrice;} }
    record Pickup() implements DeliveryPolicy { public long cost(long total){return 0;} }
    record DeliveryService(DeliveryPolicy policy){long quote(long total){return policy.cost(total);}}
    public static void main(String[] args){System.out.println(new DeliveryService(new Courier(500)).quote(4000));}
}
