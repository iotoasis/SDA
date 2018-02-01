package com.pineone.icbms.sda.comm;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *   쿼리 수행을 위한 설정정보
 */
public class SqlMapConfig {
	private static SqlSessionFactory sqlSession;

	static {
		String resource = "mybatis-config.xml";

		try {
			Reader reader = Resources.getResourceAsReader(resource);
			sqlSession = new SqlSessionFactoryBuilder().build(reader);
			reader.close();
		} catch (Exception e) {
			System.out.println("SqlMapConfig error : " + e);
		}
	}

	/**
	 * Session을 리턴
	 * @return
	 */
	public static SqlSessionFactory getSqlSession() {
		return sqlSession;
	}
}