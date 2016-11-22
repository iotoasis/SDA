package com.pineone.icbms.sda.comm.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.StringTokenizer;

public class PropertiesParser implements Serializable
{
	private static final long serialVersionUID = 1L;
	Properties props = null;

    public PropertiesParser(Properties props) {
        this.props = props;
    }


    public Properties getUnderlyingProperties() {
        return props;
    }
    public String getStringProperty(String name) {
    	return props.getProperty(name);
    }

    public String getStringProperty(String name, String def) {
    	
        String val = props.getProperty(name, def);
        if (val == null) {
            return def;
        }
        
        val = val.trim();
        
        return (val.length() == 0) ? def : val;
    }

    public String[] getStringArrayProperty(String name) {
        return getStringArrayProperty(name, null);
    }

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

    public boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    public boolean getBooleanProperty(String name, boolean def) {
        String val = getStringProperty(name);
        
        return (val == null) ? def : Boolean.valueOf(val).booleanValue();
    }

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

    public char getCharProperty(String name) {
        return getCharProperty(name, '\0');
    }

    public char getCharProperty(String name, char def) {
        String param = getStringProperty(name);
        return  (param == null) ? def : param.charAt(0);
    }

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

    public int[] getIntArrayProperty(String name) throws NumberFormatException {
        return getIntArrayProperty(name, null);
    }

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

    public Properties getPropertyGroup(String prefix) {
        return getPropertyGroup(prefix, false, null);
    }

    public Properties getPropertyGroup(String prefix, boolean stripPrefix) {
        return getPropertyGroup(prefix, stripPrefix, null);
    }

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
    
    public boolean containsKey(String key) {
    	return props.containsKey(key);
    }
    
    public void put(String key, Object value) {
    	props.put(key, value);
    }
}
