package com.example.pr3.Controller;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.pr3.dto.Category1;
import com.example.pr3.dto.ContentDTO;
import com.example.pr3.dto.MemberDTO;
import com.example.pr3.dao.MemberDAO;
import com.example.pr3.dao.ProductDAO;
import com.example.pr3.dao.ContentDAO;

@Controller
public class AdminController {
	
	@Autowired ContentDAO contentDAO;
	@Autowired MemberDAO memberDAO;
	@Autowired ProductDAO dao;
	
	@PostMapping("/memberdelete")
    public String delete(@RequestParam("user_no") int user_no) {
        memberDAO.memberdelete(user_no);
        return "redirect:/memberlist";
    }
	
	@GetMapping("/adminlist")
	public String adminList(Model model, HttpSession session) {
	    MemberDTO login = (MemberDTO) session.getAttribute("login");
	    if (login == null || !"admin".equals(login.getRank())) {
	        return "redirect:/";
	    }
	    
	    List<MemberDTO> adminlist = memberDAO.adminlist();
	    model.addAttribute("adminList", adminlist);
	    List<Category1> categoris = dao.categories1();
        model.addAttribute("cat1List", categoris);
	    return "admin/adminlist";
	}
	
	@GetMapping("/memberlist")
	public String memberList(Model model, HttpSession session) {
	    MemberDTO login = (MemberDTO) session.getAttribute("login");
	    if (login == null || !"admin".equals(login.getRank())) {
	        return "redirect:/";
	    }
	    
	    List<MemberDTO> memberlist = memberDAO.memberlist();
	    model.addAttribute("adminList", memberlist);
	    List<Category1> categoris = dao.categories1();
        model.addAttribute("cat1List", categoris);
	    return "admin/memberlist";
	}
	
	@PostMapping("/rankupdate")
    public String updateMemberRank(@RequestParam("rank") String rank,
    								@RequestParam("user_no") int user_no,
                                   RedirectAttributes redirectAttributes) {
        int result = memberDAO.rankupdate(rank, user_no);
        
        // 등급 변경 후 다시 회원 목록 페이지로 이동
        if ("admin".equals(rank)) {
            return "redirect:/adminlist";
        } else if ("user".equals(rank)) {
            return "redirect:/memberlist";
        } else {
            return "redirect:/adminpage";
        }
    }
	
	//신고 리스트
    @GetMapping("/report/list")
    public String reportlist(Model model, HttpSession session) {
    	MemberDTO login = (MemberDTO) session.getAttribute("login");
	    if (login == null || !"admin".equals(login.getRank())) {
	        return "redirect:/";
	    }
    	List<ContentDTO> reports = contentDAO.reportlist();
    	for (ContentDTO r : reports) {
    		switch (r.getReson().intValue()) {
    	        case 1:
    	            r.setReson_text("광고성 컨텐츠");
    	            break;
    	        case 2:
    	            r.setReson_text("상품 정보 부정확");
    	            break;
    	        case 3:
    	            r.setReson_text("거래 금지 품목");
    	            break;
    	        case 4:
    	            r.setReson_text("사기 의심");
    	            break;
    	        case 5:
    	        	r.setReson_text("컨텐츠 내용 불쾌");
    	    }
    		r.setRepoter_text(memberDAO.findNicknameByUserNo(r.getRepoter()));
    	}
    	model.addAttribute("item", reports);
    	List<Category1> categoris = dao.categories1();
        model.addAttribute("cat1List", categoris);
    	return "admin/reportList";
    }
	
	

}
