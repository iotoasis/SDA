package com.pineone.icbms.sda.hbase.onem2m;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import com.pineone.icbms.sda.comm.util.Utils;

// thread로 돌리는 경우
public class HBasePutTest implements Serializable  {
	private static final long serialVersionUID = 1554224755390165654L;
	
	static final String TABLE_NAME = "sidata";
	static final String COLFAM_NAME = "cf";
	
	static final String task_group_id = "TG3000";
	static final String task_id = "AG-1-1-100";
	static final String start_time = "20161122T163339";	
	static final String col_from = "COL-SI-DATA";

	public static void main(String[] args) {
		HBasePutTest hBaseTest = new HBasePutTest();
		try {
			hBaseTest.saveToHBase();
		} catch (Exception ex) {
			System.out.println("exception :"+ex.getStackTrace());
		}
	}

	public void saveToHBase() throws Exception {
				Connection conn = null;
				List<String> data = new ArrayList<String>();

				 // test data
				 data.add("{\"_id\":{\"_time\":1479800019,\"_machine\":-1422354674,\"_inc\":-8916726,\"_new\":false},\"rn\":\"CONTENT_INST_15892692\",\"ty\":4,\"ri\":\"CONTENT_INST_15892692\",\"pi\":\"CONTAINER_5153\",\"lbl\":[\"icbms_ReportData\"],\"cr\":\"icbms\",\"cnf\":\"text/plain:0\",\"cs\":1,\"con\":\"1\",\"_uri\":\"/herit-in/herit-cse/ONSB_BleScanner01_001/FAC_MOU_003/status/CONTENT_INST_15892692\",\"ct\":\"20161122T163339\",\"lt\":\"20161122T163339\"}");
				 System.out.println("data.size() : "+data.size());

				try {
					 	// HBase접속
						 conn = getConnection(Utils.HBASE_ZOOKEEPER_HOST, Utils.HBASE_ZOOKEEPER_PORT);

						 for(int i = 0; i < data.size(); i++) {
							putData(conn, task_group_id, task_id, start_time, col_from, data.get(i).toString());
						 }
				} catch (Exception e) {
					System.out.println("exception :"+e.toString());
				} finally {
					if(conn != null) conn.close();
				}
	}
	
	//  데이타 저장
	private final void putData(Connection conn, String task_group_id, String task_id, String start_time, String col_from, String data) throws Exception {
		Table table = null;
		JSONObject obj = JSONObject.fromObject(data);
		
		System.out.println("data : "+data);
		System.out.println("obj : "+obj.toString());
		
		//create time
		String ctTmp = obj.getString("ct");
		
		// ae
		String ae = obj.getString("cr");
		
		long ct = Utils.dateFormat.parse(ctTmp).getTime();
		
		//test
		ct =new Date().getTime();
		start_time = Utils.dateFormat.format(new Date());

		try {
			
			System.out.println("put key : "+ makeRowKey(col_from, ae, ct));
			
			table = conn.getTable(TableName.valueOf(TABLE_NAME));
			Put put = new Put(Bytes.toBytes(makeRowKey(col_from, ae, ct)));
			byte[] family = Bytes.toBytes(COLFAM_NAME);
			
			put.addColumn(family, Bytes.toBytes("task_group_id"), Bytes.toBytes(task_group_id));
			put.addColumn(family, Bytes.toBytes("task_id"), Bytes.toBytes(String.valueOf(task_id)));
			put.addColumn(family, Bytes.toBytes("start_time"), Bytes.toBytes(String.valueOf(start_time)));
			put.addColumn(family, Bytes.toBytes("col_from"), Bytes.toBytes(col_from));
			
			put.addColumn(family, Bytes.toBytes("contents"), Bytes.toBytes(obj.toString()));
			
			System.out.println("put data : "+put.toString());
			
			table.put(put);
		} catch(Exception e) {
			System.out.println(" exception in putData()");
		} finally {
			if(table != null) table.close();
		}
	}		
	
	// key 생성(Seed+수집구분+AE+CT)
	private final String makeRowKey(String col_from, String ae, long ct) throws Exception {
		String seed = StringUtils.reverse(String.format("%010x", col_from.hashCode()));
		long desc_ct = Long.MAX_VALUE - ct;
		return String.format("%s-%s-%s-%019d", seed, StringUtils.leftPad(col_from, 10,'X'), StringUtils.leftPad(col_from, 20,'X'), desc_ct);
	}
	
	/*
	 * hbase connection연결(api호출시 클라이언트에서 hbaseHost및 hbasePort를 지정함)
	 */
	private final Connection getConnection(String hbaseHost, String hbasePort) throws Exception {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", hbaseHost);
		conf.set("hbase.zookeeper.property.clientPort", hbasePort);

		// 접속실패시 재시도 회수 지정		
        conf.set("hbase.client.retries.number", "3");
        conf.set("zookeeper.recovery.retry", "3");
        
        Connection conn = ConnectionFactory.createConnection(conf);
		return conn;
	}

}
