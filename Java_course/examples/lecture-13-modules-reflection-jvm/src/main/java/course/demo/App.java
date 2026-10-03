package course.demo;
import java.lang.annotation.*;
public class App {
    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD) @interface Demo { String value(); }
    @Demo("reflection") static void hello(){System.out.println("Hello from reflection");}
    public static void main(String[] args)throws Exception{var method=App.class.getDeclaredMethod("hello");System.out.println(method.getAnnotation(Demo.class).value());method.invoke(null);System.out.println("PID: "+ProcessHandle.current().pid());}
}
