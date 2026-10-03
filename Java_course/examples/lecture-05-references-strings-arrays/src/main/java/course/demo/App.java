package course.demo;
import java.util.*;
public class App {
    enum Status { NEW, IN_PROGRESS, DONE }
    static String normalize(String csv) { return Arrays.stream(csv.split(",")).map(String::strip).filter(s->!s.isEmpty()).map(String::toUpperCase).map(Status::valueOf).map(Enum::name).reduce((a,b)->a+", "+b).orElse(""); }
    public static void main(String[] args) { System.out.println(normalize("new, in_progress, done")); }
}
