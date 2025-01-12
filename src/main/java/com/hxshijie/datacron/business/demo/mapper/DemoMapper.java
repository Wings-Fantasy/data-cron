package com.hxshijie.datacron.business.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DemoMapper {

    @Select("SELECT 1")
    Integer selectOne();
}
