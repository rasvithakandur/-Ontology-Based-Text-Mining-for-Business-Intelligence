import java.util.Comparator;

/**
 * Created by Karthik on 11/20/17.
 */
public class Cell{
    String a;
    String b;
    String c;
    String d;
    String e;
    String f;
    String g;

    public Cell(String a, String b, String c, String d, String e, String f, String g) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
    }

    public Cell(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Cell(String a, String b, String c, String d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) {
            return false;
        }
        Cell that = (Cell) o;
        return (a == that.a);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Integer.parseInt(a);
        return result;
    }


}