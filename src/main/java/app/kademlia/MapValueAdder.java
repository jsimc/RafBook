package app.kademlia;

import app.ServentInfo;

import java.util.function.BiFunction;

public class MapValueAdder implements BiFunction<ServentInfo, Integer, Integer> {

    private final int valueToAdd;

    public MapValueAdder(int valueToAdd) {
        this.valueToAdd = valueToAdd;
    }

    @Override
    public Integer apply(ServentInfo key, Integer oldValue) {
        return oldValue + valueToAdd;
    }
}
