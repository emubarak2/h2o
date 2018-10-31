package eyad.com.h2o.utility;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * utility class tht provide method to deal with object and json
 * @author eyadm@amazon.com
 */
@Slf4j
public class JsonUtility {


    /**
     * this method convert any provided object to json string
     * @param object the object need to be converted to json string format
     * @return json string format
     */
    public static String convertObjectToJson(Object object) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            log.error("error at converting object to JSON: ", exception);
        }
        return null;
    }
}
