package com.example.pr3.Controller;

import com.example.pr3.dao.ProductDAO;
import com.example.pr3.dto.Category1;
import com.example.pr3.dto.Category2;
import com.example.pr3.dto.Category3;
import com.example.pr3.dto.ProductDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductDAO productDAO;

    // 홈
    @GetMapping("/")
    public String home(Model model) {
        List<Category1> categoris = productDAO.categories1();
        model.addAttribute("cat1List", categoris);
        return "home";
    }

    // 전체 카테고리 페이지
    @GetMapping("/category")
    public String list(Model model) {
        List<Category1> cat1List = productDAO.categories1();
        model.addAttribute("cat1List", cat1List);
        return "content/category";
    }

    // 기본 대분류 진입
    @GetMapping("/category1/{cat1id}")
    public String category(@PathVariable("cat1id") int cat1, Model model) {
        List<Category1> cat1List = productDAO.categories1();
        List<Category2> cat2 = productDAO.categories2(cat1);
        model.addAttribute("cat1List", cat1List);
        model.addAttribute("cat2", cat2);
        model.addAttribute("currentCat1", cat1);
        return "content/category";
    }

    // 🔹 대분류 게시물 (페이징 포함)
    @GetMapping("/api/posts/category1/{cat1id}")
    public String loadPostsByCat1(@PathVariable("cat1id") int cat1id,
    		 					  @RequestParam(defaultValue = "1", name = "page") int page,
                                  Model model) {
        int pageSize = 15;
        int offset = (page - 1) * pageSize;

        List<ProductDTO> list = productDAO.product_cat1_paged(cat1id, offset, pageSize);
        int totalCount = productDAO.countByCat1(cat1id);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("list", list);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("contextType", "cat1");
        model.addAttribute("contextId", cat1id);

        return "fragments/postList :: postListFragment";
    }

    // 🔹 중분류 게시물 (페이징 포함)
    @GetMapping("/api/posts/category2/{cat2id}")
    public String loadPostsByCat2(@PathVariable("cat2id") int cat2id,
    							  @RequestParam(defaultValue = "1", name = "page") int page,
                                  Model model) {
        int pageSize = 15;
        int offset = (page - 1) * pageSize;

        List<ProductDTO> list = productDAO.product_cat2_paged(cat2id, offset, pageSize);
        int totalCount = productDAO.countByCat2(cat2id);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("list", list);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("contextType", "cat2");
        model.addAttribute("contextId", cat2id);

        return "fragments/postList :: postListFragment";
    }

    // 🔹 소분류 게시물 (페이징 포함)
    @GetMapping("/api/posts/category3/{cat3id}")
    public String loadPostsByCat3(@PathVariable("cat3id") int cat3id,
    		 					  @RequestParam(defaultValue = "1", name = "page") int page,
                                  Model model) {
        int pageSize = 15;
        int offset = (page - 1) * pageSize;

        List<ProductDTO> list = productDAO.product_cat3_paged(cat3id, offset, pageSize);
        int totalCount = productDAO.countByCat3(cat3id);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("list", list);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("contextType", "cat3");
        model.addAttribute("contextId", cat3id);

        return "fragments/postList :: postListFragment";
    }

    // 🔹 대분류 변경 시 중분류 + 초기 게시물 로드
    @GetMapping("/api/category1/{cat1id}")
    public String loadCategory1(@PathVariable("cat1id") int cat1, Model model) {
        List<ProductDTO> list = productDAO.product_cat1_paged(cat1, 0, 15);
        List<Category2> cat2 = productDAO.categories2(cat1);
        model.addAttribute("list", list);
        model.addAttribute("cat2", cat2);
        model.addAttribute("contextType", "cat1");
        model.addAttribute("contextId", cat1);
        return "fragments/category1-change :: category1Change";
    }

    // 🔹 소분류 목록
    @GetMapping("/api/category3/{cat2id}")
    public String loadCategory3(@PathVariable("cat2id") int cat2id, Model model) {
        List<Category3> cat3List = productDAO.categories3(cat2id);
        model.addAttribute("cat3List", cat3List);
        return "fragments/category3 :: category3Fragment";
    }

    // 🔹 게시물 검색
    @GetMapping("/api/search/{keyword}")
    public String search(@PathVariable("keyword") String key,
                         @RequestParam(defaultValue = "1", name = "page") int page,
                         Model model) {
        String keyword = "%" + key + "%";
        int pageSize = 15;
        int offset = (page - 1) * pageSize;

        List<ProductDTO> list = productDAO.search_paged(keyword, offset, pageSize);
        int totalCount = productDAO.countSearch(keyword);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("list", list);
        model.addAttribute("contextType", "search");
        model.addAttribute("contextId", key);  // 원본 키워드 저장
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "fragments/postList :: postListFragment";
    }


    // 🔹 전체 게시물
    @GetMapping("/api/post/all")
    public String loadPostsall(@RequestParam(defaultValue = "1", name = "page") int page,
                               Model model) {
        int pageSize = 15;
        int offset = (page - 1) * pageSize;

        List<ProductDTO> list = productDAO.product_all_paged(offset, pageSize);
        int totalCount = productDAO.countAll();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("list", list);
        model.addAttribute("contextType", "all");
        model.addAttribute("contextId", 0);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "fragments/postList :: postListFragment";
    }


    // 🔹 상세 페이지
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model) {
        List<Category1> categoris = productDAO.categories1();
        model.addAttribute("cat1List", categoris);

        List<String> files = new ArrayList<>();
        productDAO.viewcount(id);
        ProductDTO item = productDAO.detail(id);

        if (item.getFile1() != null && !item.getFile1().isEmpty()) files.add(item.getFile1());
        if (item.getFile2() != null && !item.getFile2().isEmpty()) files.add(item.getFile2());
        if (item.getFile3() != null && !item.getFile3().isEmpty()) files.add(item.getFile3());
        if (files.isEmpty()) files.add("empty.png");

        model.addAttribute("item", item);
        model.addAttribute("fileList", files);

        return "content/post";
    }
}
