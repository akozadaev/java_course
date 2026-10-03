package course.demo;
import java.math.*; import java.util.*;
public class App {
    record Product(long id,String name,BigDecimal price){ Product { Objects.requireNonNull(name); Objects.requireNonNull(price); name=name.strip(); if(id<=0||name.isEmpty()||price.signum()<0)throw new IllegalArgumentException(); }}
    record OrderLine(Product product,int quantity){ OrderLine { if(quantity<=0)throw new IllegalArgumentException(); } BigDecimal total(){return product.price().multiply(BigDecimal.valueOf(quantity));}}
    public static void main(String[] args){var line=new OrderLine(new Product(1,"Java Book",new BigDecimal("49.90")),2);System.out.println(line.total());}
}
