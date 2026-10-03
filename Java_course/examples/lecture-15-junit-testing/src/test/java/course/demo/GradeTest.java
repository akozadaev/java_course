package course.demo;
import org.junit.jupiter.params.ParameterizedTest; import org.junit.jupiter.params.provider.CsvSource; import static org.junit.jupiter.api.Assertions.*;
class GradeTest { @ParameterizedTest @CsvSource({"90,A","89,B","60,D","59,F"}) void maps(int score,String expected){assertEquals(expected,App.label(score));} }
