package org.spring.dao;

import java.util.List;

import org.spring.dto.MemberVO;

public interface MemberDAO {
	public List<MemberVO> selectMember() throws Exception;
}
