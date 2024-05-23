package tests;

import ctblc.Cocktailbar;
import ctblc.LongCounter;

import java.util.HashMap;

public class TestDingsis {
    public static void main(String[] args) {
        System.out.println("short:");

        Cocktailbar.getAvailableDrinks("resources/cocktailmix_short.txt").forEach(System.out::println);
        System.out.println();
        System.out.println("normal:");
        Cocktailbar.getAvailableDrinks("resources/cocktailmix.txt").forEach(System.out::println);

        System.out.println();
        System.out.println("unknown:");
        Cocktailbar.getAvailableDrinks("resources/cocktailmix_unknown.txt").forEach(System.out::println);
        System.out.println("empty:");
        Cocktailbar.getAvailableDrinks("resources/empty.txt").forEach(System.out::println);

        LongCounter<String> c1 = new LongCounter<>();
        c1.put("hugo");
        c1.put("hugo");
        c1.put("detlef");
        c1.put("detlef");
        c1.put("detlef");
        c1.put("detlef");
        c1.put("detlef");
        c1.put("hugo", 4);
        System.out.println("c1: " + c1); // Ausgabe: c1: {detlef=5, hugo=6}
        LongCounter<String> c2 = new LongCounter<>();
        c2.put("hugo");
        c2.put("hugo");
        c2.put("franz");
        c2.put("franz", 4);
        c2.put("walter", 2);
        System.out.println("c2: " + c2); // Ausgabe: c2: {franz=5, hugo=2, walter=2}
        c1.subtractAll(new HashMap<>(c2));  // Ausgabe: c1.subtractAll(c2): {detlef=5, franz=-5, hugo=4, walter=-2}
        System.out.println("c1.subtractAll(c2): " + c1);
        System.out.println(c1.mostCommon(3)); // Ausgabe: [detlef=5, hugo=4, walter=-2]
        LongCounter<Character> cS = LongCounter.fromString("Hallo Welt? Wie geht es Dir?");
        cS.putAll(LongCounter.fromString("Super, warum fragst Du?"));  // mostCommon: [ =8, e=5, r=4, ?=3, a=3, l=3, t=3, u=3, D=2, W=2, g=2, i=2, // s=2, ,=1, H=1, S=1, f=1, h=1, m=1, o=1, p=1, w=1]
        System.out.println("mostCommon: " + cS.mostCommon(99));  // Ausgabe: lessCommon: [w=1, p=1]
        System.out.println("lessCommon: " + cS.lessCommon(2));
        LongCounter<Character> s = LongCounter.fromString("anna");
        s.subtractAll(LongCounter.fromString("anna"));
        System.out.println(s); // Ausgabe: {a=0, n=0}
        s.clearZeros();
        System.out.println(s); // Ausgabe: {}
    }
}
