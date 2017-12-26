package com.pineone.icbms.sda.sf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import com.pineone.icbms.sda.comm.exception.UserDefinedException;
import com.pineone.icbms.sda.comm.util.Utils;

public class QueryCommon {
	private final Log log = LogFactory.getLog(this.getClass());

	// 쿼리에 있는 변수를 적절한 값으로 치환하여 리턴함
	public final String makeFinal(String qlStringl, String[] idxVals) throws Exception {
		StringBuffer parseQl = new StringBuffer();
		Date now = new Date();
		String addStr = "";
		String lastStr = "";
		String argStr = "";
		String idx = "";
		int skipCnt = 0;

		if (idxVals == null) {
			log.debug("idxVals is null");
		} else {
//			log.debug("count of idxVals : " + idxVals.length);
//			log.debug("values of idxVals : " + Arrays.toString(idxVals));
			//log.debug(String.format("Count : %s , Vals : %s , Query : %s", idxVals.length, Arrays.toString(idxVals), qlStringl));
			log.debug(String.format("Count : %s , Vals : %s ", idxVals.length, Arrays.toString(idxVals)));
		}

		if (qlStringl == null || qlStringl.equals("")) {
			throw new UserDefinedException(HttpStatus.BAD_REQUEST, "sparql is null or none !");
		}

		//log.debug("sparql to make ===========>\n" + sparql);

		while (! qlStringl.equals("")) {
			try {
				addStr = qlStringl.substring(0, qlStringl.indexOf("@{"));
			} catch (StringIndexOutOfBoundsException e) { // 더이상 "@{"이 없다면 나머지
															// 문자열은 그대로 적용
				parseQl.append(qlStringl);
				break;
			}

			lastStr = qlStringl.substring(qlStringl.indexOf("@{"));
			skipCnt += 2;
			argStr = lastStr.substring(skipCnt, lastStr.indexOf("}"));

			// log.debug("addStr[" + cnt + "]==========>" + addStr);
			// log.debug("lastStr[" + cnt + "]==========>" + lastStr);
			// log.debug("argStr[" + cnt + "]==========>" + argStr);

			parseQl.append(addStr);
			if (argStr.equals("systime")) {
				parseQl.append(Utils.systimeFormat.format(now));
			} else if (argStr.equals("sysdate")) {
				parseQl.append(Utils.sysdateFormat.format(now));
			} else if (argStr.equals("todayzero")) {
				parseQl.append(Utils.sysdateFormat2.format(now) + "T00:00:00");
			} else if (argStr.equals("todaylast")) {
				parseQl.append(Utils.sysdateFormat2.format(now) + "T23:59:59");
			} else if (argStr.equals("sysdatetime")) {
				parseQl.append(Utils.sysdatetimeFormat.format(now));
			} else if (argStr.equals("nyear")) {
				parseQl.append(Utils.nYearFormat.format(now));
			} else if (argStr.equals("nmonth")) {
				parseQl.append(Utils.nMonthFormat.format(now));
			} else if (argStr.equals("nday")) {
				parseQl.append(Utils.nDayFormat.format(now));
			} else if (argStr.equals("nhour")) {
				parseQl.append(Utils.nHourFormat.format(now));
			} else if (argStr.equals("nminute")) {
				parseQl.append(Utils.nMinuteFormat.format(now));
			} else if (argStr.equals("nsecond")) {
				parseQl.append(Utils.nSecondFormat.format(now));
			} else if (argStr.equals("sysweekday")) {
				String d = Utils.sysweekdayFormat.format(now);
				String dStr = "";
				if (d.equals("1")) { // 월요일
					dStr = "monday";
				} else if (d.equals("2")) {
					dStr = "tuesday";
				} else if (d.equals("3")) {
					dStr = "wednesday";
				} else if (d.equals("4")) {
					dStr = "thursday";
				} else if (d.equals("5")) {
					dStr = "friday";
				} else if (d.equals("6")) {
					dStr = "saturday";
				} else if (d.equals("7")) { // 일요일
					dStr = "sunday";
				}
				parseQl.append(dStr);
			} else if (argStr.startsWith("arg")) {
				idx = argStr.substring(3); // "arg"이후의 숫자값을 취함
				for (int i = 0; i < 100; i++) {
					if (Integer.parseInt(idx) == i) {
						parseQl.append(idxVals[i]);
						break;
					}
				}
			} else if (argStr.startsWith("now")) {

				String[] split = argStr.split(","); // @{now+3, second, "YYYYMM"}
				int val = Integer.parseInt(split[0].substring(3)); // "now"이후의 연산자(+, -)를 취함

				// 날짜계산
				Calendar cal = new GregorianCalendar();
				cal.setTime(now);

				if (split[1].trim().equals("year")) {
					log.debug("add year by " + String.valueOf(val));
					cal.add(Calendar.YEAR, val); // 년을 더한다.

				} else if (split[1].trim().equals("month")) {
					log.debug("add month by " + String.valueOf(val));
					cal.add(Calendar.MONTH, val); // 월을 더한다.

				} else if (split[1].trim().equals("day")) {
					log.debug("add day by " + String.valueOf(val));
					cal.add(Calendar.DAY_OF_YEAR, val); // 하루를 더한다.

				} else if (split[1].trim().equals("hour")) {
					log.debug("add hour by " + String.valueOf(val));
					cal.add(Calendar.HOUR, val); // 시간을 더한다.

				} else if (split[1].trim().equals("minute")) {
					log.debug("add minute by " + String.valueOf(val));
					cal.add(Calendar.MINUTE, val); // 분을 더한다

				} else if (split[1].trim().equals("second")) {
					log.debug("add second by " + String.valueOf(val));
					cal.add(Calendar.SECOND, val); // 초를 더한다
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat(split[2]);
				parseQl.append(dateFormat.format(cal.getTime()));

			} else {
				parseQl.append("@{" + argStr + "}");
			}
			skipCnt += argStr.length();

			// skipCnt만큼 지난 이후의 나머지 문자열을 설정
			qlStringl = lastStr.substring(skipCnt + 1); // '}'에 대한 1 증가
			skipCnt = 0;
			lastStr = "";
			argStr = "";
			// cnt++;
		} // end of while

		//log.debug("sparql made ===========>\n" + parseQl.toString());

		return parseQl.toString();
	}

	// 숫자 비교용 클래스(내림차순, asc)
	public final class CntCompare implements Comparator<IdxCnt> {
		@Override
		public int compare(IdxCnt arg0, IdxCnt arg1) {
			//desc
			//return arg0.getCnt() > arg1.getCnt() ? -1 : arg0.getCnt() < arg1.getCnt() ? 1 : 0;
			
			//asc
			return arg0.getCnt() < arg1.getCnt() ? -1 : arg0.getCnt() > arg1.getCnt() ? 1 : 0;
		}
	}

	public String removeQueryGubun(String query) {
		if(query.contains(Utils.SPLIT_STR)) {
			return query.split(Utils.SPLIT_STR)[0];
		} else return query;
	}
	
	public List<String> removeQueryGubun(List<String> queryList) {
		List<String> newQuery = new ArrayList<String>();
		
		for(int m = 0; m < queryList.size(); m++) {
			if(queryList.get(m).contains(Utils.SPLIT_STR)) {
				newQuery.add(queryList.get(m).split(Utils.SPLIT_STR)[0]);
			} else newQuery.add(queryList.get(m));
		}
		return newQuery;
	}
}
