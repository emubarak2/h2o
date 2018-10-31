package eyad.com.h2o.service;

import eyad.com.h2o.annotation.LogMethodExecutionTime;
import eyad.com.h2o.bean.DataSetWriter;
import eyad.com.h2o.bean.SearchResults;
import eyad.com.h2o.bean.SearchResultsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Service class that handle the operations on data set : read, search, write, list.
 *
 * @author eyadm@amazon.com
 */
@Slf4j
@Service
public class DataSetService {

    /**
     * this method search in specific data set about the provided search query
     *
     * @param contentId    data set id
     * @param searchString search query
     * @return will return search results wrapper class contain all search results findings
     */
    @LogMethodExecutionTime
    public SearchResultsWrapper getSearchResults(String contentId, String searchString) {

        if ( searchString == null || searchString.equals("")) {
            List<SearchResults> searchResultsList = new ArrayList<>();
           searchResultsList.add(new SearchResults("You should provide a value in search query request param.", 0L, ""));
           SearchResultsWrapper searchResultsWrapper = new SearchResultsWrapper(searchResultsList,0L);
           return searchResultsWrapper;
        }

        List<SearchResults> searchResultsList = new ArrayList<>();
        searchResultsList = searchText(contentId, searchString);

        SearchResultsWrapper searchResultsWrapper = new SearchResultsWrapper(searchResultsList, searchResultsList.size());
        return searchResultsWrapper;

    }

    /**
     * this method search in all data set tht exits in data folder about the provided search query
     *
     * @param searchString the search query
     * @return list of search results, with time consumed for search process and result count
     */
    @LogMethodExecutionTime
    public SearchResultsWrapper getSearchResults(String searchString) {
        long matchCounter = 0L;

        List<File> fileList = getFilesList();
        List<SearchResults> searchResultsList = new ArrayList<>();
        fileList.stream().forEach(d -> searchText(d.getName(), searchString).stream().forEach(s -> {
            if (s.getLineNumber() > 0) {
                searchResultsList.add(s);
            }
        }));
        matchCounter = searchResultsList.stream().filter(s -> s.getLineNumber() > 0).collect(Collectors.toList()).size();

        SearchResultsWrapper searchResultsWrapper = new SearchResultsWrapper(searchResultsList, matchCounter);

        return searchResultsWrapper;
    }


    /**
     * this method list all data sets that do exist in the data folder
     * @return list of all data sets that do exist in the data folder
     */
    @LogMethodExecutionTime
    public List<String> list() {

        List<String> dataSetFilesNames = new ArrayList<>();

        dataSetFilesNames = getFilesList().stream().map(d -> d.getName()).collect(Collectors.toList());

        return dataSetFilesNames;

    }

    /**
     * This method list the contents of the data set with the provided id
     *
     * @param contentId data set id
     * @return contents of the data set with the provided id
     */
    @LogMethodExecutionTime
    public String listContent(String contentId) {

        File file = getDataSet(contentId);

        if (file != null) {
            return readDataSet(file.getName());
        } else {
            return "No data set was found that match your content id.";
        }

    }


    /**
     * this method return all data sets files that do exist in the data folder
     * @return list of data set files
     */
    public List<File> getFilesList() {

        List<File> dataSetFiles;
        File folder = new File(System.getProperty("user.dir") + "/data");
        File[] listOfFiles = folder.listFiles();
        dataSetFiles = Arrays.asList(listOfFiles);

        return dataSetFiles;
    }


    /**
     * this method to return a file object of the provided data set id
     *
     * @param contentId data set id
     * @return file object
     */
    public File getDataSet(String contentId) {
        File file = getFilesList().stream().filter(d -> d.getName().equals(contentId)).findFirst().orElse(null);

        return file;
    }


    /**
     * this method search in the specific provided data set id the search query provided by user
     *
     * @param contentId    data set id
     * @param searchString search query
     * @return search result if found
     */
    public List<SearchResults> searchText(String contentId, String searchString) {
        List<SearchResults> searchResultsList = new ArrayList<>();

        File file = getDataSet(contentId);
        if (file != null) {

            try {
                Scanner scan = new Scanner(file);
                long lineNumber = 0;
                while (scan.hasNext()) {
                    lineNumber++;
                    String line = scan.nextLine().toLowerCase().toString();
                    if (line.contains(searchString)) {
                        searchResultsList.add(new SearchResults(file.getName(), lineNumber, line));
                    }
                }
                lineNumber = 0;
            } catch (IOException ioException) {
                log.info("Exception in searchText method.", ioException);
            }

        } else {
            searchResultsList.add(new SearchResults(contentId, 0L, "Your contents id doesn't match any data set, check content id."));
            return searchResultsList;
        }

        if (searchResultsList.size() == 0) {

            searchResultsList.add(new SearchResults(file.getName(), 0L, "No match found for your search query: " + searchString + "  in data set with id: " + contentId));
            return searchResultsList;
        } else {
            return searchResultsList;
        }

    }


    /**
     * the method write a text on the data set with the provided id
     * @param contentId data set id
     * @param addedString string to append to the data set
     */
    public void stringWriter(String contentId, String addedString) {


        try {
            File newDataSet = new File(System.getProperty("user.dir") + "/data/" + contentId);
            BufferedWriter dataSetWriter = new BufferedWriter(new FileWriter(newDataSet, true));
            dataSetWriter.append("\n" + addedString);
            dataSetWriter.flush();
            dataSetWriter.close();
        } catch (IOException exception) {
            log.info("error at stringWriter method:" + exception);
        }
    }

    /**
     * this method decided to write on an existing data set file or create new data set file if the data set id doesn't exist
     * @param dataSetWriter data set writer object
     * @return the writing process results, as information text for user
     */
    public String writeOnDataSet(DataSetWriter dataSetWriter) {

        File dataSet = getDataSet(dataSetWriter.getContentId());

        if (dataSetWriter.getContentId() == null || dataSetWriter.getContentId().equals("") || dataSetWriter.getStringToAdd() == null || dataSetWriter.getStringToAdd().equals("")) {

            return "Please fill both input fields (contentId and stringToAdd) with values, they can't be null or empty.";

        }

        if (dataSet == null) {

            stringWriter(dataSetWriter.getContentId(), dataSetWriter.getStringToAdd());

            return "Your data set with id '" + dataSetWriter.getContentId() + "' is not found a new data set was created with that name and your " +
                    "text was appended to it";
        } else {

            stringWriter(dataSetWriter.getContentId(), dataSetWriter.getStringToAdd());
            return "Your text was appended to end of the data set with id [" + dataSetWriter.getContentId() + "] successfully";
        }
    }

    /**
     * this method read the contents of the provided data set id line by line
     * @param dataSetId data set id
     * @return  contents of the provided data set id
     */
    public String readDataSet(String dataSetId) {

        StringBuilder dataSetContents = new StringBuilder();

        File file = getDataSet(dataSetId);
        if (file != null) {

            try {
                Scanner scan = new Scanner(file);
                while (scan.hasNext()) {
                    String line = scan.nextLine().toLowerCase().toString();
                    dataSetContents.append("\n" + line);
                }

            } catch (IOException ioException) {
                log.info("Exception in readDataSet method.", ioException);
            }

        } else {
            return "Your contents id doesn't match any data set, check content id.";
        }

        return dataSetContents.toString();
    }

}
