package com.bsb.web.dao;

import com.bsb.web.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zeng
 */
public interface IOrderItemMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> getByOrderNoUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);

    List<OrderItem> getByOrderNo(@Param("orderNo") Long orderNo);

    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);

}