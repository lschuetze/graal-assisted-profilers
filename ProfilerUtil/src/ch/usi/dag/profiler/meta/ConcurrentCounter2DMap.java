package ch.usi.dag.profiler.meta;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("serial")
public class ConcurrentCounter2DMap extends ConcurrentHashMap<String, ConcurrentCounterMap> {

    public void increment(String outerKey, String innerKey) {
        computeIfAbsent(outerKey, input -> new ConcurrentCounterMap()).increment(innerKey);
    }

}
