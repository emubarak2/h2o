package eyad.com.h2o.bean;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * result wrapper class that contains :match counter, execution time, search results list
 * @author  eyadm@amazon.comm
 */
@Data
public class SearchResultsWrapper {

    @JsonProperty("Number of times the search query was found in a data set")
    long matchCounter;

    @JsonProperty("Search execution time")
    String executionTime="";

    @JsonProperty("Search Results List")
    List<SearchResults> searchResultsList = new ArrayList<>();


    public SearchResultsWrapper(List<SearchResults> searchResultsList, long matchCounter) {
        this.searchResultsList = searchResultsList;
        this.matchCounter = matchCounter;
    }
}
