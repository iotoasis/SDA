package com.pineone.icbms.sda.comm.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * 설정값 Parser
 */
public class PropertiesParser implements Serializable
{
	private static final long serialVersionUID = 1L;
	Properties props = null;

    public PropertiesParser(Properties props) {
        this.props = props;
    }

    /**
     * 근본속성 가져오기
     * @return Properties
     */
    public Properties getUnderlyingProperties() {
        return props;
    }
    
    /**
     * 속성값 가져오기
     * @param name
     * @return String
     */
    public String getStringProperty(String name) {
    	return props.getProperty(name);
    }

    /**
     * 속성값 가져오기
     * @param name
     * @param def
     * @return String
     */
    public String getStringProperty(String name, String def) {
        String val = props.getProperty(name, def);
        if (val == null) {
            return def;
        }
        
        val = val.trim();
        
        return (val.length() == 0) ? def : val;
    }

    /**
     * 여러 속성값을 가져오기
     * @param name
     * @return String[]
     */
    public String[] getStringArrayProperty(String name) {
        return getStringArrayProperty(name, null);
    }

    /**
     * 여러 속성값을 가져오기
     * @param name
     * @param def
     * @return String[]
     */
    public String[] getStringArrayProperty(String name, String[] def) {
        String vals = getStringProperty(name);
        if (vals == null) {
            return def;
        }

        StringTokenizer stok = new StringTokenizer(vals, ",");
        ArrayList strs = new ArrayList();
        try {
            while (stok.hasMoreTokens()) {
                strs.add(stok.nextToken().trim());
            }
            
            return (String[])strs.toArray(new String[strs.size()]);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * boolean형 속성값 
     * @param name
     * @return boolean
     */
    public boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    /**
     * boolean형 속성값
     * @param name
     * @param def
     * @return
     * @return boolean
     */
    public boolean getBooleanProperty(String name, boolean def) {
        String val = getStringProperty(name);
        
        return (val == null) ? def : Boolean.valueOf(val).booleanValue();
    }

    /**
     * Byte형 속성값
     * @param name
     * @throws NumberFormatException
     * @return byte
     */
    public byte getByteProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Byte.parseByte(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * byte형 속성값
     * @param name
     * @param def
     * @throws NumberFormatException
     * @return byte
     */
    public byte getByteProperty(String name, byte def)
        throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Byte.parseByte(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Char형 속성값
     * @param name
     * @return char
     */
    public char getCharProperty(String name) {
        return getCharProperty(name, '\0');
    }

    /**
     * Char형 속성값
     * @param name
     * @param def
     * @return char
     */
    public char getCharProperty(String name, char def) {
        String param = getStringProperty(name);
        return  (param == null) ? def : param.charAt(0);
    }

    /**
     * Double형 속성값
     * @param name
     * @throws NumberFormatException
     * @return double
     */
    public double getDoubleProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Double형 속성값
     * @param name
     * @param def
     * @throws NumberFormatException
     * @return double
     */
    public double getDoubleProperty(String name, double def)
        throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Float형 속성값
     * @param name
     * @throws NumberFormatException
     * @return float
     */
    public float getFloatProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Float형 속성값
     * @param name
     * @param def
     * @throws NumberFormatException
     * @return float
     */
    public float getFloatProperty(String name, float def)
        throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Int형 속성값
     * @param name
     * @throws NumberFormatException
     * @return int
     */
    public int getIntProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Int형 속성값
     * @param name
     * @param def
     * @throws NumberFormatException
     * @return int
     */
    public int getIntProperty(String name, int def)
        throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Int 배열형 속성값
     * @param name
     * @throws NumberFormatException
     * @return int[]
     */
    public int[] getIntArrayProperty(String name) throws NumberFormatException {
        return getIntArrayProperty(name, null);
    }

    /**
     * Int 배열형 속성값 
     * @param name
     * @param def
     * @throws NumberFormatException
     * @return int[]
     */
    public int[] getIntArrayProperty(String name, int[] def)
        throws NumberFormatException {
        String vals = getStringProperty(name);
        if (vals == null) {
            return def;
        }

        StringTokenizer stok = new StringTokenizer(vals, ",");
        ArrayList ints = new ArrayList();
        try {
            while (stok.hasMoreTokens()) {
                try {
                    ints.add(new Integer(stok.nextToken().trim()));
                } catch (NumberFormatException nfe) {
                    throw new NumberFormatException(" '" + vals + "'");
                }
            }
                        
            int[] outInts = new int[ints.size()];
            for (int i = 0; i < ints.size(); i++) {
                outInts[i] = ((Integer)ints.get(i)).intValue();
            }
            return outInts;
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Long형 속성값
     * @param name
     * @throws NumberFormatException
     * @return long
     */
    public long getLongProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Long.parseLong(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Long형 속성값
     * @param name
     * @param def
     * @throws NumberFormatException
     * @return long
     */
    public long getLongProperty(String name, long def)
        throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Long.parseLong(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Short형 속성값
     * @param name
     * @throws NumberFormatException
     * @return short
     */
    public short getShortProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            throw new NumberFormatException(" null string");
        }

        try {
            return Short.parseShort(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * Shot형 속성값
     * @param name
     * @param def
     * @throws NumberFormatException
     * @return short
     */
    public short getShortProperty(String name, short def)
        throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) {
            return def;
        }

        try {
            return Short.parseShort(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    /**
     * 속성그룹
     * @param prefix
     * @return String[]
     */
    public String[] getPropertyGroups(String prefix) {
        Enumeration keys = props.propertyNames();
        HashSet groups = new HashSet(10);

        if (!prefix.endsWith(".")) {
            prefix += ".";
        }

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith(prefix)) {
                String groupName = key.substring(prefix.length(), key.indexOf(
                        '.', prefix.length()));
                groups.add(groupName);
            }
        }

        return (String[]) groups.toArray(new String[groups.size()]);
    }

    /**
     * 속성 그룹
     * @param prefix
     * @return Properties
     */
    public Properties getPropertyGroup(String prefix) {
        return getPropertyGroup(prefix, false, null);
    }

    /**
     * 속성그룹
     * @param prefix
     * @param stripPrefix
     * @return Properties
     */
    public Properties getPropertyGroup(String prefix, boolean stripPrefix) {
        return getPropertyGroup(prefix, stripPrefix, null);
    }

    /**
     * 속성그룹
     * @param prefix
     * @param stripPrefix
     * @param excludedPrefixes
     * @return Properties
     */
    public Properties getPropertyGroup(String prefix, boolean stripPrefix, String[] excludedPrefixes) {
        Enumeration keys = props.propertyNames();
        Properties group = new Properties();

        if (!prefix.endsWith(".")) {
            prefix += ".";
        }

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith(prefix)) {
                
                boolean exclude = false;
                if (excludedPrefixes != null) {
                    for (int i = 0; (i < excludedPrefixes.length) && (exclude == false); i++) {
                        exclude = key.startsWith(excludedPrefixes[i]);
                    }
                }

                if (exclude == false) {
                    String value = getStringProperty(key, "");
                    
                    if (stripPrefix) { 
                        group.put(key.substring(prefix.length()), value);
                    } else {
                        group.put(key, value);
                    }
                }
            }
        }

        return group;
    }
    
    /**
     * 키 포함 확인
     * @param key
     * @return boolean
     */
    public boolean containsKey(String key) {
    	return props.containsKey(key);
    }
    
    /**
     * 속성 등록
     * @param key
     * @param value
     * @return void
     */
    public void put(String key, Object value) {
    	props.put(key, value);
    }
}
