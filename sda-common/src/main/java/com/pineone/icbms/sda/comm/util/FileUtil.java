package com.pineone.icbms.sda.comm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
 
/**
 * 파일처리 유틸성 클래스
 */
public class FileUtil {
	public static final String langXML          = "RDF/XML" ;
	public static final String langXMLAbbrev    = "RDF/XML-ABBREV" ;
	public static final String langNTriple      = "N-TRIPLE" ;
	public static final String langN3           = "N3" ;
	public static final String langFLO          = "FLO" ;
	public static final String langTurtle       = "TURTLE" ;

	/** Java name for UTF-8 encoding */
	public static final String encodingUTF8     = "utf-8" ;

	static Charset utf8 = null ;
	static {
		try {
			utf8 = Charset.forName(encodingUTF8) ;
		} catch (Throwable ex)
		{
			System.err.println("Failed to get charset for UTF-8") ;
		}
	}

	/** Create a reader that uses UTF-8 encoding */     
	static public Reader asUTF8(InputStream in) {       
		return new InputStreamReader(in, utf8.newDecoder());
	}

	/** Create a buffered reader that uses UTF-8 encoding */    
	static public BufferedReader asBufferedUTF8(InputStream in) {
		return new BufferedReader(asUTF8(in)) ;
	}

	/** Create a writer that uses UTF-8 encoding */ 
	static public Writer asUTF8(OutputStream out) {
		return new OutputStreamWriter(out, utf8.newEncoder());
	}

	/** Create a print writer that uses UTF-8 encoding */ 
	static public PrintWriter asPrintWriterUTF8(OutputStream out) {
		return new PrintWriter(asUTF8(out)); 
	}

	/**
	 *   파일 확장자에 따른 온톨로지 파일 유형 판단.
	 * @param name
	 * @param otherwise
	 * @return String
	 */
	public static String guessLang( String name, String otherwise )
	{        
		String suffix = getFilenameExt( name );
		if (suffix.startsWith( "n3" ))   return langN3;
		if (suffix.startsWith( "nt" ))   return langNTriple;
		if (suffix.startsWith( "ttl" ))  return langTurtle ;
		if (suffix.startsWith( "rdf" ))  return langXML;
		if (suffix.startsWith( "owl" ))  return langXMLAbbrev;
		if (suffix.startsWith( "flo" ))  return langFLO;
		return otherwise; 
	}  

	public static String guessLang(String urlStr)
	{
		return guessLang(urlStr, null) ;
	}

	/**
	 *   아스키 체크
	 * @param ch
	 * @return boolean
	 */
	public static boolean isASCIILetter(char ch)
	{
		return ( ch >= 'a' && ch <= 'z' ) || ( ch >= 'A' && ch <= 'Z' ) ;
	}

