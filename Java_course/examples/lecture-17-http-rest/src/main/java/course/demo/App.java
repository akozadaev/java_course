package course.demo;
import com.sun.net.httpserver.*; import java.net.*; import java.nio.charset.*;
public class App { public static void main(String[] args)throws Exception{var server=HttpServer.create(new InetSocketAddress(8080),0);server.createContext("/health",e->{var body="{\"status\":\"UP\"}".getBytes(StandardCharsets.UTF_8);e.getResponseHeaders().add("Content-Type","application/json");e.sendResponseHeaders(200,body.length);try(var out=e.getResponseBody()){out.write(body);}});server.start();System.out.println("GET http://localhost:8080/health");}}
