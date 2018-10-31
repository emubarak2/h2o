package eyad.com.h2o.bean;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * search result bean class that contains, file name, line number, line text
 */
@Data
public class SearchResults {

    @JsonProperty("File_Name")
    String fileName;

    Long LineNumber;

    @JsonProperty("Search_Query")
    String foundInLine;


    public SearchResults(String fileName, Long lineNumber, String foundInLine) {
        this.fileName = fileName;
        this.LineNumber = lineNumber;
        this.foundInLine = foundInLine;

    }
}
