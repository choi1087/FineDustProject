package org.spring.service;

import java.util.List;

import org.spring.dto.MemberVO;

public interface MemberService {

	public List<MemberVO> selectMember()throws Exception;
}
