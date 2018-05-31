package com.mapper;

import com.model.Companys;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CompanysMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Companys record);

    int insertSelective(Companys record);

    Companys selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Companys record);

    int updateByPrimaryKey(Companys record);
}