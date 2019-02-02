/**
 * Created by Lorenzo on 06/10/2017!
 */

public abstract class Field<T> {
    protected T value;
    public Field() {}
    public Field(T val) { this.value = val; }

    public static class ReadyOnly<T> extends Field<T> {
        public T get() { return this.value; }
    }

    public static class ReadAndWrite<T> extends ReadyOnly<T> {
        public void set(T val) { this.value = val; }
    }
}
