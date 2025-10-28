package com.example.pr3.API;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pr3.dao.ProductDAO;
import com.example.pr3.dto.ProductDTO;

@RestController
@RequestMapping("/api")
public class APIcontroller {

	@Autowired
	ProductDAO dao;
	
	@GetMapping("products/{id}")
	public ProductDTO get(@PathVariable("id") int id) {
		return dao.detail(id);
	}
	
	@GetMapping("productlist")
	public List<ProductDTO> list() {
		System.out.println("들어옴");
		return dao.list();
	}
}
