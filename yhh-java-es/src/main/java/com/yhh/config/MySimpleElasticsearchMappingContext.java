package com.yhh.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchPersistentEntity;
import org.springframework.data.util.TypeInformation;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年10月21日  
 */
@Component
@Slf4j
public class MySimpleElasticsearchMappingContext extends SimpleElasticsearchMappingContext{

    private @Nullable ApplicationContext context;
    
    @Override
    protected <T> SimpleElasticsearchPersistentEntity<?> createPersistentEntity(TypeInformation<T> typeInformation) {
        Class<T> clazz = typeInformation.getType();
        if (clazz.isAnnotationPresent(Document.class)) {
            Document document = clazz.getAnnotation(Document.class);
            String orginIndexName = document.indexName();
            try {
                // 获取代理处理器
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(document);
                // 获取私有 memberValues 属性
                Field f = invocationHandler.getClass().getDeclaredField("memberValues");
                f.setAccessible(true);
                // 获取实例的属性map
                @SuppressWarnings("unchecked")
                Map<String, Object> memberValues = (Map<String, Object>) f.get(invocationHandler);
                // 修改属性值
                String changeName = "yhh-" + orginIndexName;
                memberValues.put("indexName", changeName.toLowerCase());
                log.info("索引名包装 : {} -> {}", orginIndexName ,changeName);
            } catch (Exception e) {
                e.printStackTrace();
            }
         
        }
        SimpleElasticsearchPersistentEntity<T> persistentEntity = new SimpleElasticsearchPersistentEntity<>(
                typeInformation);
        if (context != null) {
            persistentEntity.setApplicationContext(context);
        }
        return persistentEntity;
    }
}
