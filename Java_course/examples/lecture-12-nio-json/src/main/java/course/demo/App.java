package course.demo;
import com.fasterxml.jackson.databind.ObjectMapper; import java.nio.charset.*; import java.nio.file.*; import java.util.*;
public class App { record Product(long id,String name){} public static void main(String[] args)throws Exception{var mapper=new ObjectMapper();var file=Files.createTempFile("products",".json");mapper.writeValue(file.toFile(),List.of(new Product(1,"Java")));System.out.println(Files.readString(file,StandardCharsets.UTF_8));Files.deleteIfExists(file);}}
