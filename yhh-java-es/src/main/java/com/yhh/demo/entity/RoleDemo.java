package com.yhh.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年10月26日  
 */
@Document(indexName = "roleDemo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDemo {

    @Id
    private long id;
    
    @Field(type = FieldType.Keyword)
    private String roleName;
    
    @Field(type = FieldType.Text)
    private String desc;
}
