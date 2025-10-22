package com.example.pr3.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.example.pr3.dao.ContentDAO;
import com.example.pr3.dao.MemberDAO;
import com.example.pr3.dao.ProductDAO;
import com.example.pr3.dao.CategoryDAO;
import com.example.pr3.dto.Category1;
import com.example.pr3.dto.Category2;
import com.example.pr3.dto.Category3;
import com.example.pr3.dto.ContentDTO;
import com.example.pr3.dto.ProductDTO;

@Controller
@RequestMapping("/content")
public class ContentController {
	
	@Autowired ProductDAO productdao;
	@Autowired MemberDAO memberdao;
	
	private final String uploadDir = "uploads";
    
    private final ContentDAO contentDAO;
    private final CategoryDAO categoryDAO;

    public ContentController(ContentDAO contentDAO, CategoryDAO categoryDAO) {
        this.contentDAO = contentDAO;
        this.categoryDAO = categoryDAO;
    }
    

    // 리스트 페이지
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        List<ContentDTO> contentList = contentDAO.selectAll();
        model.addAttribute("contentList", contentList);
        return "content/list"; // templates/content/list.html
    }
    
    // 글작성 페이지 이동
    @GetMapping("/form")
    public String form(Model model, HttpSession session, HttpServletRequest request) {
        Object login = session.getAttribute("login");
        if (login == null) {
        	String referer = "http://localhost/content/form";      	System.out.println();
        	request.getSession().setAttribute("redirectURL", referer);
        	model.addAttribute("error","글 작성은 로그인 후 이용해주세요.");
        	return "member/login";
        }

        model.addAttribute("login", login);
        
        List<Category1> cat1List = categoryDAO.selectCat1();
        List<Category2> cat2List = categoryDAO.selectCat2();
        List<Category3> cat3List = categoryDAO.selectCat3();
        
        model.addAttribute("cat1List", cat1List);
        model.addAttribute("cat2List", cat2List);
        model.addAttribute("cat3List", cat3List);
        
        
        return "content/form"; // templates/content/form.html
    }
    
    //글 작성
    @PostMapping("/add")
    public String uploadContent(@RequestParam("upload1") MultipartFile upload1,
                                @RequestParam("upload2") MultipartFile upload2,
                                @RequestParam("upload3") MultipartFile upload3,
                                ContentDTO dto) throws IOException {

    	String uploadPath = new File("src/main/resources/static/upload/").getAbsolutePath() + "/";
        if (!upload1.isEmpty()) {
            String filename1 = upload1.getOriginalFilename();
            upload1.transferTo(new File(uploadPath + filename1));
            dto.setFile1(filename1);
        }
        if (!upload2.isEmpty()) {
            String filename2 = upload2.getOriginalFilename();
            upload2.transferTo(new File(uploadPath + filename2));
            dto.setFile2(filename2);
        }
        if (!upload3.isEmpty()) {
            String filename3 = upload3.getOriginalFilename();
            upload3.transferTo(new File(uploadPath + filename3));
            dto.setFile3(filename3);
        }
        contentDAO.insert(dto);
        return "redirect:/category";
    }
    
    //수정 폼이동
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id , Model model) {
    	ProductDTO item = productdao.detail(id);
    	List<Category1> cat1List = categoryDAO.selectCat1();
        List<Category2> cat2List = categoryDAO.selectCat2();
        List<Category3> cat3List = categoryDAO.selectCat3();
        
        model.addAttribute("cat1List", cat1List);
        model.addAttribute("cat2List", cat2List);
        model.addAttribute("cat3List", cat3List);
    	model.addAttribute("item", item);
    	return "content/edit";
    }
    
    //업데이트
    @PostMapping("/update")
    public String update(ContentDTO dto) {
    	String uploadPath = new File("src/main/resources/static/upload/").getAbsolutePath() + "/";
    	
    	try {
    		if (dto.getUpload1() != null && !dto.getUpload1().isEmpty()) {
        	    String filename1 = dto.getUpload1().getOriginalFilename();
        	    dto.getUpload1().transferTo(new File(uploadPath + filename1));
        	    dto.setFile1(filename1);
        	} else {
        	    dto.setFile1(dto.getOldFile1()); // ✅ 유지
        	}
    		
    		if (dto.getUpload2() != null && !dto.getUpload2().isEmpty()) {
        	    String filename2 = dto.getUpload2().getOriginalFilename();
        	    dto.getUpload2().transferTo(new File(uploadPath + filename2));
        	    dto.setFile2(filename2);
        	} else {
        	    dto.setFile2(dto.getOldFile2()); // ✅ 유지
        	}
    		
    		if (dto.getUpload3() != null && !dto.getUpload3().isEmpty()) {
        	    String filename3 = dto.getUpload3().getOriginalFilename();
        	    dto.getUpload3().transferTo(new File(uploadPath + filename3));
        	    dto.setFile3(filename3);
        	} else {
        	    dto.setFile3(dto.getOldFile3()); // ✅ 유지
        	}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	contentDAO.update(dto);
    	
    	return "redirect:/detail/" + dto.getPr_no();
    }
    
 // 삭제 처리
    @ResponseBody
    @PostMapping("/delete")
    public String delete(@RequestParam("cont_no") int cont_no) {
        contentDAO.delete(cont_no);
        return "success";
    }
    
    // 상태 업데이트
    @ResponseBody
    @PostMapping("/updatestate")
    public String updatestate(@RequestParam("cont_no") int no, @RequestParam("state") int state) {
    	contentDAO.updateState(no, state);
    	return "success";
    }
    
    // 신고 접수
    @ResponseBody
    @PostMapping("/report")
    public String report(ContentDTO dto) {
    	productdao.postcount(dto.getPr_no().intValue());
    	contentDAO.report(dto);
    	return "success";
    }
    
 // 카테고리1 리스트 불러오기
    @ResponseBody
    @PostMapping("/api/category")
    public List<Category1> category1() {
    	System.out.println("hello");
    	List<Category1> list = productdao.categories1();
    	return list;
    }
    
    // 카테고리2 리스트 불러오기
    @ResponseBody
    @PostMapping("/api/category2")
    public List<Category2> category2(@RequestParam("cat1") int cat1) {
    	List<Category2> list = productdao.categories2(cat1);
    	System.out.println(list);
    	return list;
    }
    
 // 카테고리3 리스트 불러오기
    @ResponseBody
    @PostMapping("/api/category3")
    public List<Category3> category3(@RequestParam("cat2") int cat2) {
    	List<Category3> list = productdao.categories3(cat2);
    	System.out.println(list);
    	return list;
    }
    
}
