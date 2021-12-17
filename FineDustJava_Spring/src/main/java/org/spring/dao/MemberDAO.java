package org.spring.dao;

import java.util.List;

import org.spring.dto.MemberVO;

public interface MemberDAO {
	public List<MemberVO> selectMember(MemberVO member) throws Exception;
	public void insertMember(MemberVO member) throws Exception;
	
	//public int selectSpotidx(String spot_name) throws Exception;
}
