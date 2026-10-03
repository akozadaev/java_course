package course.demo;
import java.math.*;
public class App { public static void main(String[] args) {
    var price = new BigDecimal("19.90"); var quantity = 3;
    var subtotal = price.multiply(BigDecimal.valueOf(quantity));
    var total = subtotal.multiply(new BigDecimal("1.20")).setScale(2, RoundingMode.HALF_UP);
    System.out.println("Total: " + total);
}}
