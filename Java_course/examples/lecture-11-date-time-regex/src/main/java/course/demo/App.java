package course.demo;
import java.time.*; import java.time.format.*; import java.util.regex.*;
public class App { public static void main(String[] args){var input="2026-09-17T12:00:00Z";if(!Pattern.matches("\\d{4}-.*Z",input))throw new IllegalArgumentException("format");var instant=Instant.parse(input);for(var zone: new String[]{"Europe/Moscow","Asia/Tokyo","America/New_York"})System.out.println(instant.atZone(ZoneId.of(zone)).format(DateTimeFormatter.ISO_ZONED_DATE_TIME));}}
