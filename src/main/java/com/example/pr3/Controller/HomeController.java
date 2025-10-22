package com.example.pr3.Controller;

import com.example.pr3.dao.ContentDAO;
import com.example.pr3.dto.ContentDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ContentDAO contentDAO;

    // 생성자 주입
    public HomeController(ContentDAO contentDAO) {
        this.contentDAO = contentDAO;
    }

    // 첫 화면: /
    @GetMapping("/home")
    public String home(Model model) {
        // DB에서 모든 콘텐츠 가져오기
        List<ContentDTO> contentList = contentDAO.selectAll();

        // 모델에 담기
        model.addAttribute("contentList", contentList);

        // templates/content/list.html 반환
        return "content/list";
    }
}