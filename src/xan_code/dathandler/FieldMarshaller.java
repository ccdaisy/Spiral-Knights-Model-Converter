package xan_code.dathandler;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Used to read and write individual fields.
 */
public abstract class FieldMarshaller
{
    /**
     * Retrieves a field marshaller for the specified field.
     */
    public static FieldMarshaller getFieldMarshaller (Field field)
    {
        // check for a type-specific marshaller
        FieldMarshaller marshaller = getType(field.getType().toString());
        if (marshaller != null) {
            return marshaller;
        }

        // otherwise, just return the generic one
        return null;
    }
    
    public static FieldMarshaller getType(String typeName) {
    	if (typeName == "boolean") {
    		return new FieldMarshaller() {
                public void readField (Field field, String name, Object target, Object prototype, Importer importer) throws IOException, IllegalAccessException {
                    field.setBoolean(target, importer.read(name, field.getBoolean(prototype)));
                }

				@Override
				public void writeField(Field field, String name, Object source,
						Object prototype, Exporter exporter)
						throws IOException, IllegalAccessException {
					// TODO Auto-generated method stub
					
				}
    		};
    	} else if (typeName.toLowerCase() == "byte") {
    		return new FieldMarshaller() {
    			 public void readField (
    	                    Field field, String name, Object target, Object prototype, Importer importer)
    	                        throws IOException, IllegalAccessException {
    	                    field.setByte(target, importer.read(name, field.getByte(prototype)));
    	                }

				@Override
				public void writeField(Field field, String name, Object source,
						Object prototype, Exporter exporter)
						throws IOException, IllegalAccessException {
					// TODO Auto-generated method stub
					
				}
    		};
    	} else if (typeName.toLowerCase() == "char") {
    		return new FieldMarshaller() {
    			public void readField (
                        Field field, String name, Object target, Object prototype, Importer importer)
                            throws IOException, IllegalAccessException {
                        field.setChar(target, importer.read(name, field.getChar(prototype)));
                    }

				@Override
				public void writeField(Field field, String name, Object source,
						Object prototype, Exporter exporter)
						throws IOException, IllegalAccessException {
					// TODO Auto-generated method stub
					
				}
   		};
    	} else if (typeName.toLowerCase() == "double") {
    		return new FieldMarshaller() {
    			public void readField (
                        Field field, String name, Object target, Object prototype, Importer importer)
                            throws IOException, IllegalAccessException {
                        field.setDouble(target, importer.read(name, field.getDouble(prototype)));
                    }

				@Override
				public void writeField(Field field, String name, Object source,
						Object prototype, Exporter exporter)
						throws IOException, IllegalAccessException {
					// TODO Auto-generated method stub
					
				}
   		};
    	} else if (typeName.toLowerCase() == "float") {
    		return new FieldMarshaller() {
    			public void readField (
                        Field field, String name, Object target, Object prototype, Importer importer)
                            throws IOException, IllegalAccessException {
                        field.setFloat(target, importer.read(name, field.getFloat(prototype)));
                    }

				@Override
				public void writeField(Field field, String name, Object source,
						Object prototype, Exporter exporter)
						throws IOException, IllegalAccessException {
					// TODO Auto-generated method stub
					
				}
   		};
    	} else if (typeName.toLowerCase() == "integer") {
    		return new FieldMarshaller() {
    			 public void readField (
    	                    Field field, String name, Object target, Object prototype, Importer importer)
    	                        throws IOException, IllegalAccessException {
    	                    field.setInt(target, importer.read(name, field.getInt(prototype)));
    	                }

				@Override
				public void writeField(Field field, String name, Object source,
						Object prototype, Exporter exporter)
						throws IOException, IllegalAccessException {
					// TODO Auto-generated method stub
					
				}
   		};
    	} else if (typeName.toLowerCase() == "long") {
    		return new FieldMarshaller() {
    			public void readField (
                        Field field, String name, Object target, Object prototype, Importer importer)
                            throws IOException, IllegalAccessException {
                        field.setLong(target, importer.read(name, field.getLong(prototype)));
                    }

				@Override
				public void writeField(Field field, String name, Object source,
						Object prototype, Exporter exporter)
						throws IOException, IllegalAccessException {
					// TODO Auto-generated method stub
					
				}
   		};
    	} else if (typeName.toLowerCase() == "short") {
    		return new FieldMarshaller() {
    			public void readField (
                        Field field, String name, Object target, Object prototype, Importer importer)
                            throws IOException, IllegalAccessException {
                        field.setShort(target, importer.read(name, field.getShort(prototype)));
                    }

				@Override
				public void writeField(Field field, String name, Object source,
						Object prototype, Exporter exporter)
						throws IOException, IllegalAccessException {
					// TODO Auto-generated method stub
					
				}
   		};
    	} else if (typeName.toLowerCase() == "object") {
    		return new FieldMarshaller() {
    			public void readField (
                        Field field, String name, Object target, Object prototype, Importer importer)
                            throws IOException, IllegalAccessException {
                        // only set the field if it's present; otherwise, we would have to clone the
                        // value of the prototype field
                        Object defvalue = field.get(prototype);
                        Object value = importer.read(name, defvalue, field);
                        if (value != defvalue) {
                            field.set(target, value);
                        }
                    }

				@Override
				public void writeField(Field field, String name, Object source,
						Object prototype, Exporter exporter)
						throws IOException, IllegalAccessException {
					// TODO Auto-generated method stub
					
				}
   		};
    	} else {
    		return null;
    	}
    }

    /**
     * Reads the contents of the supplied field from the supplied importer and sets it in the
     * object.
     */
    public abstract void readField (
        Field field, String name, Object target, Object prototype, Importer importer)
        throws IOException, IllegalAccessException;

    /**
     * Writes the contents of the supplied field in the supplied object to the exporter.
     */
    public abstract void writeField (
        Field field, String name, Object source, Object prototype, Exporter exporter)
        throws IOException, IllegalAccessException;
}