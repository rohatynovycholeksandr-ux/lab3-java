import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // --- ЗАВДАННЯ 1 ---
        System.out.println("=== Завдання 1 ===");
        System.out.print("Кількість тварин: ");
        int n = Integer.parseInt(sc.nextLine());
        Animal[] animals = new Animal[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Тип (1-Тварина, 2-Птах, 3-Ссавець, 4-Парнокопитне): ");
            int type = Integer.parseInt(sc.nextLine());
            System.out.print("Назва та вік (через пробіл): ");
            String name = sc.next();
            int age = sc.nextInt();
            sc.nextLine();

            animals[i] = switch (type) {
                case 2 -> {
                    System.out.print("Розмах крил (м): ");
                    yield new Bird(name, age, Double.parseDouble(sc.nextLine()));
                }
                case 3 -> {
                    System.out.print("Колір шерсті: ");
                    yield new Mammal(name, age, sc.nextLine());
                }
                case 4 -> {
                    System.out.print("Колір шерсті: ");
                    String fur = sc.nextLine();
                    System.out.print("Є роги (true/false): ");
                    yield new Artiodactyl(name, age, fur, Boolean.parseBoolean(sc.nextLine()));
                }
                default -> new Animal(name, age);
            };
        }

        for (Animal a : animals) {
            a.show();
            System.out.println();
        }
        sc.close();

        // --- ЗАВДАННЯ 2 (Абстрактний клас) ---
        System.out.println("\n=== Завдання 2 (Pair) ===");
        Pair[] p = { new Money(120, 75), new Money(30, 50), new Complex(4, -3), new Complex(2, 5) };
        System.out.println("Money: " + p[0].add(p[1]) + " | " + p[0].subtract(p[1]) + " | *2: " + p[0].multiply(2) + " | /5: " + p[0].divide(5) + " | eq: " + p[0].equals(new Money(120, 75)));
        System.out.println("Complex: " + p[2].add(p[3]) + " | " + p[2].subtract(p[3]) + " | *2: " + p[2].multiply(2) + " | /2: " + p[2].divide(2) + " | eq: " + p[2].equals(p[3]));

        // --- ЗАВДАННЯ 3 (Інтерфейс) ---
        System.out.println("\n=== Завдання 3 (IPair) ===");
        IPair[] ip = { new MoneyImpl(200, 90), new MoneyImpl(50, 30), new ComplexImpl(6, 4), new ComplexImpl(1.5, -2) };
        System.out.println("Money: " + ip[0].add(ip[1]) + " | " + ip[0].subtract(ip[1]) + " | *2: " + ip[0].multiply(2) + " | /10: " + ip[0].divide(10) + " | eq: " + ip[0].equals(new MoneyImpl(200, 90)));
        System.out.println("Complex: " + ip[2].add(ip[3]) + " | " + ip[2].subtract(ip[3]) + " | *4: " + ip[2].multiply(4) + " | /2: " + ip[2].divide(2) + " | eq: " + ip[2].equals(ip[3]));
    }
}

// ==================== ЗАВДАННЯ 1 ====================
class Animal {
    private String name;
    private int age;

    public Animal(String name, int age) { this.name = name; this.age = age; }
    public void show() { System.out.print("Тварина: " + name + ", вік: " + age); }
}

class Bird extends Animal {
    private double wingspan;

    public Bird(String name, int age, double wingspan) { super(name, age); this.wingspan = wingspan; }
    @Override public void show() { super.show(); System.out.print(", Птах (крила: " + wingspan + " м)"); }
}

class Mammal extends Animal {
    private String furColor;

    public Mammal(String name, int age, String furColor) { super(name, age); this.furColor = furColor; }
    @Override public void show() { super.show(); System.out.print(", Ссавець (шерсть: " + furColor + ")"); }
}

class Artiodactyl extends Mammal {
    private boolean hasHorns;

    public Artiodactyl(String name, int age, String furColor, boolean hasHorns) { super(name, age, furColor); this.hasHorns = hasHorns; }
    @Override public void show() { super.show(); System.out.print(", Парнокопитне (роги: " + (hasHorns ? "є" : "ні") + ")"); }
}

