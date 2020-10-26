package com.yhh.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年10月23日  
 */
public interface EService<T> {

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    T save(T entity);

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     */

    Iterable<T> saveBatch(List<T> entityList);

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     */
    boolean saveBatch(List<T> entityList, int batchSize);

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    boolean removeById(Serializable id);

    /**
     * 根据 entity 条件，删除记录
     *
     * @param queryWrapper 实体包装类
     */
    void remove(EsWrapper queryWrapper);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表
     */
    boolean removeByIds(Collection<? extends Serializable> idList);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    T getById(Serializable id);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     */
    List<T> listByIds(Collection<? extends Serializable> idList);


    /**
     * 根据 Wrapper，查询一条记录 <br/>
     * <p>结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")</p>
     *
     * @param queryWrapper 实体对象封装操作类
     */
    T getOne(EsWrapper queryWrapper);

    /**
     * 查询总记录数
     *
     * @see Wrappers#emptyWrapper()
     */
    long count();

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类
     */
    long count(EsWrapper queryWrapper);

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类
     */
    List<T> list(EsWrapper queryWrapper);

    /**
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    List<T> list();

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类
     */
    Page<T> page(Pageable page, EsWrapper queryWrapper);

    /**
     * 无条件翻页查询
     *
     * @param page 翻页对象
     */
    Page<T> page(Pageable page);
    
    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类
     * @param total 是否计算总数
     */
    Page<T> page(Pageable page, EsWrapper queryWrapper,boolean total);

    /**
     * 无条件翻页查询
     *
     * @param page 翻页对象
     * @param total 是否计算总数
     */
    Page<T> page(Pageable page,boolean total);

}
