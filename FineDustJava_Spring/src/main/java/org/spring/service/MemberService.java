package org.spring.service;

import java.util.List;

import org.spring.dto.MemberVO;

public interface MemberService {

	public List<MemberVO> selectMember(MemberVO member)throws Exception;
	
	public void insertMember(MemberVO member) throws Exception;

	
	
	//public int selectSpotidx(String spot_name) throws Exception;
		
}

	
