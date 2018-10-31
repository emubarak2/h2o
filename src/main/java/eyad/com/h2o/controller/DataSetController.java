package eyad.com.h2o.controller;


import eyad.com.h2o.bean.DataSetWriter;
import eyad.com.h2o.bean.SearchResultsWrapper;
import eyad.com.h2o.service.DataSetService;
import eyad.com.h2o.utility.JsonUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * explore data set controller
 * @author eyadm@amazon.com
 */

@RestController
@Slf4j
@RequestMapping("/dataset")
public class DataSetController {


    @Autowired
    private DataSetService dataSetService;

    /**
     * this a controller for searching in all data sets that do exist in data folder
     * @param searchText search query
     * @return search results object wrapper : match count, line numbers, data set where the search query was found
     */
    @RequestMapping(path = "/search/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchAllDataSet(@RequestParam("search_query") String searchText) {
        return JsonUtility.convertObjectToJson(dataSetService.getSearchResults(searchText));
    }

    /**
     * this a controller for searching in a specific provided data sets id
     * @param searchText search query
     * @return search results object wrapper : match count, line numbers, data set where the search query was found
     */
    @RequestMapping(path = "/search/{contentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResultsWrapper searchSpecificDataSet(@PathVariable("contentId") String contentId, @RequestParam("search_text") String searchText) {

        return dataSetService.getSearchResults(contentId, searchText);
    }


    /**
     * this method list all data sets that do exist in the data folder
     * @return list of all data sets that do exist in the data folder
     */
    @RequestMapping(path = "/list/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> list() {
        List<String> dataSetNames = dataSetService.list();
        return dataSetNames;

    }

    /**
     * This method list the contents of the data set with the provided id
     * @param contentId data set id
     * @return contents of the data set with the provided id
     */
    @RequestMapping(path = "/list/{contentId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String listByContentId(@PathVariable("contentId") String contentId) {

        return dataSetService.listContent(contentId);

    }

    /**
     * this method decided to write on an existing data set file or create new data set file if the data set id doesn't exist
     * @param dataSetWriter data set writer object
     * @return the writing process results, as information text for user
     */ 
    @RequestMapping(path = "/write/", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public String write(@RequestBody DataSetWriter dataSetWriter) {

        return dataSetService.writeOnDataSet(dataSetWriter);

    }

}
