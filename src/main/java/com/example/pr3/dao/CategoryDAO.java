package com.example.pr3.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.pr3.dto.Category1;
import com.example.pr3.dto.Category2;
import com.example.pr3.dto.Category3;

import java.util.List;

@Mapper
public interface CategoryDAO {

    // Cat1 전체 조회
	@Select("SELECT cat1_id, cat1_name FROM category1 ORDER BY cat1_id")
    List<Category1> selectCat1();
    
    @Select("SELECT cat2_id, cat2_name FROM category2 ORDER BY cat2_id")
    List<Category2> selectCat2();
    
    @Select("SELECT cat3_id, cat3_name FROM category3 ORDER BY cat3_id")
    List<Category3> selectCat3();
}
