package eyad.com.h2o.aspect;

import eyad.com.h2o.bean.SearchResultsWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * this aspect oriented class record the executed time for any method annotated with @LogMethodExecutionTime
 * @author eyadm@amazon.com
 */
@Component
@Aspect
@Slf4j
public class RecordMethodExecutionTimeAspect {

    long startTime = 0L;
    long endTime = 0L;

    /**
     * this aspect method record the start time of the method before returning
     *
     * @throws Throwable
     */
    @Around(value = "@annotation(eyad.com.h2o.annotation.LogMethodExecutionTime)")
    public Object logExecutionStartTIme(ProceedingJoinPoint jointPoint) throws Throwable {
        this.startTime = System.nanoTime();
        Object returnValue = jointPoint.proceed();
        this.endTime = System.nanoTime();

        if (returnValue instanceof SearchResultsWrapper) {
            SearchResultsWrapper searchResultsWrapper = (SearchResultsWrapper) returnValue;
            searchResultsWrapper.setExecutionTime("Search execution time in seconds was : " + Precision.round(((System.nanoTime() - startTime) / (double) 1000000000L), 3));
            returnValue = searchResultsWrapper;
        }


        log.info("The method (" + jointPoint.getSignature().getName() + ") execution time in seconds was : " + Precision.round(((System.nanoTime() - startTime) / (double) 1000000000L), 3));
        return returnValue;

    }

}

