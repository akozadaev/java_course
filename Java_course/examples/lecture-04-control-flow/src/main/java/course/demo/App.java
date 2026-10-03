package course.demo;
public class App {
    static String label(int score) { if (score < 0 || score > 100) throw new IllegalArgumentException("score");
        return switch (score / 10) { case 10, 9 -> "A"; case 8 -> "B"; case 7 -> "C"; case 6 -> "D"; default -> "F"; }; }
    public static void main(String[] args) { for (int score : new int[]{59,60,89,90,100}) System.out.println(score+" -> "+label(score)); }
}
