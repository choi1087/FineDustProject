package org.spring.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.spring.dto.MemberVO;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDAOImpl implements MemberDAO {
 
    @Inject
    private SqlSession sqlSession;
    
    private static final String Namespace = "org.spring.mapper.memberMapper";
    
    @Override
    public List<MemberVO> selectMember() throws Exception {
 
        return sqlSession.selectList(Namespace+".selectMember");
    }
 
}
