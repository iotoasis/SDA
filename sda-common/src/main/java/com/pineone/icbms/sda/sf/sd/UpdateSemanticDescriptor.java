package com.pineone.icbms.sda.sf.sd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;


public class UpdateSemanticDescriptor {
	
	final Configuration config_lecture = new Configuration(new File(Utils.QUERY_LECTURE_PATH));
	final Configuration config_device = new Configuration(new File(Utils.QUERY_DEVICE_PATH));
	

	private void getMergedFile(String dbName) throws UserDefinedException, IOException {

		StringBuffer buffer = new StringBuffer();
		String prefix = Utils.getHeaderForTripleFile();
		buffer.append(prefix);
		
		if(dbName.equals("device")) {
			buffer.append(getDeviceInfoFromRDBMSTriples());
		} else if(dbName.equals("lecture")) {
			buffer.append(getLectureInfoFromRDBMSTriples());
		} else if(dbName.equals("all")) {
			buffer.append(getDeviceInfoFromRDBMSTriples() + "\n" + getLectureInfoFromRDBMSTriples());
		} else {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST);
		}
		
		File previous = null;
		FileOutputStream fos = null;
		// Write
		try {
			previous = new File(Utils.SD_SAVE_FILE_PATH);

			if(previous.exists()){
				previous.delete();
			}
			
			fos = new FileOutputStream(Utils.SD_SAVE_FILE_PATH);
			
			fos.write(buffer.toString().getBytes());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//null 일시 close 아니면 pass
			if(fos !=null ) {
				fos.close();
			} 
		}

	}
	
	public String getLectureInfoFromRDBMSTriples() {
		StringBuffer buffer = new StringBuffer();
		String driver = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://" + Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.server") + "/device";
		String username = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.user");
		String password = Utils.getSdaProperty("com.pineone.icbms.sda.ss.db.pass");

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String query = (String) config_lecture.get("lecture");
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String str = rs.getString(1);
				buffer.append(str + "\n");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public String getDeviceInfoFromRDBMSTriples() {
		StringBuffer buffer = new StringBuffer();
		String driver = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://" + Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.server") + ":" 
					+ Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.port") + "/device";
		String username = Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.user");
		String password = Utils.getSdaProperty("com.pineone.icbms.sda.m2tech.db.pass");

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String query = (String) config_device.get("device");
			try {
			Connection connection = DriverManager.getConnection(url, username, password);
			PreparedStatement pstmt = connection.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String str = rs.getString(1);
				buffer.append(str + "\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
	}

	
	public void makeUpdateJena(String dbName ) throws UserDefinedException, IOException {
		UpdateSemanticDescriptor sd = new UpdateSemanticDescriptor();
		sd.getMergedFile(dbName);
	}
}
