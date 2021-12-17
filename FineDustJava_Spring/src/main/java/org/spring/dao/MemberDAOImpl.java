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
    public List<MemberVO> selectMember(MemberVO member) throws Exception {
 
        return sqlSession.selectList(Namespace+".selectMember", member);
    }

    @Override
	public void insertMember(MemberVO member){
		sqlSession.insert(Namespace+".insertMember", member);
	}
    
    /*
    @Override
    public int selectSpotidx(String spot_name) throws Exception{
    	return sqlSession.selectOne(Namespace+".selectSpotidx", spot_name);
    }
    */
 
}
