package course.demo;
import java.io.*; import java.nio.file.*; import java.util.*;
public class App {
    static List<String> validLines(Path path)throws IOException{var result=new ArrayList<String>();try(var lines=Files.lines(path)){lines.map(String::strip).filter(s->!s.isEmpty()).forEach(result::add);}return List.copyOf(result);}
    public static void main(String[] args)throws IOException{var file=Files.createTempFile("orders",".txt");Files.writeString(file,"first\n\nsecond\n");System.out.println(validLines(file));Files.deleteIfExists(file);}
}
