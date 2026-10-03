package course.demo;
public final class App {
    public static long applyDiscount(long price, int percent) {
        if (price < 0 || percent < 0 || percent > 100) throw new IllegalArgumentException("Invalid price or percent");
        return price * (100 - percent) / 100;
    }
    public static void main(String[] args) { System.out.println(applyDiscount(10_000, 15)); }
}
