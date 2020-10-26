package com.yhh.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.Assert;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年10月23日  
 */
@SuppressWarnings("unchecked")
public class ESeviceImpl<T> implements EService<T>{
    
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    
    private Class<?> persistentClass = ReflectionKit.getSuperClassGenericType(getClass(), 0);
    
    @Override
    public T save(T entity) {
        return elasticsearchRestTemplate.save(entity);
    }

    @Override
    public Iterable<T> saveBatch(List<T> entityList) {
        return elasticsearchRestTemplate.save(entityList);
    }

    @Override
    public boolean saveBatch(List<T> entityList, int batchSize) {
        List<List<T>> lists = averageAssign(entityList, batchSize);
        lists.forEach(k -> elasticsearchRestTemplate.save(k));
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        Assert.notNull(id, "id must not be null");
        return Objects.equals(id, elasticsearchRestTemplate.delete(id.toString(), persistentClass));
    }

    @Override
    public void remove(EsWrapper queryWrapper) {
        elasticsearchRestTemplate.delete(wrapper(queryWrapper).build(), persistentClass, getIndexByClass(persistentClass));
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        Assert.notNull(idList,"idList must not be null");
        idList.forEach(k -> elasticsearchRestTemplate.delete(k.toString(), persistentClass));
        return false;
    }

    @Override
    public T getById(Serializable id) {
        Assert.notNull(id,"id must not be null");
        return (T) elasticsearchRestTemplate.get(id.toString(), persistentClass);
    }

    @Override
    public List<T> listByIds(Collection<? extends Serializable> idList) {
        return  (List<T>) idList.stream()
                .map(k -> elasticsearchRestTemplate.get(k.toString(), persistentClass))
                .collect(Collectors.toList());
    }

    @Override
    public T getOne(EsWrapper queryWrapper) {
        return (T) elasticsearchRestTemplate
                .searchOne(wrapper(queryWrapper).build(), persistentClass, getIndexByClass(persistentClass)).getContent();
    }

    @Override
    public long count() {
        return elasticsearchRestTemplate.count(new NativeSearchQuery(QueryBuilders.matchAllQuery()), persistentClass);
    }

    @Override
    public long count(EsWrapper queryWrapper) {
        return elasticsearchRestTemplate.count(wrapper(queryWrapper).build(), persistentClass);
    }

    @Override
    public List<T> list(EsWrapper queryWrapper) {
        return (List<T>) elasticsearchRestTemplate
                .search(wrapper(queryWrapper).build(), persistentClass)
                .stream().map(k -> k.getContent()).collect(Collectors.toList());
    }

    @Override
    public List<T> list() {
        return (List<T>) elasticsearchRestTemplate.search(new NativeSearchQuery(QueryBuilders.matchAllQuery()), persistentClass)
                .stream().map(k -> k.getContent())
                .collect(Collectors.toList());
    }

    @Override
    public Page<T> page(Pageable page, EsWrapper queryWrapper) {
        List<T> list = (List<T>) elasticsearchRestTemplate.search(wrapper(queryWrapper)
                .withPageable(page).build(), persistentClass)
                .stream().map(k -> k.getContent())
                .collect(Collectors.toList());
        Page<T> result = new Page<>(page.getPageNumber(), page.getPageSize(), list);
        return result;
    }

    @Override
    public Page<T> page(Pageable page) {
        List<T> list = (List<T>) (List<T>) elasticsearchRestTemplate.search(new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withPageable(page).build(), persistentClass)
                .stream().map(k -> k.getContent())
                .collect(Collectors.toList());
        Page<T> result = new Page<>(page.getPageNumber(), page.getPageSize(), list);
        return result;
    }
    
    @Override
    public Page<T> page(Pageable page, EsWrapper queryWrapper, boolean total) {
        List<T> list = (List<T>) elasticsearchRestTemplate.search(wrapper(queryWrapper)
                .withPageable(page).build(), persistentClass)
                .stream().map(k -> k.getContent())
                .collect(Collectors.toList());
        Page<T> result = new Page<>(page.getPageNumber(), page.getPageSize(), list);
        if(total) {
            result.setTotal(this.count(queryWrapper));
        }
        return result;
    }

    @Override
    public Page<T> page(Pageable page, boolean total) {
        List<T> list = (List<T>) elasticsearchRestTemplate.search(new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withPageable(page).build(), persistentClass)
                .stream().map(k -> k.getContent())
                .collect(Collectors.toList());
        Page<T> result = new Page<>(page.getPageNumber(), page.getPageSize(), list);
        if(total) {
            result.setTotal(this.count());
        }
        return result;
    }

    
    /**
     * 将一个List均分成n个list,主要通过偏移量来实现的
     *
     * @param source 源集合
     * @param limit 最大值
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int limit) {
        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<T>> result = new ArrayList<>();
        int listCount = (source.size() - 1) / limit + 1;
        int remaider = source.size() % listCount; // (先计算出余数)
        int number = source.size() / listCount; // 然后是商
        int offset = 0;// 偏移量
        for (int i = 0; i < listCount; i++) {
            List<T> value;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
    
    private NativeSearchQueryBuilder wrapper(EsWrapper wrapper) {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        wrapper.getEqValue().forEach(k -> boolBuilder.must(QueryBuilders.termQuery(k.getField(),k.getValue())));
        wrapper.getGtValue().forEach(k -> boolBuilder.must(QueryBuilders.rangeQuery(k.getField()).gt(k.getValue())));
        wrapper.getLtValue().forEach(k -> boolBuilder.must(QueryBuilders.rangeQuery(k.getField()).lt(k.getValue())));
        wrapper.getLikeValue().forEach(k -> boolBuilder.must(QueryBuilders.wildcardQuery(k.getField(),"*" + k.getValue() + "*")));
        wrapper.getNeValue().forEach(k -> boolBuilder.mustNot(QueryBuilders.termQuery(k.getField(),k.getValue())));
        wrapper.getShouldValue().forEach(k -> boolBuilder.should(QueryBuilders.termQuery(k.getField(),k.getValue())));
        builder.withQuery(boolBuilder);
        wrapper.getOrderValue().forEach(k -> builder.withSort(SortBuilders.fieldSort(k.getField()).order((SortOrder) k.getValue())));
        return builder;
    }
    
    private IndexCoordinates getIndexByClass(Class<?> clazz) {
        IndexCoordinates index = null;
        if(clazz.isAnnotationPresent(Document.class)) {
            Document doc = clazz.getAnnotation(Document.class);
            index = IndexCoordinates.of(doc.indexName());
        }
        return index;
    }

}