	/**
	 *   OS에 따른 폴더 구분자 
	 * @param dir
	 * @return String
	 */
	public static String getDirectoryDelimeter(String dir){
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
			return dir + "\\";
		} else {
			return dir + "/";
		}
	}

	/**
	 * 디렉토리 구분자 
	 * @return String
	 */
	public static String getDirectoryDelimeter(){
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")){
			return  "\\";
		} else {
			return "/";
		}
	}

	/**
	 * 파일을 문자열로 리턴
	 * @param source
	 * @return String
	 */
	public static String fileToString(File source){
		return fileToStringBuffer(source).toString();
	}

	/**
	 * 파일을 문자열 버퍼에 읽기
	 * @param source
	 * @return StringBuffer
	 */
	public static StringBuffer fileToStringBuffer(File source){
		BufferedReader br =  fileModelReader(source, encodingUTF8);
		StringBuffer sb = new StringBuffer();
		String tmp="";
		try {
			while((tmp = br.readLine()) != null){
				sb.append(tmp);
				sb.append(System.getProperty("line.separator")); // jhjeong 추가 2013-01-08
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb;
	}


	/**
	 * 파일 모델 리더
	 * @param source
	 * @return BufferedReader
	 */
	public static BufferedReader fileModelReader(File source){
		return fileModelReader(source, 0);
	}

	/**
	 * 파일 모델 리더
	 * @param source
	 * @param encoding
	 * @return BufferedReader
	 */
	public static BufferedReader fileModelReader(File source, String encoding){
		return fileModelReader(source, 0, encoding);
	}

	/**
	 * 파일 모델 리더
	 * @param source
	 * @param sz
	 * @return BufferedReader
	 */
	public static BufferedReader fileModelReader(File source, int sz){
		BufferedReader br = null;
		try {
			if(sz == 0)
				br = new BufferedReader(new FileReader(source));
			else
				br = new BufferedReader(new FileReader(source), sz);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return br;
	}

	/**
	 *   파일로딩(인코딩), BOM(byte order mark)에 대한 체크 수행함.
	 * @param source
	 * @param encoding
	 * @return InputStream
	 */
	public static InputStream fileInputStream(File source, String encoding) {
		try {
			return checkForUtf8ByteOrderMark(new FileInputStream(source));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 파일을 지정한 사이즈 만큼 읽기
	 * @param source
	 * @param size
	 * @param encoding
	 * @return InputStream
	 */
	public static InputStream fileInputStream(File source, int size, String encoding) {
		try {
			if(size == 0)
				return checkForUtf8ByteOrderMark(new BufferedInputStream(new FileInputStream(source)));
			else
				return checkForUtf8ByteOrderMark(new BufferedInputStream(new FileInputStream(source), size));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * InputStream을 Byte로 가져오기
	 * @param is
	 * @return byte[]
	 */
	static byte[] getInputStreamToBytes(InputStream is) {
		int read = 0;
		byte[] bytes = new byte[1024*1024*2];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((read = is.read(bytes)) != -1)
				bos.write(bytes,0,read);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	/**
	 * 파일 모델 리더
	 * @param source
	 * @param sz
	 * @param encoding
	 * @return BufferedReader
	 */
	public static BufferedReader fileModelReader(File source, int sz, String encoding){
		return fileModelReader(source, sz, encoding, false);
	}

	/**
	 * 인코딩에 따라 파일을 읽어 드림.
	 * @param source
	 * @param sz
	 * @param encoding
	 * @param force
	 * @return BufferedReader
	 */
	public static BufferedReader fileModelReader(File source, int sz, String encoding, boolean force){
		BufferedReader br = null;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		InputStream is = null;
		try {
			fis = new FileInputStream(source);
			is = checkForUtf8ByteOrderMark(fis);
			bis = new BufferedInputStream (is);
			if(!force) {
				CharsetMatch cm = checkCharset(bis);			
				if(cm != null) {
					String charset = cm.getName();
					if(!charset.equalsIgnoreCase(encoding)) {
						encoding = charset;
					}
				}
			}
			if(sz == 0) {
				br = new BufferedReader( new InputStreamReader(bis, encoding));	
			} else {
				br = new BufferedReader( new InputStreamReader(bis, encoding), sz);
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
			if(sz == 0) {
				if(encoding.length() > 0)
					try {
						br = new BufferedReader( new InputStreamReader(is, "utf-8"));
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				else
					br = new BufferedReader( new InputStreamReader(is));
			} else {
				if(encoding.length() > 0)
					try {
						br = new BufferedReader( new InputStreamReader(is,  "utf-8"), sz);
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				else
					br = new BufferedReader( new InputStreamReader(is), sz);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return br;
	}
	
	/**
	 * 파일 모델 InputStream
	 * @param source
	 * @param encoding
	 * @return InputStream
	 */
	public static InputStream fileModelInputStream(File source, String encoding){
		FileInputStream fis = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(source);
			bis = new BufferedInputStream(fis);
			CharsetMatch cm = checkCharset(bis);			
			if(cm != null) {
				String charset = cm.getName();
				if(!charset.equalsIgnoreCase(encoding)) {
					encoding = charset;
				}
			}
			
			is = checkForUtf8ByteOrderMark(bis);
			return is;
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	
	/**
	 * 캐릭터셋 확인
	 * @param input
	 * @return CharsetMatch
	 */
	public static CharsetMatch checkCharset(byte[] input) {
		CharsetDetector cd = new CharsetDetector();
		cd.setText(input);
		CharsetMatch cm = cd.detect();

		return cm;
	}
	
	/**
	 * 캐릭터셋 확인
	 * @param input
	 * @return CharsetMatch
	 */
	public static CharsetMatch checkCharset(InputStream input) {
		CharsetDetector cd = new CharsetDetector();
		try {
			cd.setText(input);
		} catch (IOException e) {
			try {
				input.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		CharsetMatch cm = cd.detect();

		return cm;
	}
	/**
	 * 파일의 BOM 제거 if exist.
	 * @param inputStream
	 * @return InputStream
	 * @throws IOException
	 */
	public static InputStream checkForUtf8ByteOrderMark(InputStream inputStream) throws IOException {
		PushbackInputStream pushbackInputStream = new PushbackInputStream(new BufferedInputStream(inputStream), 3);
		byte[] bom = new byte[3];
		if (pushbackInputStream.read(bom) != -1) {
			// check for the UTF-8 BOM, and remove it if there. See SWS-393
			if (!(bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
				pushbackInputStream.unread(bom);
			}
		}
		return pushbackInputStream;
	}

	/**
	 * Get the directory part of a filename
	 * @param filename
	 * @return String
	 */
	public static String getDirname(String filename)
	{
		File f = new File(filename) ;
		return f.getParent() ;
	}

	/** Get the basename of a filename
	 * 
	 * @param filename
	 * @return String
	 */
	public static String getBasename(String filename)
	{
		File f = new File(filename) ;
		return f.getName() ;
	}

	public static String getFilenameExt( String filename)
	{
		int iSlash = filename.lastIndexOf( '/' );      
		int iBack = filename.lastIndexOf( '\\' );
		int iExt = filename.lastIndexOf( '.' ); 
		if (iBack > iSlash) iSlash = iBack;
		return iExt > iSlash ? filename.substring( iExt+1 ).toLowerCase() : "";
	}

	/**
	 * 특정 폴더의 파일과 폴더를 모두 지운다.
	 * @param dir
	 * @return boolean
	 */
	public static boolean deleteDir(String dir) {
		return deleteDir(dir, true);
	}
	
	/**
	 * 특정 폴더의 파일과 폴더를 모두 지운다.
	 * @param dir
	 * @param deleteOwner
	 * @return boolean
	 */
	public static boolean deleteDir(String dir, boolean deleteOwner) {
		boolean success = false;
		File dirs = new File(dir);
		if(dirs.exists()) {
			deleteSubDir(dirs);
			success = true;
		} 
		if(success && deleteOwner)
			dirs.delete();
		return success;
	}
	
	/**
	 * 여러파일 지우기
	 * @param dir
	 * @return boolean
	 */
	public static boolean deleteFiles(String dir) {
		boolean success = false;
		File dirs = new File(dir);
		if(dirs.exists() && dirs.isDirectory()) {
			for(File f: dirs.listFiles()) {
				if(f.exists())
					f.delete();
			}
			success = true;
		} 
		return success;
	}

	/**
	 * 파일 지우기
	 * @param f
	 * @return boolean
	 */
	public static boolean deleteFile(String f) {
		boolean success = false;

		File fTarget = new File(f);
		if(fTarget.exists()) {
			success = fTarget.delete();
		} 

		return success;
	}

	/**
	 * 하위디렉토리 삭제
	 * @param subdir
	 * @return void
	 */
	private static void deleteSubDir(File subdir) {
		if(subdir.isFile()) {
			if(subdir.exists()) 
				subdir.delete();
		} else if(subdir.isDirectory()) {
			File[] files = subdir.listFiles();
			for(File file: files) {
				deleteSubDir(file);
			}
			subdir.delete();//모든 파일을 삭제한 후, 폴더를 삭제함.
		}
	}

	/**
	 * 파일 목록
	 * @param path
	 * @param allowedExtension
	 * @return List<File>
	 */
	public static List<File> getFiles(String path, String[] allowedExtension) {
		List<File> r = new ArrayList<File>();
		File rfolder = new File(path);
		if(rfolder.exists() && rfolder.isDirectory()) {
			File[] files = rfolder.listFiles();
			for (File f: files) {
				if(isAllowedExtension(f.getName(), allowedExtension)) {
					r.add(f);
				}
			}
		}

		return r;
	}
	
	/**
	 * 파일 목록
	 * @param path
	 * @param allowedExtension
	 * @return File[]
	 */
	public static File[] listFiles(String path, String[] allowedExtension) {
		List<File> fs = getFiles(path, allowedExtension);
		
		return fs.toArray(new File[fs.size()]);
	}
	
	/**
	 * 절대경로 포함 목록
	 * @param path
	 * @param allowedExtension
	 * @return List<File>
	 */
	public static List<File> getAbsoluteFiles(String path, String[] allowedExtension) {
		List<File> r = new ArrayList<File>();
		File rfolder = new File(path);
		if(rfolder.exists() && rfolder.isDirectory()) {
			File[] files = rfolder.listFiles();
			for (File f: files) {
				if(isAllowedExtension(f.getAbsolutePath(), allowedExtension)) {
					r.add(f);
				}
			}
		}

		return r;
	}

	/**
	 * 절대경로 포함 목록
	 * @param path
	 * @param allowedExtension
	 * @return List<String>
	 */
	public static List<String> getAbsoluteFiles2List(String path, String[] allowedExtension) {
		List<String> r = new ArrayList<String>();
		File rfolder = new File(path);
		if(rfolder.exists() && rfolder.isDirectory()) {
			File[] files = rfolder.listFiles();
			for (File f: files) {
				if(isAllowedExtension(f.getName(), allowedExtension)) {
					r.add(f.getAbsolutePath());
				}
			}
		}

		return r;
	}
	
	/**
	 * 상대경로 포함 목록
	 * @param path
	 * @param allowedExtension
	 * @return List<String>
	 */
	public static List<String> getCanonicalFiles2List(List<String> path, String[] allowedExtension) {
		List<String> r = new ArrayList<String>();
		for(String p: path) {
			List<String> r2 = new ArrayList<String>();
			File rfolder = new File(p);
			if(rfolder.exists() && rfolder.isDirectory()) {
				File[] files = rfolder.listFiles();
				for (File f: files) {
					if(f.isFile() && !f.getName().startsWith(".")) {
						if(allowedExtension.length>0) {
							if(isAllowedExtension(f.getName(), allowedExtension)) {
								r2.add(p + "/" + f.getName());
							}	
						} else {
							r2.add(p + "/" + f.getName());
						}
					}
				}
				r.addAll(r2);
			}
		}

		return r;
	}
	
	/**
	 * 상대경로 포함 목록
	 * @param path
	 * @param allowedExtension
	 * @param checkExt
	 * @return List<String>
	 */
	public static List<String> getCanonicalFiles2List(String[] path, String[] allowedExtension, boolean checkExt) {
		List<String> r = new ArrayList<String>();
		for(String p: path) {
			List<String> r2 = new ArrayList<String>();
			File rfolder = new File(p);
			if(rfolder.exists() && rfolder.isDirectory()) {
				File[] files = rfolder.listFiles();
				for (File f: files) {
					if(!checkExt || isAllowedExtension(f.getName(), allowedExtension)) {
						try {
							r2.add(f.getCanonicalPath());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				r.addAll(r2);
			}
		}

		return r;
	}
	
	/**
	 * 파일 목록
	 * @param path
	 * @param allowedExtension
	 * @return List<String>
	 */
	public static List<String> getFiles2List(String path, String[] allowedExtension) {
		List<String> r = new ArrayList<String>();
		File rfolder = new File(path);
		if(rfolder.exists() && rfolder.isDirectory()) {
			File[] files = rfolder.listFiles();
			for (File f: files) {
				if(isAllowedExtension(f.getName(), allowedExtension)) {
					r.add(f.getName());
				}
			}
		}

		return r;
	}
	
	/**
	 * 파일목록
	 * @param paths
	 * @param allowedExtension
	 * @return List<String>
	 */
	public static List<String> getFiles2List(String[] paths, String[] allowedExtension) {
		List<String> r = new ArrayList<String>();
		for (String p: paths) {
			List<String> flist = getFiles2List(p, allowedExtension);
			if(!flist.isEmpty())
				r.addAll(flist);
		}

		return r;
	}

	/**
	 * 디렉토리 목록
	 * @param path
	 * @return String[]
	 */
	public static String[] getDirsArray(String path){
		List<String> ds = getDirs(path);
		return ds.toArray(new String[ds.size()]);
	}

	/**
	 * 디렉토리 목록
	 * @param path
	 * @return List<String>
	 */
	public static List<String> getDirs(String path){
		List<String> r = new ArrayList<String>();
		File rfolder = new File(path);
		if(rfolder.exists() && rfolder.isDirectory()) {
			File[] files = rfolder.listFiles();
			for (File f: files) {
				if(f.isDirectory() && !f.getName().startsWith(".")) {
					r.add(f.getName());
				}
			}
		}

		return r;
	}
	
	/**
	 * 디렉토리 목록
	 * @param pathAll
	 * @param path
	 * @return List<String>
	 */
	public static List<String> getDirs(List<String> pathAll, String path){
		File rfolder = new File(path);
		if(rfolder.exists() && rfolder.isDirectory()) {
			File[] files = rfolder.listFiles();
			for (File f: files) {
				if(f.isDirectory() && !f.getName().startsWith(".")) {
					pathAll.add(path + "/" + f.getName());
					File sub = new File(path + "/" + f.getName());					
					if(sub.listFiles().length>0)
						getDirs(pathAll, path + "/" + f.getName());
				}
			}
		}

		return pathAll;
	}
	
	/**
	 * 디렉토리 목록
	 * @param path
	 * @return List<String>
	 */
	public static List<String> getAbsDirs(String path){
		List<String> r = new ArrayList<String>();
		File rfolder = new File(path);
		if(rfolder.exists() && rfolder.isDirectory()) {
			File[] files = rfolder.listFiles();
			for (File f: files) {
				if(f.isDirectory() && !f.getName().startsWith(".")) {
					r.add(f.getAbsolutePath());
				}
			}
		}

		return r;
	}

	/**
	 * 허용가능한 확장자
	 * @param fExt
	 * @param allowedExtension
	 * @return boolean
	 */
	private static boolean isAllowedExtension(String fExt, String[] allowedExtension) {
		for(String ext: allowedExtension) {
			if(fExt.endsWith(ext))
				return true;
		}
		return false;
	}

	/**
	 * 파일에 쓰기
	 * @param fileName
	 * @param content
	 * @param encode
	 * @return boolean
	 */
	public static boolean writeFile(String fileName, String content, String encode){
		OutputStreamWriter osw = null;
		boolean success = false;
		try {
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			osw = new OutputStreamWriter(
					new BufferedOutputStream(fos), Charset.forName(encode).newEncoder());
			osw.write(content);
			osw.flush();
			osw.close();
			fos.close();
			success = true;
		} catch (FileNotFoundException e) {
			success = false;
			e.printStackTrace();
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
		} 
		return success;	
	}

	/**
	 * 파일 복사
	 * @param sourceFile
	 * @param targetFile
	 * @return void
	 */
	public static void copyFile(File sourceFile, File targetFile){
		FileInputStream source;
		FileOutputStream destination;
		try {
			source = new FileInputStream(sourceFile);
			destination = new FileOutputStream(targetFile);
			FileChannel sourceFileChannel = source.getChannel();
			FileChannel destinationFileChannel = destination.getChannel();

			long size = sourceFileChannel.size();
			sourceFileChannel.transferTo(0, size, destinationFileChannel);
			sourceFileChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 채널로 파일복사
	 * @param aSourceFile
	 * @param aTargetFile
	 * @param aAppend
	 * @return void
	 */
	public static void copyWithChannels(File aSourceFile, File aTargetFile, boolean aAppend) {
		ensureTargetDirectoryExists(aTargetFile.getParentFile());
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		try{
			try {
				inStream = new FileInputStream(aSourceFile);
				inChannel = inStream.getChannel();
				outStream = new  FileOutputStream(aTargetFile, aAppend);        
				outChannel = outStream.getChannel();
				long bytesTransferred = 0;
				//defensive loop - there's usually only a single iteration :
				while(bytesTransferred < inChannel.size()){
					bytesTransferred += inChannel.transferTo(0, inChannel.size(), outChannel);
				}
			}
			finally {
				//being defensive about closing all channels and streams 
				if (inChannel != null) inChannel.close();
				if (outChannel != null) outChannel.close();
				if (inStream != null) inStream.close();
				if (outStream != null) outStream.close();
			}
		}
		catch (FileNotFoundException ex){
			ex.printStackTrace();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * 스트림으로 파일복사
	 * @param aSourceFile
	 * @param aTargetFile
	 * @param aAppend
	 * @return void
	 */
	public static void copyWithStreams(File aSourceFile, File aTargetFile, boolean aAppend) {
		ensureTargetDirectoryExists(aTargetFile.getParentFile());
		InputStream inStream = null;
		OutputStream outStream = null;
		try{
			try {
				byte[] bucket = new byte[32*1024];
				inStream = new BufferedInputStream(new FileInputStream(aSourceFile));
				outStream = new BufferedOutputStream(new FileOutputStream(aTargetFile, aAppend));
				int bytesRead = 0;
				while(bytesRead != -1){
					bytesRead = inStream.read(bucket); //-1, 0, or more
					if(bytesRead > 0){
						outStream.write(bucket, 0, bytesRead);
					}
				}
			}
			finally {
				if (inStream != null) inStream.close();
				if (outStream != null) outStream.close();
			}
		}
		catch (FileNotFoundException ex){
			ex.printStackTrace();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * 목적지 디렉토리 생성
	 * @param aTargetDir
	 * @return void
	 */
	public static void ensureTargetDirectoryExists(File aTargetDir){
		if(!aTargetDir.exists()){
			aTargetDir.mkdirs();
		}
	}
	
}