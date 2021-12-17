package com.Demo.myapp.DAO2;


import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.Demo.myapp.bean.TestBean;



@Repository

public class TestDAOImpl2 implements TestDAO2 {
	private static final String namespace="com.Demo.myapp.testMapper";

	@Inject
	private SqlSession sqlSession;
	
	@Override
	public List<TestBean> test() throws Exception{

		// TODO Auto-generated method stub
		return sqlSession.selectList(namespace+".test");

	}




}
