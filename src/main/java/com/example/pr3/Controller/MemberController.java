package com.example.pr3.Controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;

import com.example.pr3.dao.ContentDAO;
import com.example.pr3.dao.MemberDAO;
import com.example.pr3.dao.ProductDAO;
import com.example.pr3.dto.Category1;
import com.example.pr3.dto.ContentDTO;
import com.example.pr3.dto.MemberDTO;

@Controller
public class MemberController {

	@Autowired
	ProductDAO dao;
	
	@Autowired
	ContentDAO contentDAO;
	
    private final MemberDAO memberDAO;

    public MemberController(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    // 로그인 폼
    @GetMapping("/loginpage")
    public String loginForm(HttpServletRequest request, Model model) {
    	String referer = request.getHeader("Referer");
    	if (referer != null && !referer.contains("/login")) { 
            request.getSession().setAttribute("redirectURL", referer);
        }
    	List<Category1> categoris = dao.categories1();
        model.addAttribute("cat1List", categoris);
        return "member/login"; // templates/member/login.html
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(MemberDTO member, HttpSession session, Model model) {
        MemberDTO login = memberDAO.login(member);

        if (login != null) {
            session.setAttribute("login", login);
            String redirectURL = (String) session.getAttribute("redirectURL");
            if (redirectURL != "/") {
                session.removeAttribute("redirectURL");
                return "redirect:" + redirectURL;
            } else {
                return "redirect:/"; // 기본은 메인으로
            }
        } else {
        	List<Category1> categoris = dao.categories1();
            model.addAttribute("cat1List", categoris);
            model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "member/login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    // 회원가입 페이지
    @GetMapping("/joinpage")
    public String joinForm(Model model) {
    	List<Category1> categoris = dao.categories1();
        model.addAttribute("cat1List", categoris);
        return "member/join"; // templates/member/login.html
    }
    
    //회원가입
    @PostMapping("/join")
	public String join(MemberDTO dto) {
		memberDAO.join(dto);
		return "redirect:/";
	}
    
    // 아이디 중복 체크
    @GetMapping("/dupl_check")
    @ResponseBody
    public boolean duplCheck(@RequestParam("id") String id) {
        int count = memberDAO.dupl_check(id);
        return count == 0;
    }
    
 // 마이페이지 열기
    @GetMapping("/mypage/{user_no}")
    public ModelAndView mypage(@PathVariable(name = "user_no") int user_no,
    		ModelAndView mav, HttpSession session) {
    	if (session == null || session.getAttribute("login") == null) {
    		mav.setViewName("/");
    		return mav;
    	}
    	mav.setViewName("member/mypage");
    	mav.addObject("member", memberDAO.memberinfo(user_no));
    	mav.addObject("cat1List", dao.categories1());
        return mav;
    }
    
    // 회원정보 수정 열기
    @GetMapping("/myinfo/{user_no}")
    public ModelAndView myinfo(@PathVariable(name = "user_no") int user_no, 
    		HttpSession session, ModelAndView mav) {
    	mav.setViewName("member/myinfo");
    	mav.addObject("member", memberDAO.memberinfo(user_no));
    	mav.addObject("cat1List", dao.categories1());
        return mav;
    }
    
    // 회원정보 수정
    @PostMapping("/memberupdate")
    public String updateMember(@ModelAttribute MemberDTO member, HttpSession session) {
        memberDAO.updateMember(member);
        session.setAttribute("login", member);
        return "redirect:/mypage/" + member.getUser_no();
    }
    
    // 내 글 리스트
    @GetMapping("/mycontent")
    public String myContent(HttpSession session, Model model) {
        MemberDTO login = (MemberDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/loginpage";
        }

        String nickname = login.getNickname();
        List<ContentDTO> contentList = contentDAO.mycontent(nickname);
        List<Category1> categoris = dao.categories1();
        model.addAttribute("cat1List", categoris);
        model.addAttribute("contentList", contentList);

        return "member/mycontent";
    }
}
