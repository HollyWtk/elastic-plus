package com.yhh.demo.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.yhh.demo.entity.UserDemo;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年10月26日  
 */
public interface UserDemoRepositories extends ElasticsearchRepository<UserDemo, Long>{

}
