package com.pineone.icbms.sda.sf.sd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;

/**
 *   SemanticDescriptor update
 */
public class UpdateSemanticDescriptor {
	
	final Configuration config_lecture = new Configuration(new File(Utils.QUERY_LECTURE_PATH));
	final Configuration config_device = new Configuration(new File(Utils.QUERY_DEVICE_PATH));

	Connection connection = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	/**
	 * Device용 Triple파일 생성
	 * @param name
	 * @throws UserDefinedException
	 * @throws IOException
	 * @throws SQLException
	 * @return void
	 */
	private void getMergedFileForDevice(String name) throws UserDefinedException, IOException, SQLException {

		StringBuffer buffer = new StringBuffer();
		String prefix = Utils.getHeaderForTripleFile();
		buffer.append(prefix);
		String filepath = null;
		String querypath = null;
		String temppath = null;
		
		
		filepath = Utils.UPDATE_DEVICE_SAVE_FILE_PATH + name + ".ttl";
		querypath = Utils.UPDATE_QUERY_DEVICE_PATH;
		temppath = Utils.UPDATE_QUERY_DEVICE_TEMP_FILE_PATH+name+".sql";
		
		File previous = null;
		FileOutputStream fos = null;
		
		String result="";
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(querypath)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(temppath)));

			while((result = br.readLine()) != null) {
				result = result.replaceAll("(@\\{args)([0])+\\}", name);
				bw.write(result + "\r\n");
				bw.flush();
			}
			bw.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		buffer.append(getOneDeviceInfoFromRDBMSTriples(name)); 
		
		// Write
		try {
			previous = new File(filepath);

			if(previous.exists()){
				previous.delete();
			}
			
			fos = new FileOutputStream(filepath);
			fos.write(buffer.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fos !=null ) {
				fos.close();
			} 
		}

	}
	
	/**
	 * Semantic Descriptor 생성 메서드
	 * @param dbName
	 * @throws UserDefinedException
	 * @throws IOException
	 * @throws SQLException
	 * @return void
	 */
	private void getMergedFile(String dbName) throws UserDefinedException, IOException, SQLException {
		StringBuffer buffer = new StringBuffer();
		String prefix = Utils.getHeaderForTripleFile();
		buffer.append(prefix);
		String filepath = null;
		
		if(dbName.equals("device")) {
			buffer.append(getDeviceInfoFromRDBMSTriples());
			filepath = Utils.DEVICE_SAVE_FILE_PATH;
		} else if(dbName.equals("lecture")) {
			buffer.append(getLectureInfoFromRDBMSTriples());
			filepath = Utils.LECTURE_SAVE_FILE_PATH;
		} else if(dbName.equals("all")) {
			buffer.append(getDeviceInfoFromRDBMSTriples() + "\n" + getLectureInfoFromRDBMSTriples());
			filepath = Utils.ALL_SAVE_FILE_PATH;
		} else {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}
		
		File previous = null;
		FileOutputStream fos = null;
		// Write
		try {
			previous = new File(filepath);

			if(previous.exists()){
				previous.delete();
			}
			
			fos = new FileOutputStream(filepath);
			
			fos.write(buffer.toString().getBytes());
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fos !=null ) {
				fos.close();
			} 
		}
	}
	
	/**
	 *   Lecture시간표를 Triple로 변환
	 * @return String
	 * @throws SQLException
	 */
	public String getLectureInfoFromRDBMSTriples() throws SQLException {
		StringBuffer buffer = new StringBuffer();
		String driver = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://" + Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.server") + ":" 
				+ Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.port") + "/device";
		String username = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.user");
		String password = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.pass");

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		String query = (String) config_lecture.get("lecture");
		try {
			connection = DriverManager.getConnection(url, username, password);
			pstmt = connection.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString(1);
				buffer.append(str + "\n");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
			pstmt.close();
			rs.close();
		}
		return buffer.toString();
	}

	
	/**
	 *   Device정보를 Triple로 변환
	 * @param name
	 * @return String
	 * @throws SQLException
	 */
	public String getOneDeviceInfoFromRDBMSTriples(String name) throws SQLException {
		StringBuffer buffer = new StringBuffer();
		String driver = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://" + Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.server") + ":" 
					+ Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.port") + "/device";
		String username = Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.user");
		String password = Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.pass");

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		Configuration config_one_device = new Configuration(new File(Utils.UPDATE_QUERY_DEVICE_TEMP_FILE_PATH+name+".sql")); 
		String query = (String) config_one_device.get("device");
			try {
			connection = DriverManager.getConnection(url, username, password);
			pstmt = connection.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String str = rs.getString(1);
				buffer.append(str + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
			pstmt.close();
			rs.close();
		}
		return buffer.toString();
	}
	
	
	/**
	 *   Device정보를 Triple로 변환
	 * @return String
	 * @throws SQLException
	 */
	public String getDeviceInfoFromRDBMSTriples() throws SQLException {
		StringBuffer buffer = new StringBuffer();
		String driver = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://" + Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.server") + ":" 
					+ Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.port") + "/device";
		String username = Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.user");
		String password = Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.pass");

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		String query = (String) config_device.get("device");
			try {
			connection = DriverManager.getConnection(url, username, password);
			pstmt = connection.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String str = rs.getString(1);
				buffer.append(str + "\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
			pstmt.close();
			rs.close();
		}
		return buffer.toString();
	}
	
	
	/**
	 *   Device정보 확인
	 * @param name
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean checkDevice(String name) throws SQLException {
		String url = "jdbc:mariadb://" + Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.server") + ":" 
					+ Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.port") + "/device";
		String username = Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.user");
		String password = Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.pass");
		
		String query = "SELECT COUNT(*) FROM Device WHERE name = ? ";
		
		try {
			connection = DriverManager.getConnection(url, username, password);
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int result = rs.getInt(1);
				if(result==1)
					return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
			pstmt.close();
			rs.close();
		}
		
		return false;
	}
	
	/**
	 * Device정보 Update
	 * @param name
	 * @throws UserDefinedException
	 * @throws IOException
	 * @throws SQLException
	 * @return void
	 */
	public void makeUpdateDevice(String name) throws UserDefinedException, IOException, SQLException {
		UpdateSemanticDescriptor sd = new UpdateSemanticDescriptor();
		sd.getMergedFileForDevice(name);
	}
	
	/**
	 * Jena update
	 * @param dbName
	 * @throws UserDefinedException
	 * @throws IOException
	 * @throws SQLException
	 * @return void
	 */
	public void makeUpdateJena(String dbName) throws UserDefinedException, IOException, SQLException {
		UpdateSemanticDescriptor sd = new UpdateSemanticDescriptor();
		sd.getMergedFile(dbName);
	}

	/**
	 * 임시파일 삭제
	 * @param name
	 * @return boolean
	 */
	public boolean deleteTempFile(String name) {

		String temp_sql = Utils.UPDATE_QUERY_DEVICE_TEMP_FILE_PATH+name+".sql";
		String temp_ttl = Utils.UPDATE_DEVICE_SAVE_FILE_PATH+name+".ttl";
		File f1 = new File(temp_sql);
		File f2 = new File(temp_ttl);
		if (f1.delete() && f2.delete()) {
			return true;
			
		} else {
			return false;
		}
	}
}