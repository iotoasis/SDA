package com.pineone.icbms.sda.comm.conf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.jena.atlas.lib.StrUtils; 

import com.pineone.icbms.sda.comm.util.FileUtil;
import com.pineone.icbms.sda.comm.util.PropertiesParser;

/**
 * 설정정보 구현체
 */
public class ConfigurationImpl implements Configuration {
	private static final long serialVersionUID = 1L;
	private PropertiesParser cfg;
	private String HOME = "src/main/java";
	private String CONF_PATH = "/WEB-INF/classes/conf/system.properties";
	private String PROJECT_CONF = "conf/project.properties";
	private Map<Object, Object> conf_prefix = null;
	Properties props_ref = null;
	private Context ctx;

	public ConfigurationImpl() {
		try {
			String h = System.getProperty("HOME", "src/main/java/");
			if (h != null)
				this.HOME = h;

			String config_user = System.getProperty("CONFIG");// -DCONFIG=system.properties
			if (config_user != null)
				CONF_PATH = "conf/" + config_user;

			try {
				String abs_system_prop = new File(HOME + CONF_PATH).getAbsolutePath();
				System.setProperty("SYS_ABS_PATH", abs_system_prop);
			} finally {
				try {
					initialize(HOME + CONF_PATH);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (ConfigException e) {
			e.printStackTrace();
		}
	}

	public ConfigurationImpl(Context ctx) {
		try {
			this.ctx = ctx;
			String h = System.getProperty("HOME", "../");
			if (h != null)
				this.HOME = h;

			String config_user = System.getProperty("CONFIG");// -DCONFIG=system.properties
			if (config_user != null)
				CONF_PATH = "conf/" + config_user;
			initialize(HOME + CONF_PATH);
		} catch (ConfigException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ConfigurationImpl(String configuration) {
		this.prepare(new Properties());
		try {
			if (configuration != null) {
				CONF_PATH = configuration;
				initialize(CONF_PATH);
			} else {
				System.exit(0);
				throw new ConfigException("configuration is null.");
			}

		} catch (ConfigException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ConfigurationImpl(String host, int port) {
		try {
			this.prepare(new Properties());

		} catch (ConfigException e) {
			e.printStackTrace();
		}
	}

	public ConfigurationImpl(String host, int port, String bindName, boolean export, boolean proxy,
			String createRegistry) {
		try {
			this.prepare(new Properties());

		} catch (ConfigException e) {
			e.printStackTrace();
		}
	}

	public ConfigurationImpl(String home_path, String configuration, Context ctx) {
		try {
			this.ctx = ctx;
			System.setProperty("HOME", home_path);
			System.setProperty("CONFIG", configuration);
			this.HOME = home_path;

			String config_user = configuration;
			if (config_user != null)
				CONF_PATH = "conf/" + config_user;
			initialize(HOME + CONF_PATH);
		} catch (ConfigException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#addProjectConfiguration()
	 */
	public void addProjectConfiguration() throws ConfigException {
		this.PROJECT_CONF = getRoot() + "/conf/" + ctx.name() + "/" + PROJECT_CONF;
		Properties props = new Properties();
		InputStream is = FileUtil.fileInputStream(new File(PROJECT_CONF), "utf-8");

		try {
			if (is != null) {
				is = new BufferedInputStream(is);
			} else {
				is = new BufferedInputStream(new FileInputStream(PROJECT_CONF));
			}
			props.load(is);
		} catch (IOException ioe) {
			ConfigException initException = new ConfigException(
					"Properties file: '" + PROJECT_CONF + "' could not be read.", ioe);
			throw initException;
		}
		initialize(props, true);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#contains(java.lang.String)
	 */
	public boolean contains(String key) throws ConfigException {
		return cfg.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#getBooleanProperty(java.lang.String)
	 */
	public boolean getBooleanProperty(String key) {
		return cfg.getBooleanProperty(key);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#getBooleanProperty(java.lang.String, boolean)
	 */
	public boolean getBooleanProperty(String key, boolean base) throws ConfigException {
		return cfg.getBooleanProperty(key, base);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#getIntProperty(java.lang.String)
	 */
	public int getIntProperty(String key) {
		return cfg.getIntProperty(key);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#getIntProperty(java.lang.String, int)
	 */
	public int getIntProperty(String key, int base) throws ConfigException {
		return cfg.getIntProperty(key, base);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#getMainPath()
	 */
	public String getMainPath() {
		return HOME;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#getRoot()
	 */
	public String getRoot() {
		if (HOME.endsWith("/") || HOME.endsWith("\\")) {
			return HOME.substring(0, HOME.length() - 1);
		} else
			return HOME;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#getStringProperty(java.lang.String)
	 */
	public String getStringProperty(String key) {
		return cfg.getStringProperty(key);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#getStringProperty(java.lang.String, java.lang.String)
	 */
	public String getStringProperty(String key, String base) throws ConfigException {
		return cfg.getStringProperty(key, base);
	}

	/**
	 * 설정정보 초기화
	 * @param props
	 * @param add
	 * @throws ConfigException
	 * @return void
	 */
	public void initialize(Properties props, boolean add) throws ConfigException {
		if (conf_prefix.size() > 0) {
			Map<Object, Object> temp_prop = new HashMap<Object, Object>();
			for (Iterator<Object> it = props.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				String value = props.getProperty(key);
				value = (String) reversePrefix(value);
				temp_prop.put(key, value);
			}

			if (!add) {
				props_ref = new Properties();
			}
			props_ref.putAll(temp_prop);
		}
		prepare();
	}

	/**
	 * 초기화
	 * @param filename
	 * @throws ConfigException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return void
	 */
	public void initialize(String filename) throws ConfigException, FileNotFoundException, IOException {

		Properties props = new Properties();
		if (cfg != null) {
			props.load(new FileInputStream(new File(filename)));
			cfg = new PropertiesParser(props);
			// return;
		}
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#prepare()
	 */
	public void prepare() throws ConfigException {
		this.cfg = new PropertiesParser(props_ref);
	}

	/**
	 * 설정정보 준비
	 * @param props
	 * @throws ConfigException
	 * @return void
	 */
	public void prepare(Properties props) throws ConfigException {
		this.cfg = new PropertiesParser(props);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#putProperty(java.lang.String, java.lang.Object)
	 */
	public void putProperty(String key, Object value) {
		cfg.put(key, value);
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#removeEndPathSeperator(java.lang.String)
	 */
	public String removeEndPathSeperator(String path) {
		if (path.endsWith("/") || path.endsWith("\\")) {
			return path.substring(0, path.length() - 1);
		} else
			return path;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#reversePrefix(java.lang.String)
	 */
	public Object reversePrefix(String str) {
		for (Iterator<Object> it = conf_prefix.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			if (str.indexOf(key) > -1)
				return StrUtils.replace(str, key, conf_prefix.get(key).toString());
		}

		return str;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#reverseRoot(java.lang.String)
	 */
	public String reverseRoot(String path) {
		if (path != null && path.indexOf("{ROOT}") > -1) {
			return StrUtils.replace(path, "{ROOT}", getRoot());
		}

		return path;
	}

	/* (non-Javadoc)
	 * @see com.pineone.icbms.sda.comm.conf.Configuration#setMainPath(java.lang.String)
	 */
	public void setMainPath(String path) {
		System.setProperty("HOME", path);
		this.HOME = path;
	}
}
