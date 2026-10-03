package course.demo;
import java.util.*; import static java.util.stream.Collectors.*;
public class App {
    record Sale(String category,long amount,boolean cancelled){}
    static Map<String,Long> totals(List<Sale> sales){return sales.stream().filter(s->!s.cancelled()).collect(groupingBy(Sale::category,summingLong(Sale::amount)));}
    public static void main(String[] args){System.out.println(totals(List.of(new Sale("books",1000,false),new Sale("books",500,true),new Sale("food",300,false))));}
}
