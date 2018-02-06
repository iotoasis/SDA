package com.pineone.icbms.sda.comm.util;

import java.rmi.server.UID;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.AnonId;

/**
 * 문자열관련 유틸 클래스
 */
public class StrUtils {
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	static private boolean okURIChars[] = new boolean[128];

	static {
		for (int i = 32; i < 127; i++)
			okURIChars[i] = true;
		okURIChars['<'] = false;
		okURIChars['>'] = false;
		okURIChars['\\'] = false;
	}

	/**
	 * UUID생성
	 * @param str
	 * @return String
	 */
	public static String getUUID(String str){
		return UUID.fromString(str).toString();
	}
	
	
	/**
	 * 숫자를 형식에 맞추기
	 * @param num
	 * @return String
	 */
	public static String numberFormat(int num) {
		NumberFormat nf = NumberFormat.getInstance();
		return nf.format(num);
	}
	
	/**
	 * 숫자를 형식에 맞추기
	 * @param num
	 * @return String
	 */
	public static String numberFormat(long num) {
		NumberFormat nf = NumberFormat.getInstance();
		return nf.format(num);
	}
	
	/**
	 * 무명의 이름
	 * @param id
	 * @return String
	 */
	protected static String anonName(AnonId id) {
		String name = "_:A";
		final String sid = id.toString();
		for (int i = 0; i < sid.length(); i++) {
			final char c = sid.charAt(i);
			if (c == 'X') {
				name = name + "XX";
			} else if (Character.isLetterOrDigit(c)) {
				name = name + c;
			} else {
				name = name + "X" + Integer.toHexString(c) + "X";
			}
		}
		return name;
	}

	/**
	 * 무영의 이름
	 * @param id
	 * @return String
	 */
	public static String anonName(String id) {
		String name = "_:A";
		final String sid = id.toString();
		for (int i = 0; i < sid.length(); i++) {
			final char c = sid.charAt(i);
			if (c == 'X') {
				name = name + "XX";
			} else if (Character.isLetterOrDigit(c)) {
				name = name + c;
			} else {
				name = name + "X" + Integer.toHexString(c) + "X";
			}
		}
		return name;
	}

