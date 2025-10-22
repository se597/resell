package com.example.pr3.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContentDTO {
	private Long pr_no;
    private String product_name;
    private int price;
    private String title;
    private String writer;
    private Date upload_at;
    private String content;
    
	/*
	 * private String file1; private String file2; private String file3;
	 */
    
    private String file1;
    private String file2;
    private String file3;
    private MultipartFile upload1;
    private MultipartFile upload2;
    private MultipartFile upload3;
    private String oldFile1;
    private String oldFile2;
    private String oldFile3;
    
    private int viewcount;
    private int postcount;
    private int state;
    
    private Long cat1_id;
    private String cat1_name;
    private Long cat2_id;
    private String cat2_name;
    private Long cat3_id;
    private String cat3_name;
    
    //신고 관련
    private Long id;
    private Long reson;
    private String reson_text;
    private Long repoter;
    private String repoter_text;
}
