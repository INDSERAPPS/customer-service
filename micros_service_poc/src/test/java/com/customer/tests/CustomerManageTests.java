package com.customer.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.customer.main.CustomerManage;
import com.customer.message.CustomerInsertRequestBody;
import com.customer.message.CustomerUpdateRequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=CustomerManage.class)
public class CustomerManageTests {
	
	
	
	private static ObjectMapper jacksonMapper = new ObjectMapper();
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void testCustomerInsertSuccess() throws Exception
	{
		CustomerInsertRequestBody custInsertReqBody = new CustomerInsertRequestBody();
		custInsertReqBody.setAccountType("S");
		custInsertReqBody.setEmail("helloint@tcs.com");
		custInsertReqBody.setIsEnabled("true");
		custInsertReqBody.setName("ABCD TEST Junit");
		custInsertReqBody.setPassword("uniqueoktestneeded");
		
		HttpHeaders httpHeader = createHeaders();
		
		String customerInsertReqBody = jacksonMapper.writeValueAsString(custInsertReqBody);
		mockMvc.perform(post("/v1.1/customers").headers(httpHeader).content(customerInsertReqBody)).andExpect(jsonPath("$.customerId").isNotEmpty());
               
	}
	
	@Test
	public void testCustomerUpdateSuccess() throws Exception
	{
		String pwd ="qwerttyyuio1234" ;
		CustomerUpdateRequestBody custUpdateReqBody = new CustomerUpdateRequestBody();
		custUpdateReqBody.setCustomerId("PRYMR9hm74Y2WNYA2");
		custUpdateReqBody.setPassword(pwd);
		String customerUpdateRequestBody = jacksonMapper.writeValueAsString(custUpdateReqBody);
		
		HttpHeaders httpHeader = createHeaders();
		
		mockMvc.perform(put("/v1.1/customers").headers(httpHeader).content(customerUpdateRequestBody))
		.andExpect(jsonPath("$.password").value(pwd));
	}
	
	public HttpHeaders createHeaders()
	{
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.add("Content-Type", "application/json");
		httpHeader.add("Accept-Charset", "utf-8");
		httpHeader.add("Date", "Tue, 16 May 2017 13:36:02 GMT");
		httpHeader.add("Server", "localhost");
		httpHeader.add("Tocken", "ABCDEF");
		
		return httpHeader ;
	}

}
