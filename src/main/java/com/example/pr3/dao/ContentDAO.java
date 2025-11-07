package com.example.pr3.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import com.example.pr3.dto.ContentDTO;

import java.util.List;

@Mapper
public interface ContentDAO {
	//전체 글 리스트
	@Select("""
		    SELECT p.pr_no, p.product_name, p.price, p.title, p.writer, p.upload_at,
		        p.content, p.file1, p.file2, p.file3, p.viewcount, p.postcount, p.state,
		        p.cat1_id, p.cat2_id, p.cat3_id,
		        c1.cat1_name, c2.cat2_name, c3.cat3_name
		    FROM pr3 p
		    LEFT JOIN category1 c1 ON p.cat1_id = c1.cat1_id
		    LEFT JOIN category2 c2 ON p.cat2_id = c2.cat2_id
		    LEFT JOIN category3 c3 ON p.cat3_id = c3.cat3_id
		    ORDER BY p.pr_no DESC
		""")
        List<ContentDTO> selectAll();

        // 등록
        @Insert("""
            INSERT INTO pr3(
                product_name, price, title, writer, content, 
                file1, file2, file3, 
                cat1_id, cat2_id, cat3_id
            )
            VALUES (
                #{product_name}, #{price}, #{title}, #{writer}, #{content}, 
                #{file1,jdbcType=VARCHAR}, #{file2,jdbcType=VARCHAR}, #{file3,jdbcType=VARCHAR}, 
                #{cat1_id}, #{cat2_id}, #{cat3_id}
            )
        """)
        void insert(ContentDTO dto);
    // 삭제
    @Delete("DELETE FROM pr3 WHERE pr_no = #{cont_no}")
    void delete(int cont_no);
    // 판매 상태변경
    @Update("UPDATE pr3 SET state = #{state} WHERE pr_no = #{no}")
    void updateState(@Param("no") int no, @Param("state") int state);
    // 판매글 수정
    @Update("""
    	    UPDATE pr3
    	    SET product_name = #{product_name},
    	        price = #{price},
    	        title = #{title},
    	        content = #{content},
    	        cat1_id = #{cat1_id},
    	        cat2_id = #{cat2_id},
    	        cat3_id = #{cat3_id},
    	        file1 = #{file1, jdbcType=VARCHAR},
    	        file2 = #{file2, jdbcType=VARCHAR},
    	        file3 = #{file3, jdbcType=VARCHAR}
    	    WHERE pr_no = #{pr_no}
    	""")
    	void update(ContentDTO dto);
    //특정 회원 글 리스트
 	@Select("select * from pr3 where writer = #{nickname} order by pr_no desc")
 	List<ContentDTO> mycontent(@Param("nickname")String nickname);
 	
 	@Insert("INSERT INTO pr3report (pr_no, reson, repoter) VALUES (#{pr_no}, #{reson}, #{repoter})")
 	void report(ContentDTO dto);
 	
 	@Select("""
 		    SELECT r.id, r.pr_no, c.title, r.reson, r.repoter, c.writer
 		    FROM pr3report r
 		    JOIN pr3 c ON r.pr_no = c.pr_no
 		""")
 		List<ContentDTO> reportlist();


}
