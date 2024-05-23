package ctblc;

import java.util.*;


/**
 * Die Klasse <b>ctblc.LongCounter</b> stellt einen Zähler für beliebige Werte zur Verfügung.<br><br>
 * <b>Verwendungs-Beispiele:</b>
 * <code>
 * <br>        ctblc.LongCounter&lt;String&gt; c1 = new ctblc.LongCounter&lt;&gt;();
 * <br>        c1.put("hugo");
 * <br>        c1.put("hugo");
 * <br>        c1.put("detlef");
 * <br>        c1.put("detlef");
 * <br>        c1.put("detlef");
 * <br>        c1.put("detlef");
 * <br>        c1.put("detlef");
 * <br>        c1.put("hugo", 4);
 * <br>        System.out.println("c1: " +c1); // Ausgabe: c1: {detlef=5, hugo=6}
 * <br>
 * <br>        ctblc.LongCounter&lt;String&gt; c2 = new ctblc.LongCounter&lt;&gt;();
 * <br>        c2.put("hugo");
 * <br>        c2.put("hugo");
 * <br>        c2.put("franz");
 * <br>        c2.put("franz", 4);
 * <br>        c2.put("walter", 2);
 * <br>        System.out.println("c2: " + c2); // Ausgabe: c2: {franz=5, hugo=2, walter=2}
 * <br>
 * <br>        c1.subtractAll(new HashMap&lt;&gt;(c2));
 * <br>
 * <br>        // Ausgabe: c1.subtractAll(c2): {detlef=5, franz=-5, hugo=4, walter=-2}
 * <br>        System.out.println("c1.subtractAll(c2): " + c1);
 * <br>
 * <br>        System.out.println(c1.mostCommon(3)); // Ausgabe: [detlef=5, hugo=4, walter=-2]
 * <br>
 * <br>        ctblc.LongCounter&lt;Character&gt; cS = ctblc.LongCounter.fromString("Hallo Welt? Wie geht es Dir?");
 * <br>        cS.putAll(ctblc.LongCounter.fromString("Super, warum fragst Du?"));
 * <br>
 * <br>        // mostCommon: [ =8, e=5, r=4, ?=3, a=3, l=3, t=3, u=3, D=2, W=2, g=2, i=2,
 * <br>        //               s=2, ,=1, H=1, S=1, f=1, h=1, m=1, o=1, p=1, w=1]
 * <br>        System.out.println("mostCommon: " + cS.mostCommon(99));
 * <br>
 * <br>        // Ausgabe: lessCommon: [w=1, p=1]
 * <br>        System.out.println("lessCommon: " + cS.lessCommon(2));
 * <br>
 * <br>        ctblc.LongCounter&lt;Character&gt; s = ctblc.LongCounter.fromString("anna");
 * <br>        s.subtractAll(ctblc.LongCounter.fromString("anna"));
 * <br>        System.out.println(s); // Ausgabe: {a=0, n=0}
 * <br>        s.clearZeros();
 * <br>        System.out.println(s); // Ausgabe: {}
 * </code>
 */
public class LongCounter<K> extends HashMap<K, Long> {
    /**
     * Standard-Vergleichsoperator für Keys, die Comparable implementieren,
     * wird z.B. für {@link #mostCommon(int)} verwendet
     */
    private Comparator<K> keyComparator = (a, b) -> ((Comparable) a).compareTo(b);


    /**
     * Vergleichsoperator für Value- und Key-Paare,
     * so dass zuerst nach der Value und dann nach dem Key gereiht wird.<br>
     * Der Key-Comparator kann mittels {@link #LongCounter(Comparator)} gesetzt werden.
     * wird z.B. für {@link #mostCommon(int)} verwendet
     */
    private final Comparator<Entry<K, Long>> entryComparator = (a, b) -> {
        int d = Long.compare(a.getValue(), b.getValue());
        if (d != 0) {
            return d;
        }

        return keyComparator.compare(b.getKey(), a.getKey());
    };


    /**
     * Der keyComperator wird zum Sortieren für {@link #mostCommon(int)}} und
     * {@link #lessCommon(int)} vewendet.
     *
     * @param keyComparator zum Sortieren der Keys
     */
    public LongCounter(Comparator<K> keyComparator) {
        this.keyComparator = keyComparator;
    }


