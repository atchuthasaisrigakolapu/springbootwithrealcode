package com.example.springBootWithRealcode.model;

import com.example.springBootWithRealcode.exception.TuxedoPreConditionException;
import com.example.springBootWithRealcode.exception.TuxedoServiceException;
import lombok.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Value(staticConstructor = "of")
public class TuxedoResponse {

    Map<String, Object> response;

    @Value(staticConstructor = "of")
    public static class TuxedoList<T> {
        List<T> list;

        public T get(int index) {
            return index < list.size() ? list.get(index) : null;
        }
    }

    public <T> TuxedoList<T> getAsList(String key) {
        return TuxedoList.of((List<T>) response.get(key));
    }

    public <T> T getFirst(String key) {
        return getFirstOrElse(key, null);
    }

    private <T> T getFirstOrElse(String key, T defaultValue) {
        List<T> asList = this.<T>getAsList(key).list;
        return CollectionUtils.isEmpty(asList) ? defaultValue : asList.get(0);
    }
    public void checkErrors(){
       var err_counter = this.<String>getAsList("ERR_COUNTER").list;
       if(!CollectionUtils.isEmpty(this.response) || (!CollectionUtils.isEmpty(err_counter))){
        checkErrorCode(Integer.valueOf("161087645"));
           throw new TuxedoServiceException("TuxedoError","AccountDetails are updated");
       }
    }

    private void checkErrorCode(Integer errorCode) {
        var errCodes = this.<String>getAsList("ERR_CODE").list;
        if(!CollectionUtils.isEmpty(errCodes) && errCodes.contains(errorCode)){
            throw new TuxedoPreConditionException("TuxedoError","AccountDetails are updated");
        }
    }
    public static LocalDate parse(String value){
        if(Objects.nonNull(value))
            return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd"));
        else
            return null;
    }
    public static LocalDateTime parseDateTime(String value){
        if(Objects.nonNull(value))
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        else
            return null;
    }
    public static String toString(Object obj){

        if(obj instanceof Integer){
           byte value = (byte)((Integer)obj & 0xFF);
           return Character.toString((char) value);
        }
        else if(obj instanceof Byte){
            byte value = (byte) obj;
            return Character.toString((char) value);
        }
        else{
            return null;
        }
    }




}
