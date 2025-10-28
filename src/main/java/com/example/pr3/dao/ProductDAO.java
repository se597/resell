package com.example.pr3.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.pr3.dto.Category1;
import com.example.pr3.dto.Category2;
import com.example.pr3.dto.Category3;
import com.example.pr3.dto.ProductDTO;


@Mapper
public interface ProductDAO {
	List<Category1> categories1();	
	List<Category2> categories2(int id);
	List<Category3> categories3(int id); //카테고리 불러오기
	
	List<ProductDTO> product_all();
	List<ProductDTO> product_cat1(int id);
	
	
	//페이징용
	List<ProductDTO> product_cat1_paged(@Param("cat1id") int cat1id,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);
	@Select("SELECT COUNT(*) FROM pr3 WHERE cat1_id = #{cat1id}")
    int countByCat1(int cat1id);
	List<ProductDTO> product_cat2_paged(@Param("cat2id") int cat2id,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

	@Select("SELECT COUNT(*) FROM pr3 WHERE cat2_id = #{cat2id}")
	int countByCat2(int cat2id);

	// 🔹 소분류별 (cat3)
	List<ProductDTO> product_cat3_paged(@Param("cat3id") int cat3id,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

	@Select("SELECT COUNT(*) FROM pr3 WHERE cat3_id = #{cat3id}")
	int countByCat3(int cat3id);
	
	List<ProductDTO> product_all_paged(@Param("offset") int offset,
            						   @Param("pageSize") int pageSize);
	
	@Select("SELECT COUNT(*) FROM pr3")
	int countAll();
	
	List<ProductDTO> search_paged(@Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);

	@Select("SELECT COUNT(*) FROM pr3 WHERE title LIKE #{keyword} OR content LIKE #{keyword}")
	int countSearch(@Param("keyword") String keyword);

	// 페이징용
	
	
	List<ProductDTO> product_cat2(int id);
	List<ProductDTO> product_cat3(int id); //카테고리별 상품 리스트
	
	ProductDTO detail(int id); //상세보기
	List<ProductDTO> list(); //전체 리스트
	void viewcount(int id); //조회수 상승
	void postcount(int id); //신고수 상승
	List<ProductDTO> search(String key);
}
