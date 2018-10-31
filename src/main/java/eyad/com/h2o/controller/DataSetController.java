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


    @RequestMapping(path = "/search/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchAllDataSet(@RequestParam("search_query") String searchText) {
        return JsonUtility.convertObjectToJson(dataSetService.getSearchResults(searchText));
    }

    @RequestMapping(path = "/search/{contentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResultsWrapper searchSpecificDataSet(@PathVariable("contentId") String contentId, @RequestParam("search_text") String searchText) {

        return dataSetService.getSearchResults(contentId, searchText);
    }


    @RequestMapping(path = "/list/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> list() {
        List<String> dataSetNames = dataSetService.list();
        return dataSetNames;

    }

    @RequestMapping(path = "/list/{contentId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String listByContentId(@PathVariable("contentId") String contentId) {

        return dataSetService.listContent(contentId);

    }

    @RequestMapping(path = "/write/", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public String write(@RequestBody DataSetWriter dataSetWriter) {

        return dataSetService.writeOnDataSet(dataSetWriter);

    }

}
