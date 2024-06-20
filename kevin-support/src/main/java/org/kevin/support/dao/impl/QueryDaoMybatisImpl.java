package org.kevin.support.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.kevin.support.dao.QueryDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * The implements of the QueryDao with mybatis.
 * @author fangang
 */
public class QueryDaoMybatisImpl implements QueryDao {

	private String sqlMapper;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	/**
	 * @return the sqlMapper
	 */
	public String getSqlMapper() {
		return sqlMapper;
	}

	/**
	 * @param sqlMapper the sqlMapper to set
	 */
	public void setSqlMapper(String sqlMapper) {
		this.sqlMapper = sqlMapper;
	}

	@Override
	public List<?> query(Map<String, Object> params) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectList(sqlMapper+".query", params);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public long count(Map<String, Object> params) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectOne(sqlMapper+".count", params);
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public Map<String, Object> aggregate(Map<String, Object> params) {
		@SuppressWarnings("unchecked")
		Map<String, String> aggregation = (Map<String, String>)params.get("aggregation");
		if(aggregation==null||aggregation.isEmpty()) return null;
		
		String buffer = "";
		for(String key : aggregation.keySet()) {
			String value = aggregation.get(key);
			if(!"".equals(buffer)) buffer+=", ";
			buffer += value+"("+key+") "+key;
		}
		params.put("aggregation", buffer);
		
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectOne(sqlMapper+".aggregate", params);
		} finally {
			sqlSession.close();
		}
	}
}
