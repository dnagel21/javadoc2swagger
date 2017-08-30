package com.spirit21.swagger.converter.writers;

import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.json.JSONObject;

import com.spirit21.swagger.converter.models.Property;
import com.spirit21.swagger.converter.parsers.ParameterParser;

/**
 * 
 * @author dsimon
 *
 */
public class PropertyWriter extends AbstractWriter {
    public PropertyWriter(Log log) {
        super(log);
    }

    /**
     * Creates JSON for properties
     * 
     * @param properties
     *            list of {@link Property} objects
     * @return JSON for properties
     */
        public JSONObject mapProperties(List<Property> properties) {
    	String patternstring = ParameterParser.patternstring;
    	JSONObject obj = new JSONObject();
    	for (Property property : properties) {
    		JSONObject prop = new JSONObject();
    		String type = property.getType();
    		String schema = property.getReference();
    		String format = property.getFormat();
    		String genericType = property.getGenericType();
    		String genericFormat = property.getGenericFormat();
    		if (type != null && schema == null && genericType == null) {
    			prop.put("type", type);
    			if (format != null) {
    				prop.put("format", format);             
    			}
    		} else if (type == null && schema != null && schema.startsWith("#")) {
    			prop.put("$ref", schema);
    		} else if (type == "array" && schema != null && schema.startsWith("#")) {
    			prop.put("type", type);
    			prop.put("items", new JSONObject().put("$ref", schema));
    		} else if (type == "array" && genericType != null) {
    			prop.put("type", type);
    			JSONObject items = new JSONObject();
    			items.put("type", genericType);
    			if (genericFormat != null) {
    				items.put("format", genericFormat);
    			}
    			prop.put("items", items);
    		}
    		obj.put(property.getName(), prop);
    		for(int i = 0; i < obj.names().length(); i++) {
    			if(property.getName().equals("name") && obj.names().getString(i).contains("name") && type.contains("string")) {
    				prop.put("pattern", patternstring );
    			}
    		}
    	}
    	return obj;
    }

}
