package com.example.pr3.dao;

import com.example.pr3.dto.MemberDTO;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;



@Mapper
public interface MemberDAO {
	
	// 전체 회원 조회
	@Select("SELECT * FROM pr3member where rank = 'user' order by user_no")
	    List<MemberDTO> memberlist();
		
	// 관리자 계정 조회
	@Select("SELECT * FROM pr3member WHERE rank = 'admin'")
		List<MemberDTO> adminlist();
		
	// 계삭
	@Delete("DELETE FROM pr3member WHERE user_no = #{user_no}")
	    void memberdelete(int user_no);

    // 로그인: id와 pw가 일치하는 회원 조회
    @Select("SELECT * FROM pr3member WHERE id = #{id} AND pw = #{pw}")
    MemberDTO login(MemberDTO member);
    
    // 중복 아이디 체크
    @Select("SELECT COUNT(*) FROM pr3member WHERE id = #{id}")
    int dupl_check(@Param("id") String id);
    
    
    // 회원가입
    @Insert("""
            INSERT INTO pr3member 
            (id, pw, name, address, birth, phone_number, email, nickname) 
            VALUES 
            (#{id}, #{pw}, #{name}, #{address}, #{birth}, #{phone_number}, 
            #{email}, #{nickname})
        """)
    void join(MemberDTO dto);
    
    // 해당 회원 정보조회
 	@Select("SELECT * FROM pr3member where user_no = #{user_no}")
    MemberDTO memberinfo(@Param("user_no") int user_no);
 	
 	// 회원 정보 수정
 	@Update("""
 		    UPDATE pr3member
 		    SET pw = #{pw},
 		        name = #{name},
 		        address = #{address},
 		        birth = #{birth},
 		        phone_number = #{phone_number},
 		        email = #{email},
 		        nickname = #{nickname}
 		    WHERE user_no = #{user_no}
 		""")
 		int updateMember(MemberDTO dto);
 	
 	// 회원 등급 변경
  	@Update("update pr3member set rank = #{rank} where user_no = #{user_no}")
  	int rankupdate(@Param("rank")String rank, @Param("user_no")int user_no);
 	
 	// user_no로 닉네임 뽑기
 	@Select("""
 	        SELECT nickname
 	        FROM pr3member
 	        WHERE user_no = #{user_no}
 	    """)
 	    String findNicknameByUserNo(Long user_no);
}
