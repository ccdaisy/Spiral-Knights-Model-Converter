package xan_code.dathandler;

import java.io.IOException;

import java.lang.reflect.Field;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

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
        FieldMarshaller marshaller = MARSHALLERS.get(field.getType());
        if (marshaller != null) {
            return marshaller;
        }

        // otherwise, just return the generic one
        return MARSHALLERS.get(Object.class);
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

    /** Field marshallers mapped by class. */
    protected static final Map<Class<?>, FieldMarshaller> MARSHALLERS =
        ImmutableMap.<Class<?>, FieldMarshaller>builder()
            .put(Boolean.TYPE, new FieldMarshaller() {
                public void readField (
                    Field field, String name, Object target, Object prototype, Importer importer)
                        throws IOException, IllegalAccessException {
                    field.setBoolean(target, importer.read(name, field.getBoolean(prototype)));
                }
                public void writeField (
                    Field field, String name, Object source, Object prototype, Exporter exporter)
                        throws IOException, IllegalAccessException {
                    exporter.write(name, field.getBoolean(source), field.getBoolean(prototype));
                }
            })
            .put(Byte.TYPE, new FieldMarshaller() {
                public void readField (
                    Field field, String name, Object target, Object prototype, Importer importer)
                        throws IOException, IllegalAccessException {
                    field.setByte(target, importer.read(name, field.getByte(prototype)));
                }
                public void writeField (
                    Field field, String name, Object source, Object prototype, Exporter exporter)
                        throws IOException, IllegalAccessException {
                    exporter.write(name, field.getByte(source), field.getByte(prototype));
                }
            })
            .put(Character.TYPE, new FieldMarshaller() {
                public void readField (
                    Field field, String name, Object target, Object prototype, Importer importer)
                        throws IOException, IllegalAccessException {
                    field.setChar(target, importer.read(name, field.getChar(prototype)));
                }
                public void writeField (
                    Field field, String name, Object source, Object prototype, Exporter exporter)
                        throws IOException, IllegalAccessException {
                    exporter.write(name, field.getChar(source), field.getChar(prototype));
                }
            })
            .put(Double.TYPE, new FieldMarshaller() {
                public void readField (
                    Field field, String name, Object target, Object prototype, Importer importer)
                        throws IOException, IllegalAccessException {
                    field.setDouble(target, importer.read(name, field.getDouble(prototype)));
                }
                public void writeField (
                    Field field, String name, Object source, Object prototype, Exporter exporter)
                        throws IOException, IllegalAccessException {
                    exporter.write(name, field.getDouble(source), field.getDouble(prototype));
                }
            })
            .put(Float.TYPE, new FieldMarshaller() {
                public void readField (
                    Field field, String name, Object target, Object prototype, Importer importer)
                        throws IOException, IllegalAccessException {
                    field.setFloat(target, importer.read(name, field.getFloat(prototype)));
                }
                public void writeField (
                    Field field, String name, Object source, Object prototype, Exporter exporter)
                        throws IOException, IllegalAccessException {
                    exporter.write(name, field.getFloat(source), field.getFloat(prototype));
                }
            })
            .put(Integer.TYPE, new FieldMarshaller() {
                public void readField (
                    Field field, String name, Object target, Object prototype, Importer importer)
                        throws IOException, IllegalAccessException {
                    field.setInt(target, importer.read(name, field.getInt(prototype)));
                }
                public void writeField (
                    Field field, String name, Object source, Object prototype, Exporter exporter)
                        throws IOException, IllegalAccessException {
                    exporter.write(name, field.getInt(source), field.getInt(prototype));
                }
            })
            .put(Long.TYPE, new FieldMarshaller() {
                public void readField (
                    Field field, String name, Object target, Object prototype, Importer importer)
                        throws IOException, IllegalAccessException {
                    field.setLong(target, importer.read(name, field.getLong(prototype)));
                }
                public void writeField (
                    Field field, String name, Object source, Object prototype, Exporter exporter)
                        throws IOException, IllegalAccessException {
                    exporter.write(name, field.getLong(source), field.getLong(prototype));
                }
            })
            .put(Short.TYPE, new FieldMarshaller() {
                public void readField (
                    Field field, String name, Object target, Object prototype, Importer importer)
                        throws IOException, IllegalAccessException {
                    field.setShort(target, importer.read(name, field.getShort(prototype)));
                }
                public void writeField (
                    Field field, String name, Object source, Object prototype, Exporter exporter)
                        throws IOException, IllegalAccessException {
                    exporter.write(name, field.getShort(source), field.getShort(prototype));
                }
            })
            .put(Object.class, new FieldMarshaller() {
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
                public void writeField (
                    Field field, String name, Object source, Object prototype, Exporter exporter)
                        throws IOException, IllegalAccessException {
                    @SuppressWarnings("unchecked") Class<Object> clazz =
                        (Class<Object>)field.getType();
                    exporter.write(name, field.get(source), field.get(prototype), clazz);
                }
            })
            .build();
}