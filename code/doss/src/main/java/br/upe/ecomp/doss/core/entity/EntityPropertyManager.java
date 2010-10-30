package br.upe.ecomp.doss.core.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.upe.ecomp.doss.core.annotation.Parameter;

/**
 * .
 * 
 * @author Rodrigo Castro
 */
public final class EntityPropertyManager {

    private EntityPropertyManager() {
    }

    /**
     * Returns the current value of the field specified by <code>paramName</code> of the object
     * informed.
     * 
     * @param entity The object from which one want to know the value of the specified field.
     * @param paramName The name of the field or the value of the name property of the
     *            <code>Parameter</code> annotation.
     * @return The current value of the field specified by <code>paramName</code> of the object
     *         informed.
     */
    public static Object getValue(Object entity, String paramName) {
        Object value = null;
        Field field = getField(entity, paramName);
        if (field != null) {
            field.setAccessible(true);
            try {
                value = field.get(entity);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return value;
    }

    /**
     * Returns the current value of the field specified by <code>paramName</code> of the object
     * informed converted to <code>String</code>.
     * 
     * @param entity The object from which one want to know the value of the specified field.
     * @param paramName The name of the field or the value of the name property of the
     *            <code>Parameter</code> annotation.
     * @return The current value of the field specified by <code>paramName</code> of the object
     *         informed converted to <code>String</code>
     */
    public static String getValueAsString(Object entity, String paramName) {
        String value = null;
        Object rawValue = getValue(entity, paramName);
        if (rawValue != null) {
            value = String.valueOf(rawValue);
        }
        return value;
    }

    /**
     * Sets the value of the field specified by <code>paramName</code> of the object
     * informed.
     * 
     * @param entity The object which one want to set the value of the specified field.
     * @param paramName The name of the field or the value of the name property of the
     *            <code>Parameter</code> annotation.
     * @param value The new value of the specified field.
     */
    public static void setValue(Object entity, String paramName, String value) {
        Field field = getField(entity, paramName);

        if (field != null) {
            field.setAccessible(true);
            try {
                if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    field.setInt(entity, Integer.parseInt(value));
                } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                    field.setDouble(entity, Double.parseDouble(value));
                } else if (field.getType().equals(String.class)) {
                    field.set(entity, value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a list with the name of all fields of the object informed that have the
     * <code>Parameter</code> annotation.
     * 
     * @param entity The object that will be inspected.
     * @return A list with the name of all fields that have the <code>Parameter</code> annotation.
     */
    public static List<String> getFieldsName(Object entity) {
        Set<String> fieldsName = new HashSet<String>();
        Class<?> clazz = entity.getClass();

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Parameter.class)) {
                    fieldsName.add(field.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }
        return new ArrayList<String>(fieldsName);
    }

    /**
     * Returns a list with the value of the name property of the <code>Parameter</code> annotation
     * of all fields of the object informed that have the <code>Parameter</code> annotation.
     * 
     * @param entity The object that will be inspected.
     * @return A list with the value of the name property of the <code>Parameter</code> annotation
     *         of all fields of the object informed that have the <code>Parameter</code> annotation.
     */
    public static List<String> getParametersNameFromFields(Object entity) {
        Set<String> parametersName = new HashSet<String>();
        Class<?> clazz = entity.getClass();

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Parameter.class)) {
                    parametersName.add(field.getAnnotation(Parameter.class).name());
                }
            }
            clazz = clazz.getSuperclass();
        }
        return new ArrayList<String>(parametersName);
    }

    private static Field getField(Object entity, String paramName) {
        Field field = null;
        Class<?> clazz = entity.getClass();

        while (clazz != null && field == null) {
            field = getFieldWithName(clazz, paramName);
            if (field == null) {
                field = getFieldWithParameterName(clazz, paramName);
            }
            clazz = clazz.getSuperclass();
        }
        return field;
    }

    private static Field getFieldWithName(Class<?> clazz, String paramName) {
        Field field = null;
        for (Field currentField : clazz.getDeclaredFields()) {
            if (currentField.isAnnotationPresent(Parameter.class) && currentField.getName().equals(paramName)) {
                field = currentField;
                break;
            }
        }
        return field;
    }

    private static Field getFieldWithParameterName(Class<?> clazz, String paramName) {
        Field field = null;
        for (Field currentField : clazz.getDeclaredFields()) {
            if (currentField.isAnnotationPresent(Parameter.class)
                    && currentField.getAnnotation(Parameter.class).name().equals(paramName)) {
                field = currentField;
                break;
            }
        }
        return field;
    }
}
