package eyad.com.h2o.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * data set writer bean class that wrap two fields : contentId, StringToAdd
 * @author eyadm@amazon.com
 */
@Slf4j
@Data
public class DataSetWriter {

    @JsonProperty("content_id")
    String contentId;

    @JsonProperty("text_to_add")
    String stringToAdd;

}
