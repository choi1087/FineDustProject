package com.Demo.myapp.service;


import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.Demo.myapp.DAO2.TestDAO2;
import com.Demo.myapp.bean.TestBean;



@Service

public class TestServiceImpl implements TestService {

	@Inject

	private TestDAO2 dao;

	

	@Override

	public List<TestBean> test() throws Exception {

		// TODO Auto-generated method stub

		return dao.test();

	}



}