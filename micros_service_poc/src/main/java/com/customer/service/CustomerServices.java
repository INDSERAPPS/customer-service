package com.customer.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.customer.Entity.AppMetaData;
import com.customer.Entity.BaseEntity;
import com.customer.Entity.Customer;
import com.customer.constants.CustomerConstants;
import com.customer.exceptions.ApplicationException;
import com.customer.message.CustomerGetRequest;
import com.customer.message.CustomerInsertRequest;
import com.customer.message.CustomerUpdateRequest;


@Service
@Transactional
public class CustomerServices {
	
	private EntityManager entityManager;
	static Random rnd = new Random();
	private static Logger log = Logger.getLogger(CustomerServices.class);

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private void injectPersistenceContext() {
		BaseEntity.setEntityManager(entityManager);
	}
	
	
	
	public String insertCustomer(CustomerInsertRequest customerInsetReq) throws ApplicationException
	{
		try{
		injectPersistenceContext() ;
		Customer customer = new Customer() ;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		customer.setCustomerId(randomString(10)) ;
		customer.setName(customerInsetReq.getCustomerInsReqBody().getName());
		customer.setEmail(customerInsetReq.getCustomerInsReqBody().getEmail()) ;
		customer.setPassword(customerInsetReq.getCustomerInsReqBody().getPassword());
		customer.setAccountType(customerInsetReq.getCustomerInsReqBody().getAccountType());
		boolean isEnabled = Boolean.valueOf(customerInsetReq.getCustomerInsReqBody().getIsEnabled()) ;
		customer.setEnabled(isEnabled);
		customer.setLastUpdated(timestamp) ;
		customer.persist() ;
		log.info("Inserted Customer details to database") ;
		return customer.getCustomerId();
		}catch(Exception e){
			throw new ApplicationException(CustomerConstants.SERVER_ERROR,"Internal Server Error") ;
			
		}
	}
	
	String randomString(int len) {
		String testStr ="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" ;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
               sb.append(testStr.charAt(rnd.nextInt(testStr.length())));
        return sb.toString();
 }

	public String updateCustomer(CustomerUpdateRequest custUpdateReq) throws ApplicationException {
		
		try{
		injectPersistenceContext() ;
		Customer customer = new Customer();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		 customer = Customer.findById(custUpdateReq.getCustomerUpdateReqBody().getCustomerId());
		 if(custUpdateReq.getCustomerUpdateReqBody().getName()!=null)
		 {
		customer.setName(custUpdateReq.getCustomerUpdateReqBody().getName());
		 }
		 if(custUpdateReq.getCustomerUpdateReqBody().getAccountType()!=null)
		 {
		customer.setAccountType(custUpdateReq.getCustomerUpdateReqBody().getAccountType());
		 }
		 if(custUpdateReq.getCustomerUpdateReqBody().getEmail()!=null)
		 {
		customer.setEmail(custUpdateReq.getCustomerUpdateReqBody().getEmail());
		 }
		 if(custUpdateReq.getCustomerUpdateReqBody().getPassword()!=null)
		 {
		customer.setPassword(custUpdateReq.getCustomerUpdateReqBody().getPassword());
		 }
		 if(custUpdateReq.getCustomerUpdateReqBody().getIsEnabled()!=null)
		 {
		boolean isEnabled = Boolean.valueOf(custUpdateReq.getCustomerUpdateReqBody().getIsEnabled()) ;
		customer.setEnabled(isEnabled);
		 }
		customer.setLastUpdated(timestamp);
		log.info("Updated Customer details to database") ;
		return "true";
		}catch(Exception e)
		{
			throw new ApplicationException(CustomerConstants.SERVER_ERROR,"Internal Server Error") ;
		}
	}
	
public Customer  getCustomer(CustomerGetRequest custGetReq) throws ApplicationException {
		
	try{
		injectPersistenceContext() ;
		Customer customer = new Customer();
		if(custGetReq.getCustomerGetReqBody().getCustomerid()!=null)
		{
		 customer = Customer.findById(custGetReq.getCustomerGetReqBody().getCustomerid());
		
		return customer;
		}
		else
		{
			customer = Customer.findByEmail(custGetReq.getCustomerGetReqBody().getEmail());
		return customer ;
		}
	}catch(Exception e){
		throw new ApplicationException(CustomerConstants.SERVER_ERROR,"Internal Server Error") ;
	}
	}

public String checkCustomerExist(String email) throws ApplicationException
{
	injectPersistenceContext() ;
	Customer customer = Customer.findByEmail(email);
	if(customer==null)
		return "true";
	else
		return "false" ;
}

public Customer fetchCustomer(String custId) throws ApplicationException
{
	try{
	injectPersistenceContext() ;
	Customer customer = Customer.findById(custId);
	return customer ;
	}catch(Exception e)
	{
		throw new ApplicationException(CustomerConstants.SERVER_ERROR,"Internal Server Error") ;
	}
	
}

public String fetchMetaData()
{
	injectPersistenceContext() ;
	return AppMetaData.findSchemaVersion();
}





}
