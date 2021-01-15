package com.gmail.podkutin.dmitry.votingsystem.util.exception;

import com.gmail.podkutin.dmitry.votingsystem.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

import java.util.Map;
import java.util.Objects;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionInfoHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionInfoHandler.class);
    public static final String EXCEPTION_DUPLICATE_VOTE = "You already voted today";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT = "A restaurant with this name already exists";
    public static final String EXCEPTION_DUPLICATE_DISH = "A dish with this name for this restaurant already exists in this date";
    private final Map<String, String> sqlLocalizedMessage = Map.of("VOTE_USER_ID_DATE_U_INDEX",EXCEPTION_DUPLICATE_VOTE,
            "RESTAURANT_UNIQUE_NAME_INDEX", EXCEPTION_DUPLICATE_RESTAURANT,
            "DISH_UNIQUE_RESTAURANT_ID_DATE_NAME_IDX",EXCEPTION_DUPLICATE_DISH);
    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, ErrorType.DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({BindException.class})
    public ErrorInfo validationError(HttpServletRequest req, BindException e) {
        return logAndGetErrorInfo(req, new ValidationException(""), false, ErrorType.VALIDATION_ERROR, e.getBindingResult().getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage())).toArray(String[]::new));
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        for (String key : sqlLocalizedMessage.keySet()) {
            if (Objects.requireNonNull(e.getRootCause()).getLocalizedMessage().contains(key)) {
                return logAndGetErrorInfo(req, new VotingException(sqlLocalizedMessage.get(key)), false, ErrorType.VALIDATION_ERROR);
            }
        }
        return logAndGetErrorInfo(req, e, true, ErrorType.DATA_ERROR);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(VotingException.class)
    public ErrorInfo votingError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.VOTING_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, ErrorType.APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String... errorDetails) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        errorDetails = errorDetails.length == 0 ? new String[]{rootCause.getMessage()} : errorDetails;
        if (logException) {
            LOG.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            LOG.warn("{} at request  {}: {}", errorType, req.getRequestURL(), errorDetails);
        }
        return new ErrorInfo(req.getRequestURL(), errorType, errorDetails);
    }
}