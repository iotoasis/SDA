package com.pineone.icbms.sda.sf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pineone.icbms.sda.comm.util.Utils;

import scala.NotImplementedError;

/**
 *   SDA DB에 쿼리
 */
public class MariaDbOfSdaQueryImpl extends QueryCommon implements QueryItf {

	private final Log log = LogFactory.getLog(this.getClass());
	
	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.lang.String, java.lang.String[])
	 */
	@Override
	public List<Map<String, String>> runQuery(String query, String[] idxVals) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		log.info("runQuery of mariadb of SDA start ======================>");

		log.debug("try (first) .................................. ");
		try {
			list = getResult(query, idxVals);
		} catch (Exception e) {
			int waitTime = 15*1000;
			log.debug("Exception message in runQuery() =====> "+e.getMessage());  
			
			try {
				// 일정시간 대기 했다가 다시 수행함
				log.debug("sleeping (first)................................. in "+waitTime);
				Thread.sleep(waitTime);
				
				log.debug("try (second).................................. ");
				list = getResult(query, idxVals);
			} catch (Exception ee) {
				log.debug("Exception(1)====>"+ee.getMessage());
				waitTime = 30*1000;
				if(ee.getMessage().contains("Service Unavailable")|| ee.getMessage().contains("java.net.ConnectException")
						) {					
					try {
						// restart fuseki
						Utils.restartFuseki();
					
						// 일정시간을 대기 한다.
						log.debug("sleeping (final)................................. in "+waitTime);
						Thread.sleep(waitTime);
						
						// 마지막으로 다시한번 처리해줌
						log.debug("try (final).................................. ");
						list = getResult(query, idxVals);
					} catch (Exception eee) {
						log.debug("Exception(2)====>"+eee.getMessage());
						throw eee;
					}
				}
				throw ee;
			}
		}

		log.info("runQuery of mariadb of SDA end ======================>");
		return list;
	}
	
	/**
	 *   결과 가져오기
	 * @param query
	 * @param idxVals
	 * @return List<Map<String, String>>
	 * @throws Exception
	 */
	private final List<Map<String, String>> getResult (String query, String[] idxVals) throws Exception {
		String db_server = Utils.getSdaProperty("com.pineone.icbms.sda.stat.db.sda.server");
		String db_port = Utils.getSdaProperty("com.pineone.icbms.sda.stat.db.sda.port");
		String db_name = Utils.getSdaProperty("com.pineone.icbms.sda.stat.db.sda.name");
		String db_user = Utils.getSdaProperty("com.pineone.icbms.sda.stat.db.sda.user");
		String db_pass = Utils.getSdaProperty("com.pineone.icbms.sda.stat.db.sda.pass");

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		query = makeFinal(query, idxVals);
	
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mariadb://" + db_server + ":" + db_port + "/" + db_name, db_user,  db_pass);
			
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			while (rs.next()){
			   HashMap<String,String> row = new HashMap<String, String>(columns);
			   for(int i=1; i<=columns; i++){           
				   row.put(md.getColumnName(i), rs.getObject(i).toString());
			   }
			    list.add(row);
			}
			
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle) {
				}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.lang.String)
	 */
	@Override
	public List<Map<String, String>> runQuery(String query) throws Exception {
		throw new NotImplementedError();
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.util.List)
	 */
	@Override
	public List<Map<String, String>> runQuery(List<String> queryList) throws Exception {
		throw new NotImplementedError();
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.sf.QueryItf#runQuery(java.util.List, java.lang.String[])
	 */
	@Override
	public List<Map<String, String>> runQuery(List<String> queryList, String[] idxVals) throws Exception {
		throw new NotImplementedError();
	}
}
