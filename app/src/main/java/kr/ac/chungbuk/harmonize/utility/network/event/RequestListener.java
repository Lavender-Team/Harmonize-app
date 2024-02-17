package kr.ac.chungbuk.harmonize.utility.network.event;

public interface RequestListener<T> {
    public void getResult(T object);
}
