import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class UniqueMap {

    /**
     * concurrent map so that all threads can modify the map
     */
    
    public static Map<String, Integer> uniqueUrls = new ConcurrentHashMap<String, Integer>();
    public static Map<String, Integer> uniqueUrlsOut = new ConcurrentHashMap<String, Integer>();
    public static Integer	count = new Integer(0);
    
}
