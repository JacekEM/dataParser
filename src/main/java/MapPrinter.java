import java.util.Map;

class MapPrinter<K, V> {

    void readMap(Map<K, V> data) {
        for (Map.Entry<K, V> entry : data.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

}
