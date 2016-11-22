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

	public static String getUUID(String str){
		return UUID.fromString(str).toString();
	}
	
	
	public static String numberFormat(int num) {
		NumberFormat nf = NumberFormat.getInstance();
		return nf.format(num);
	}
	
	public static String numberFormat(long num) {
		NumberFormat nf = NumberFormat.getInstance();
		return nf.format(num);
	}
	
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
	 * &#47672;와 같은 문자를 utf-8로 변환
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
	
	public static String getUIDTime(){
		return System.nanoTime()+"";
	}


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



	public static String[] regSplit(String input, String seperator)
	{
		Pattern p = Pattern.compile(seperator);
		return p.split(input.toString());
	}

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

	// Splitting
	//-----------------------------------------------------------------------
	/**
	 * <p>Splits the provided text into an array, using whitespace as the
	 * separator.
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 *
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as one separator.
	 * For more control over the split use the StrTokenizer class.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 *
	 * <pre>
	 * StringUtils.split(null)       = null
	 * StringUtils.split("")         = []
	 * StringUtils.split("abc def")  = ["abc", "def"]
	 * StringUtils.split("abc  def") = ["abc", "def"]
	 * StringUtils.split(" abc ")    = ["abc"]
	 * </pre>
	 *
	 * @param str  the String to parse, may be null
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	public static String[] split(String str) {
		return split(str, null, -1);
	}

	/**
	 * <p>Splits the provided text into an array, separator specified.
	 * This is an alternative to using StringTokenizer.</p>
	 *
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as one separator.
	 * For more control over the split use the StrTokenizer class.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 *
	 * <pre>
	 * StringUtils.split(null, *)         = null
	 * StringUtils.split("", *)           = []
	 * StringUtils.split("a.b.c", '.')    = ["a", "b", "c"]
	 * StringUtils.split("a..b.c", '.')   = ["a", "b", "c"]
	 * StringUtils.split("a:b:c", '.')    = ["a:b:c"]
	 * StringUtils.split("a\tb\nc", null) = ["a", "b", "c"]
	 * StringUtils.split("a b c", ' ')    = ["a", "b", "c"]
	 * </pre>
	 *
	 * @param str  the String to parse, may be null
	 * @param separatorChar  the character used as the delimiter,
	 *  <code>null</code> splits on whitespace
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.0
	 */
	public static String[] split(String str, char separatorChar) {
		return splitWorker(str, separatorChar, false);
	}

	/**
	 * <p>Splits the provided text into an array, separators specified.
	 * This is an alternative to using StringTokenizer.</p>
	 *
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as one separator.
	 * For more control over the split use the StrTokenizer class.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> separatorChars splits on whitespace.</p>
	 *
	 * <pre>
	 * StringUtils.split(null, *)         = null
	 * StringUtils.split("", *)           = []
	 * StringUtils.split("abc def", null) = ["abc", "def"]
	 * StringUtils.split("abc def", " ")  = ["abc", "def"]
	 * StringUtils.split("abc  def", " ") = ["abc", "def"]
	 * StringUtils.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
	 * </pre>
	 *
	 * @param str  the String to parse, may be null
	 * @param separatorChars  the characters used as the delimiters,
	 *  <code>null</code> splits on whitespace
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	public static String[] split(String str, String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}


	public static String[] split(String str, String seperator, boolean trim)
	{
		return internalSplit(str, seperator, trim);
	}
	
	public static String[] split(String str, String seperator, boolean trim, String appended_first)
	{
		return internalSplit(str, seperator, trim, appended_first);
	}

	/**
	 * <p>Splits the provided text into an array with a maximum length,
	 * separators specified.</p>
	 *
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as one separator.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> separatorChars splits on whitespace.</p>
	 *
	 * <p>If more than <code>max</code> delimited substrings are found, the last
	 * returned string includes all characters after the first <code>max - 1</code>
	 * returned strings (including separator characters).</p>
	 *
	 * <pre>
	 * StringUtils.split(null, *, *)            = null
	 * StringUtils.split("", *, *)              = []
	 * StringUtils.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
	 * StringUtils.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
	 * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
	 * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
	 * </pre>
	 *
	 * @param str  the String to parse, may be null
	 * @param separatorChars  the characters used as the delimiters,
	 *  <code>null</code> splits on whitespace
	 * @param max  the maximum number of elements to include in the
	 *  array. A zero or negative value implies no limit
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	public static String[] split(String str, String separatorChars, int max) {
		return splitWorker(str, separatorChars, max, false);
	}
	public static String[] split(StringBuffer str, String seperator) {
		return internalSplit(str.toString(), seperator);
	}

	/**
	 * <p>Splits the provided text into an array, separator string specified.</p>
	 *
	 * <p>The separator(s) will not be included in the returned String array.
	 * Adjacent separators are treated as one separator.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> separator splits on whitespace.</p>
	 *
	 * <pre>
	 * StringUtils.splitByWholeSeparator(null, *)               = null
	 * StringUtils.splitByWholeSeparator("", *)                 = []
	 * StringUtils.splitByWholeSeparator("ab de fg", null)      = ["ab", "de", "fg"]
	 * StringUtils.splitByWholeSeparator("ab   de fg", null)    = ["ab", "de", "fg"]
	 * StringUtils.splitByWholeSeparator("ab:cd:ef", ":")       = ["ab", "cd", "ef"]
	 * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-") = ["ab", "cd", "ef"]
	 * </pre>
	 *
	 * @param str  the String to parse, may be null
	 * @param separator  String containing the String to be used as a delimiter,
	 *  <code>null</code> splits on whitespace
	 * @return an array of parsed Strings, <code>null</code> if null String was input
	 */
	public static String[] splitByWholeSeparator(String str, String separator) {
		return splitByWholeSeparator( str, separator, -1 ) ;
	}

	/**
	 * <p>Splits the provided text into an array, separator string specified.
	 * Returns a maximum of <code>max</code> substrings.</p>
	 *
	 * <p>The separator(s) will not be included in the returned String array.
	 * Adjacent separators are treated as one separator.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> separator splits on whitespace.</p>
	 *
	 * <pre>
	 * StringUtils.splitByWholeSeparator(null, *, *)               = null
	 * StringUtils.splitByWholeSeparator("", *, *)                 = []
	 * StringUtils.splitByWholeSeparator("ab de fg", null, 0)      = ["ab", "de", "fg"]
	 * StringUtils.splitByWholeSeparator("ab   de fg", null, 0)    = ["ab", "de", "fg"]
	 * StringUtils.splitByWholeSeparator("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
	 * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
	 * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
	 * </pre>
	 *
	 * @param str  the String to parse, may be null
	 * @param separator  String containing the String to be used as a delimiter,
	 *  <code>null</code> splits on whitespace
	 * @param max  the maximum number of elements to include in the returned
	 *  array. A zero or negative value implies no limit.
	 * @return an array of parsed Strings, <code>null</code> if null String was input
	 */
	public static String[] splitByWholeSeparator( String str, String separator, int max ) {
		if (str == null) {
			return null;
		}

		int len = str.length() ;

		if (len == 0) {
			return EMPTY_STRING_ARRAY;
		}

		if ( ( separator == null ) || ( "".equals( separator ) ) ) {
			// Split on whitespace.
			return split( str, null, max ) ;
		}


		int separatorLength = separator.length() ;

		ArrayList substrings = new ArrayList() ;
		int numberOfSubstrings = 0 ;
		int beg = 0 ;
		int end = 0 ;
		while ( end < len ) {
			end = str.indexOf( separator, beg ) ;

			if ( end > -1 ) {
				if ( end > beg ) {
					numberOfSubstrings += 1 ;

					if ( numberOfSubstrings == max ) {
						end = len ;
						substrings.add( str.substring( beg ) ) ;
					} else {
						// The following is OK, because String.substring( beg, end ) excludes
						// the character at the position 'end'.
						substrings.add( str.substring( beg, end ) ) ;

						// Set the starting point for the next search.
						// The following is equivalent to beg = end + (separatorLength - 1) + 1,
						// which is the right calculation:
						beg = end + separatorLength ;
					}
				} else {
					// We found a consecutive occurrence of the separator, so skip it.
					beg = end + separatorLength ;
				}
			} else {
				// String.substring( beg ) goes from 'beg' to the end of the String.
				substrings.add( str.substring( beg ) ) ;
				end = len ;
			}
		}

		return (String[]) substrings.toArray( new String[substrings.size()] ) ;
	}

	//-----------------------------------------------------------------------
	/**
	 * <p>Splits the provided text into an array, using whitespace as the
	 * separator, preserving all tokens, including empty tokens created by 
	 * adjacent separators. This is an alternative to using StringTokenizer.
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
	 *
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as separators for empty tokens.
	 * For more control over the split use the StrTokenizer class.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 *
	 * <pre>
	 * StringUtils.splitPreserveAllTokens(null)       = null
	 * StringUtils.splitPreserveAllTokens("")         = []
	 * StringUtils.splitPreserveAllTokens("abc def")  = ["abc", "def"]
	 * StringUtils.splitPreserveAllTokens("abc  def") = ["abc", "", "def"]
	 * StringUtils.splitPreserveAllTokens(" abc ")    = ["", "abc", ""]
	 * </pre>
	 *
	 * @param str  the String to parse, may be <code>null</code>
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String[] splitPreserveAllTokens(String str) {
		return splitWorker(str, null, -1, true);
	}

	/**
	 * <p>Splits the provided text into an array, separator specified,
	 * preserving all tokens, including empty tokens created by adjacent
	 * separators. This is an alternative to using StringTokenizer.</p>
	 *
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as separators for empty tokens.
	 * For more control over the split use the StrTokenizer class.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.</p>
	 *
	 * <pre>
	 * StringUtils.splitPreserveAllTokens(null, *)         = null
	 * StringUtils.splitPreserveAllTokens("", *)           = []
	 * StringUtils.splitPreserveAllTokens("a.b.c", '.')    = ["a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("a..b.c", '.')   = ["a", "", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("a:b:c", '.')    = ["a:b:c"]
	 * StringUtils.splitPreserveAllTokens("a\tb\nc", null) = ["a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("a b c", ' ')    = ["a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("a b c ", ' ')   = ["a", "b", "c", ""]
	 * StringUtils.splitPreserveAllTokens("a b c ", ' ')   = ["a", "b", "c", "", ""]
	 * StringUtils.splitPreserveAllTokens(" a b c", ' ')   = ["", a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens("  a b c", ' ')  = ["", "", a", "b", "c"]
	 * StringUtils.splitPreserveAllTokens(" a b c ", ' ')  = ["", a", "b", "c", ""]
	 * </pre>
	 *
	 * @param str  the String to parse, may be <code>null</code>
	 * @param separatorChar  the character used as the delimiter,
	 *  <code>null</code> splits on whitespace
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String[] splitPreserveAllTokens(String str, char separatorChar) {
		return splitWorker(str, separatorChar, true);
	}


	/**
	 * <p>Splits the provided text into an array, separators specified, 
	 * preserving all tokens, including empty tokens created by adjacent
	 * separators. This is an alternative to using StringTokenizer.</p>
	 *
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as separators for empty tokens.
	 * For more control over the split use the StrTokenizer class.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> separatorChars splits on whitespace.</p>
	 *
	 * <pre>
	 * StringUtils.splitPreserveAllTokens(null, *)           = null
	 * StringUtils.splitPreserveAllTokens("", *)             = []
	 * StringUtils.splitPreserveAllTokens("abc def", null)   = ["abc", "def"]
	 * StringUtils.splitPreserveAllTokens("abc def", " ")    = ["abc", "def"]
	 * StringUtils.splitPreserveAllTokens("abc  def", " ")   = ["abc", "", def"]
	 * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":")   = ["ab", "cd", "ef"]
	 * StringUtils.splitPreserveAllTokens("ab:cd:ef:", ":")  = ["ab", "cd", "ef", ""]
	 * StringUtils.splitPreserveAllTokens("ab:cd:ef::", ":") = ["ab", "cd", "ef", "", ""]
	 * StringUtils.splitPreserveAllTokens("ab::cd:ef", ":")  = ["ab", "", cd", "ef"]
	 * StringUtils.splitPreserveAllTokens(":cd:ef", ":")     = ["", cd", "ef"]
	 * StringUtils.splitPreserveAllTokens("::cd:ef", ":")    = ["", "", cd", "ef"]
	 * StringUtils.splitPreserveAllTokens(":cd:ef:", ":")    = ["", cd", "ef", ""]
	 * </pre>
	 *
	 * @param str  the String to parse, may be <code>null</code>
	 * @param separatorChars  the characters used as the delimiters,
	 *  <code>null</code> splits on whitespace
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String[] splitPreserveAllTokens(String str, String separatorChars) {
		return splitWorker(str, separatorChars, -1, true);
	}

	/**
	 * <p>Splits the provided text into an array with a maximum length,
	 * separators specified, preserving all tokens, including empty tokens 
	 * created by adjacent separators.</p>
	 *
	 * <p>The separator is not included in the returned String array.
	 * Adjacent separators are treated as separators for empty tokens.
	 * Adjacent separators are treated as one separator.</p>
	 *
	 * <p>A <code>null</code> input String returns <code>null</code>.
	 * A <code>null</code> separatorChars splits on whitespace.</p>
	 *
	 * <p>If more than <code>max</code> delimited substrings are found, the last
	 * returned string includes all characters after the first <code>max - 1</code>
	 * returned strings (including separator characters).</p>
	 *
	 * <pre>
	 * StringUtils.splitPreserveAllTokens(null, *, *)            = null
	 * StringUtils.splitPreserveAllTokens("", *, *)              = []
	 * StringUtils.splitPreserveAllTokens("ab de fg", null, 0)   = ["ab", "cd", "ef"]
	 * StringUtils.splitPreserveAllTokens("ab   de fg", null, 0) = ["ab", "cd", "ef"]
	 * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
	 * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
	 * StringUtils.splitPreserveAllTokens("ab   de fg", null, 2) = ["ab", "  de fg"]
	 * StringUtils.splitPreserveAllTokens("ab   de fg", null, 3) = ["ab", "", " de fg"]
	 * StringUtils.splitPreserveAllTokens("ab   de fg", null, 4) = ["ab", "", "", "de fg"]
	 * </pre>
	 *
	 * @param str  the String to parse, may be <code>null</code>
	 * @param separatorChars  the characters used as the delimiters,
	 *  <code>null</code> splits on whitespace
	 * @param max  the maximum number of elements to include in the
	 *  array. A zero or negative value implies no limit
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 * @since 2.1
	 */
	public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
		return splitWorker(str, separatorChars, max, true);
	}

	/**
	 * Performs the logic for the <code>split</code> and 
	 * <code>splitPreserveAllTokens</code> methods that do not return a
	 * maximum array length.
	 *
	 * @param str  the String to parse, may be <code>null</code>
	 * @param separatorChar the separate character
	 * @param preserveAllTokens if <code>true</code>, adjacent separators are
	 * treated as empty token separators; if <code>false</code>, adjacent
	 * separators are treated as one separator.
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return EMPTY_STRING_ARRAY;
		}
		List list = new ArrayList();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {
					list.add(str.substring(start, i));
					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			} else {
				lastMatch = false;
			}
			match = true;
			i++;
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * Performs the logic for the <code>split</code> and 
	 * <code>splitPreserveAllTokens</code> methods that return a maximum array 
	 * length.
	 *
	 * @param str  the String to parse, may be <code>null</code>
	 * @param separatorChars the separate character
	 * @param max  the maximum number of elements to include in the
	 *  array. A zero or negative value implies no limit.
	 * @param preserveAllTokens if <code>true</code>, adjacent separators are
	 * treated as empty token separators; if <code>false</code>, adjacent
	 * separators are treated as one separator.
	 * @return an array of parsed Strings, <code>null</code> if null String input
	 */
	private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
		// Performance tuned for 2.0 (JDK1.4)
		// Direct code is quicker than StringTokenizer.
		// Also, StringTokenizer uses isSpace() not isWhitespace()

		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return EMPTY_STRING_ARRAY;
		}
		List list = new ArrayList();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				} else {
					lastMatch = false;
				}
				match = true;
				i++;
			}
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static StringBuffer string(String s, boolean singleQuoteLiteral)
	{
		final StringBuffer sbuff = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);

			// Escape escapes and quotes
			if (c == '\\' || c == '"' )
			{
				sbuff.append('\\') ;
				sbuff.append(c) ;
				continue ;
			}

			// Whitespace
			if ( singleQuoteLiteral && ( c == '\n' || c == '\r' || c == '\f' || c == '\t'))
			{
				if (c == '\n') sbuff.append("\\n");
				if (c == '\t') sbuff.append("\\t");
				if (c == '\r') sbuff.append("\\r");
				if (c == '\f') sbuff.append("\\f");
				continue ;
			}

			sbuff.append(c) ;
		}

		return sbuff;
	}

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
//			else if (c == '$') {
//				sb.append("\\$");
//			} 
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
	 * @return
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
	 * @return
	 */
	public static String join(String sep, List<String> a)
	{
		return join(sep, a.toArray(new String[0])) ;
	}
	
	// 20151203T122321 --> 2015-12-03T12:23:21
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
//		System.out.println(year);
//		System.out.println(month);
//		System.out.println(day);
//		System.out.println(hour);
//		System.out.println(minute);
//		System.out.println(second);
//	    SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//	    Calendar cal = GregorianCalendar.getInstance().setTime(sd1.parse(year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+second));
	    
		return year+"-"+month+"-"+day+"T"+hour+":"+minute+":"+second;
	}
	
	public static void main(String[] args) {
		System.out.println(StrUtils.makeXsdDateFromOnem2mDate("20151214T164052"));
	}
}