// ==================== ЗАВДАННЯ 2 ====================
abstract class Pair {
    protected double a, b;
    public Pair(double a, double b) { this.a = a; this.b = b; }
    public abstract Pair add(Pair o);
    public abstract Pair subtract(Pair o);
    public abstract Pair multiply(int k);
    public abstract Pair divide(int k);
}

class Money extends Pair {
    public Money(long hrn, int kop) {
        super((hrn * 100 + kop) / 100, (hrn * 100 + kop) % 100);
    }
    private long kop() { return (long) a * 100 + (long) b; }
    private Money fromKop(long k) { return new Money(k / 100, (int) (k % 100)); }

    @Override public Pair add(Pair o) { return fromKop(kop() + ((Money) o).kop()); }
    @Override public Pair subtract(Pair o) { return fromKop(kop() - ((Money) o).kop()); }
    @Override public Pair multiply(int k) { return fromKop(kop() * k); }
    @Override public Pair divide(int k) { return fromKop(kop() / k); }
    @Override public String toString() { return String.format("%d грн %02d коп.", (long) a, Math.abs((int) b)); }
    @Override public boolean equals(Object o) { return o instanceof Money m && kop() == m.kop(); }
}

class Complex extends Pair {
    public Complex(double re, double im) { super(re, im); }

    @Override public Pair add(Pair o) { Complex c = (Complex) o; return new Complex(a + c.a, b + c.b); }
    @Override public Pair subtract(Pair o) { Complex c = (Complex) o; return new Complex(a - c.a, b - c.b); }
    @Override public Pair multiply(int k) { return new Complex(a * k, b * k); }
    @Override public Pair divide(int k) { return new Complex(a / k, b / k); }
    @Override public String toString() { return String.format("%.1f %s %.1fi", a, b >= 0 ? "+" : "-", Math.abs(b)); }
    @Override public boolean equals(Object o) { return o instanceof Complex c && a == c.a && b == c.b; }
}

// ==================== ЗАВДАННЯ 3 ====================
interface IPair {
    IPair add(IPair o);
    IPair subtract(IPair o);
    IPair multiply(int k);
    IPair divide(int k);
}

class MoneyImpl implements IPair {
    private final long hrn;
    private final int kop;

    public MoneyImpl(long hrn, int kop) {
        long t = hrn * 100 + kop;
        this.hrn = t / 100;
        this.kop = (int) (t % 100);
    }
    private long total() { return hrn * 100 + kop; }
    private MoneyImpl of(long t) { return new MoneyImpl(t / 100, (int) (t % 100)); }

    @Override public IPair add(IPair o) { return of(total() + ((MoneyImpl) o).total()); }
    @Override public IPair subtract(IPair o) { return of(total() - ((MoneyImpl) o).total()); }
    @Override public IPair multiply(int k) { return of(total() * k); }
    @Override public IPair divide(int k) { return of(total() / k); }
    @Override public String toString() { return String.format("%d грн %02d коп.", hrn, Math.abs(kop)); }
    @Override public boolean equals(Object o) { return o instanceof MoneyImpl m && total() == m.total(); }
}

class ComplexImpl implements IPair {
    private final double re, im;
    public ComplexImpl(double re, double im) { this.re = re; this.im = im; }

    @Override public IPair add(IPair o) { ComplexImpl c = (ComplexImpl) o; return new ComplexImpl(re + c.re, im + c.im); }
    @Override public IPair subtract(IPair o) { ComplexImpl c = (ComplexImpl) o; return new ComplexImpl(re - c.re, im - c.im); }
    @Override public IPair multiply(int k) { return new ComplexImpl(re * k, im * k); }
    @Override public IPair divide(int k) { return new ComplexImpl(re / k, im / k); }
    @Override public String toString() { return String.format("%.1f %s %.1fi", re, im >= 0 ? "+" : "-", Math.abs(im)); }
    @Override public boolean equals(Object o) { return o instanceof ComplexImpl c && re == c.re && im == c.im; }
}
    