	/**
	 * utf-8로 변환
	 * @param uni
	 * @return String
	 */
	public static String convertToUTF8(String uni){
		String temp_s = "";
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < uni.length(); i++) {
			if(uni.charAt(i) != '&' && uni.charAt(i) != '#'){
				if(uni.charAt(i) == ';') {
					sb.append("\\u" + Integer.toHexString(Integer.parseInt(temp_s)));
					temp_s = "";
				}
				else
					temp_s += uni.charAt(i);
			}
		}
		return sb.toString();
	}

	/**
	 * 유일한 아이디를 생성해 줌.
	 * @return String
	 */
	public static String getUID(){
		return (new UID()).toString();
	}
	
	/**
	 * UID시간 
	 * @return String
	 */
	public static String getUIDTime(){
		return System.nanoTime()+"";
	}


	/**
	 * 구분자에 따른 문자열 분리
	 * @param str
	 * @param seperator
	 * @return String[]
	 */
	private static String[] internalSplit(String str, String seperator) {
		try {
			final StringTokenizer fieldToken = new StringTokenizer(str, seperator);
			final Vector<String> fieldSplit = new Vector<String>();			
			while(fieldToken.hasMoreTokens()) {
				fieldSplit.addElement(fieldToken.nextToken());
			}

			final String[] arrayField = new String[fieldSplit.size()];
			for(int k=0;k<fieldSplit.size();k++) {
				final String ss = fieldSplit.elementAt(k);
				arrayField[k]= ss;
			}
			return arrayField;
		}
		catch(final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 구분자에 따른 문자열 분리
	 * @param str
	 * @param seperator
	 * @param trim
	 * @return String[]
	 */
	private static String[] internalSplit(String str, String seperator, boolean trim)
	{
		try {
			StringTokenizer fieldToken = new StringTokenizer(str, seperator);

			Vector<String> fieldSplit = new Vector<String>();			
			while(fieldToken.hasMoreTokens())
			{
				fieldSplit.addElement(fieldToken.nextToken());
			}

			String[] arrayField = new String[fieldSplit.size()];

			for(int k=0;k<fieldSplit.size();k++)
			{
				String ss = (String)fieldSplit.elementAt(k);
				arrayField[k]= (trim)?ss.trim():ss;
			}
			return arrayField;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 구분자에 따른 문자열 분리
	 * @param str
	 * @param seperator
	 * @param trim
	 * @param appended_first
	 * @return String[]
	 */
	private static String[] internalSplit(String str, String seperator, boolean trim, String appended_first)
	{
		try {
			StringTokenizer fieldToken = new StringTokenizer(str, seperator);

			Vector<String> fieldSplit = new Vector<String>();			
			while(fieldToken.hasMoreTokens())
			{
				fieldSplit.addElement(fieldToken.nextToken());
			}

			String[] arrayField = new String[fieldSplit.size()];

			for(int k=0;k<fieldSplit.size();k++)
			{
				String ss = (String)fieldSplit.elementAt(k);
				arrayField[k]= (trim)?(appended_first != null)?appended_first+ss.trim():ss.trim():(appended_first != null)?appended_first+ss:ss;				
			}
			return arrayField;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 구분자로 문자열 분리
	 * @param input
	 * @param seperator
	 * @return String[]
	 */
	public static String[] regSplit(String input, String seperator)
	{
		Pattern p = Pattern.compile(seperator);
		return p.split(input.toString());
	}

	/**
	 * 구분자로 문자열 분리
	 * @param input
	 * @param seperator
	 * @return String[]
	 */
	public static String[] regSplit(StringBuffer input, String seperator) {
		final Pattern p = Pattern.compile(seperator);
		return p.split(input.toString());
	}

	/**
	 * substring를 이용한 치환: replaceAll
	 * @param s
	 * @param oldString
	 * @param newString
	 * @return String
	 */
	public static String replace(
			String s,
			String oldString,
			String newString) {
		if(s == null)
			return "";
		String result = "";
		final int length = oldString.length();
		int pos = s.indexOf(oldString);
		int lastPos = 0;
		while (pos >= 0) {
			result = result + s.substring(lastPos, pos) + newString;
			lastPos = pos + length;
			pos = s.indexOf(oldString, lastPos);
		}
		return result + s.substring(lastPos, s.length());
	}

	/**
	 * 문자열 분리
	 * @param str
	 * @param seperator
	 * @param trim
	 * @param appended_first
	 * @return String[]
	 */
	public static String[] split(String str, String seperator, boolean trim, String appended_first)
	{
		return internalSplit(str, seperator, trim, appended_first);
	}

	/**
	 * 문자열 분리
	 * @param str
	 * @param seperator
	 * @return String[]
	 */
	public static String[] split(StringBuffer str, String seperator) {
		return internalSplit(str.toString(), seperator);
	}

	/**
	 * 특수문자 제거
	 * @param ch
	 * @return char
	 */
	public static char unEscape( char ch )
	{
		switch (ch)
		{
		case '\\':
		case '\"':
		case '\'': return ch;
		case 'n': return '\n';
		case 'r': return '\r';
		case 's': return ' ';
		case 't': return '\t';
		case 'f': return '\f';
		default: return ch;
		}
	}

	/**
	 * 특수문자 제거
	 * @param spelling
	 * @return String
	 */
	public static String unEscape( String spelling )
	{
		if (spelling.indexOf( '\\' ) < 0) return spelling;
		final StringBuffer result = new StringBuffer( spelling.length() );
		int start = 0;
		while (true)
		{
			final int b = spelling.indexOf( '\\', start );
			if (b < 0) break;
			result.append( spelling.substring( start, b ) );
			result.append( unEscape( spelling.charAt( b + 1 ) ) );
			start = b + 2;
		}
		result.append( spelling.substring( start ) );
		return result.toString();
	}

	/**
	 * 유니코드를 MS949로 변환
	 * @param uni
	 * @return String
	 */
	public static String uniDecode( String uni){
		final StringBuffer str = new StringBuffer();
		for( int i= uni.indexOf("\\u") ; i > -1 ; i = uni.indexOf("\\u") ){
			str.append( uni.substring( 0, i ) );
			str.append( String.valueOf( (char)Integer.parseInt( uni.substring( i + 2, i + 6 ) ,16) ) );
			uni = uni.substring( i +6);
		}
		str.append( uni );
		return str.toString();

	}

	/**
	 * 유니코드로 변환
	 * @param s
	 * @return String
	 */
	public static String uniEncode(String s){
		final StringBuffer uni_s = new StringBuffer();
		String temp_s = null;
		for( int i=0 ; i < s.length() ; i++){
			temp_s = Integer.toHexString( s.charAt(i) ).toUpperCase();
			for( int j = temp_s.length() ; j < 4 ; j++ ){
				temp_s = "0" + temp_s;
			}
			uni_s.append( "\\u" );
			uni_s.append( temp_s );			
		}
		return uni_s.toString();

	}

	/**
	 * String에 대한 유니코드 변환(한글, 특수문자)
	 * @param s
	 * @return String
	 */
	public static String writeString(String s) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' || c == '"') {
				sb.append("\\");
				sb.append(c);
			} else if (c == '\n') {
				sb.append("\\n");
			} 
			else if (c == '\r') {
				sb.append("\\r");
			} else if (c == '\t') {
				sb.append("\\t");
			} else if (c >= 32 && c < 127) {
				sb.append(c);
			} else {
				final String hexstr = Integer.toHexString(c).toUpperCase();
				int pad = 4 - hexstr.length();
				sb.append("\\u");

				for (; pad > 0; pad--)
					sb.append("0");
				sb.append(hexstr);
			}
		}
		return sb.toString();
	}

	/**
	 * uri에 대한 유니코드 변환(한글, 특수문자)
	 * @param s
	 * @return String
	 */
	public static String writeURIString(String s) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c < okURIChars.length && okURIChars[c]) {
				sb.append(c);
			} else {
				final String hexstr = Integer.toHexString(c).toUpperCase();
				int pad = 4 - hexstr.length();
				sb.append("\\u");
				for (; pad > 0; pad--)
					sb.append("0");
				sb.append(hexstr);
			}
		}
		return sb.toString();
	}

	/**
	 * 스트링에 여러개의 스트링을 붙일 경우 사용
	 * @param sep
	 * @param a
	 * @return String
	 */ 
	public static String join(String sep, String[]a)
	{
		if ( a.length == 0 )
			return "" ;

		if ( a.length == 1)
			return a[0] ;

		StringBuffer sbuff = new StringBuffer() ;
		sbuff.append(a[0]) ;

		for ( int i = 1 ; i < a.length ; i++ )
		{
			if ( sep != null )
				sbuff.append(sep) ;
			sbuff.append(a[i]) ;
		}
		return sbuff.toString() ;
	}

	/**
	 * 스트링에 여러개의 리스트 스트링을 붙일 경우 사용
	 * @param sep
	 * @param a
	 * @return String
	 */
	public static String join(String sep, List<String> a)
	{
		return join(sep, a.toArray(new String[0])) ;
	}
	
	/**
	 * 날짜를 지정된 형태로 변환
	 * @param onem2mDate
	 * @return String
	 */
	public static String makeXsdDateFromOnem2mDate(String onem2mDate){
		if(onem2mDate == null || onem2mDate.equals("")) {
			onem2mDate = Utils.dateFormat.format(new Date());
		}
		String year = onem2mDate.substring(0,4);
		String month = onem2mDate.substring(4,6);
		String day = onem2mDate.substring(6,8);
		String hour = onem2mDate.substring(9,11);
		String minute = onem2mDate.substring(11,13);
		String second = onem2mDate.substring(13,15);
		return year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+second;
	}
}
