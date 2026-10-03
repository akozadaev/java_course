package course.demo;
import java.util.*;
public class App {
    static <T> Map<T,Long> frequencies(List<T> values){var map=new HashMap<T,Long>();values.forEach(v->map.merge(v,1L,Long::sum));return Map.copyOf(map);}
    public static void main(String[] args){System.out.println(frequencies(List.of("Java","SQL","Java")));}
}
