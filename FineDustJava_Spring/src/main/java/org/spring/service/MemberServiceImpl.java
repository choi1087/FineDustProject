package org.spring.service;

import java.util.List;

import javax.inject.Inject;

import org.spring.dao.MemberDAO;
import org.spring.dto.MemberVO;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{
	@Inject
	private MemberDAO dao;
	
	public void setMemberDAO(MemberDAO dao)
	{
		this.dao=dao;
	}
	@Override
	public List<MemberVO> selectMember(MemberVO member) throws Exception{
		
		return dao.selectMember(member);
	}

	@Override
	public void insertMember(MemberVO member) throws Exception{
		// TODO Auto-generated method stub
		
		
		//MemberVO member = new MemberVO(no, spotName,dustDegree, time, is_indoor);
		
		dao.insertMember(member);
	}
	
	/*
	@Override
	public int selectSpotidx(String spot_name) throws Exception{
		
		return dao.selectSpotidx(spot_name);
	}
	*/
}