    /**
     * Erzeugt einen <tt>ctblc.LongCounter</tt> mit der Standardkapazität und dem Standard-Loadfaktor<br>
     */
    public LongCounter() {
        // nothing to do
    }

    /**
     * Erzeugt eine ctblc.LongCounter aus einer beliebigen Map die Long-Values hat
     *
     * @param m die Map mit den schon gezählten Werden
     */
    public LongCounter(Map<? extends K, Long> m) {
        super(m);
    }


    /**
     * Erzeugt einen ctblc.LongCounter aus dem Objekt-Array
     *
     * @param k das Array mit den zu zählenden Elementen
     */
    @SafeVarargs
    public LongCounter(K... k) {
        super();
        Arrays.asList(k).forEach(t -> {
            if (containsKey(t)) {
                put(t, get(t) + 1);
            } else {
                put(t, 1L);
            }
        });

    }


    /**
     * Erzeugt einen ctblc.LongCounter aus der Collection
     *
     * @param k das Array mit den zu zählenden Elementen
     */
    public LongCounter(Collection<K> k) {
        super();
        k.forEach(t -> {
            if (containsKey(t)) {
                put(t, get(t) + 1);
            } else {
                put(t, 1L);
            }
        });
    }


    /**
     * Erzeugt einen ctblc.LongCounter aus einem String und zählt alle Zeichen in dem String.
     *
     * @param s die zu zählenden Zeichen
     * @return der ctblc.LongCounter, der alle Zeichen gezählt hat
     */
    public static LongCounter<Character> fromString(CharSequence s) {
        LongCounter<Character> counter = new LongCounter<>();
        for (int i = 0; i < s.length(); i++) {
            counter.put(s.charAt(i));
        }
        return counter;
    }


    /**
     * Erhöht den Counter um "value" für den Key.
     *
     * @param key   der Key, dessen value verändert wird
     * @param value der Wert, um den der Counter verändert werden soll
     * @return der alte Wert des Counters oder null
     */
    public Long put(K key, long value) {
        long x = getOrDefault(key, 0L);
        super.put(key, value + x);
        return x == 0 ? null : x;
    }


    /**
     * Erhöht den Counter um Eins für den Key.
     *
     * @param key der Key, dessen value verändert wird
     * @return der alte Wert des Counters oder null
     */
    public Long put(K key) {
        return put(key, 1);
    }


    /**
     * Erhöht (addiert) die Werte aus der Map "m" zum ctblc.LongCounter.
     *
     * @param m die Map mit den zu addierenden Werten
     */
    public void putAll(Map<? extends K, ? extends Long> m) {
        m.forEach((k, v) -> put(k, (long) v));

    }


    /**
     * Vermindert (subtrahiert) die Werte aus der Map "m" zum ctblc.LongCounter.
     *
     * @param m die Map mit den zu subtrahierenden Werten
     */
    public void subtractAll(Map<? extends K, ? extends Long> m) {
        m.forEach((k, v) -> put(k, -v));
    }


    /**
     * Liefert (max.) die "n"-häufigsten Werte, sortiert nach Häufigkeit und danach nach den Keys.
     *
     * @param n die Anzahl der Werte
     * @return die Liste mit den Key/Counter-Paaren
     */
    public List<Entry<K, Long>> mostCommon(int n) {
        List<Entry<K, Long>> entrys = new ArrayList<>(entrySet());
        entrys.sort(entryComparator.reversed());

        return entrys.subList(0, Math.min(n, size()));
    }

    /**
     * Liefert (max.) die "n"-seltensten Werte, sortiert nach Häufigkeit und danach nach den Keys.
     *
     * @param n die Anzahl der Werte
     * @return die Liste mit den Key/Counter-Paaren
     */
    public List<Entry<K, Long>> lessCommon(int n) {
        List<Entry<K, Long>> entrys = new ArrayList<>(entrySet());
        entrys.sort(entryComparator);

        return entrys.subList(0, Math.min(n, size()));
    }


    /**
     * Löscht alle Einträge aus der Map, deren Counter == 0 ist.
     */
    public void clearZeros() {
        this.entrySet().removeIf(entry -> entry.getValue() == 0);
    }
}
