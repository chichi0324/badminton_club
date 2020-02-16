package com.badminton.club;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClubApplication.class)
//使用Web的環境來做測試
@WebAppConfiguration
public class ActivityControllerTest {
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	MockMvc mvc; //創建MockMvc類的物件
	
	@Before
	public void setup(){
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testSearch() throws Exception {
		System.out.println("基分撲蝶會活動網站測試 : 活動總覽");
		
		String uri = "/activity/search/";
		MvcResult mvcResult = mvc.perform(//
				MockMvcRequestBuilders.get(uri)//
	    ).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		
		Assert.assertEquals("請求錯誤",200,status);
	}
	
	@Test
	public void testSearchBy() throws Exception {
		System.out.println("基分撲蝶會活動網站測試 : 活動總覽依條件查詢");
				
		String uri = "/activity/searchBy";
		MvcResult mvcResult = mvc.perform(//
				MockMvcRequestBuilders.get(uri)//
				.param("keyWord","羽球")//
				.param("type","")//
				.param("status","")//
				.param("page","1")//
	    ).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		
		Assert.assertEquals("請求錯誤",200,status);
	}

}
