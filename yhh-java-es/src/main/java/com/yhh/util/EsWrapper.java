package com.yhh.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.elasticsearch.search.sort.SortOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年10月23日  
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EsWrapper {
    
    private List<ParamValue> eqValue = new ArrayList<>();
    
    private List<ParamValue> shouldValue = new ArrayList<>();
    
    private List<ParamValue> ltValue = new ArrayList<>();
    
    private List<ParamValue> gtValue = new ArrayList<>();
    
    private List<ParamValue> neValue = new ArrayList<>();
    
    private List<ParamValue> likeValue = new ArrayList<>();
    
    private List<ParamValue> orderValue = new ArrayList<>();
    
    public EsWrapper eq(String field,Object value) {
        eqValue.add(EsWrapper.of(field, value));
        return this;
    }
    
    public EsWrapper ne(String field,Object value) {
        neValue.add(EsWrapper.of(field, value));
        return this;
    }
    
    public EsWrapper should(String field,Object value) {
        shouldValue.add(EsWrapper.of(field, value));
        return this;
    }
    
    public EsWrapper lt(String field,Object value) {
        ltValue.add(EsWrapper.of(field, value));
        return this;
    }
    
    public EsWrapper gt(String field,Object value) {
        gtValue.add(EsWrapper.of(field, value));
        return this;
    }
    
    public EsWrapper like(String field,Object value) {
        likeValue.add(EsWrapper.of(field, value));
        return this;
    }
    
    public EsWrapper order(String field,SortOrder order) {
        orderValue.add(EsWrapper.of(field, order));
        return this;
    }
    
    private static ParamValue of(String key,Object value){
        return ParamValue.builder()
                .field(key)
                .value(Objects.isNull(value) ? "" : value).build();
    }
    
}
@Data
@Builder
class ParamValue{
    String field;
    
    Object value ;
}
