package ioo.tpo.maquina;

import java.util.*;

public enum Fruta {
    Frutilla(0),
    Banana(1),
    Guinda(6),
    Manzana(4),
    Sandia(5),
    Uva(3);

    private static final List<Fruta> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random rnd = new Random();

    private final int id;

    Fruta(int id) {
        this.id = id;
    }

    public static Fruta getRandom()  {
        return VALUES.get(rnd.nextInt(SIZE));
    }

    public static Optional<Fruta> getById(int id)  {
        return VALUES.stream().filter(f -> f.getId() == id).findAny();
    }

    public int getId() {
        return id;
    }
}