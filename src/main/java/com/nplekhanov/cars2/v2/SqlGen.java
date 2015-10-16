package com.nplekhanov.cars2.v2;

import com.nplekhanov.cars2.v2.moto.Offer;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author nplekhanov
 */
public class SqlGen {
    public static void main(String[] args) {
        generate(Offer.class);
    }

    public static void generate(Class<?> aClass) {
        List<String> columnDefs = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        String batchSetter = "";
        for (Class c = aClass; c != Object.class; c = c.getSuperclass()) {
            for (Field field: c.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                columnDefs.add(toDbName(field.getName())+" "+toDbType(field.getType()));
                columnNames.add(toDbName(field.getName()));

                batchSetter += "ps.set" + StringUtils.capitalize(field.getType().getSimpleName()) + "(++n, offer.get" + StringUtils.capitalize(field.getName()) + "());\n";
            }
        }
        String table = toDbName(aClass.getSimpleName());
        String ddl = "create table "+ table +" (" + StringUtils.join(columnDefs, ", ")+ ")";
        System.out.println(ddl);
        String insert = "insert into " + table +" ("+StringUtils.join(columnNames, ", ")+") values (?"+StringUtils.repeat(", ?", columnNames.size() - 1)+")";
        System.out.println(insert);
        System.out.println(batchSetter);
    }

    public static String generate(Object o, String...ignoreFields) {
        List<String> columnNames = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (Class c = o.getClass(); c != Object.class; c = c.getSuperclass()) {
            for (Field field: c.getDeclaredFields()) {
                if (Arrays.asList(ignoreFields).contains(field.getName())) {
                    continue;
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                columnNames.add(toDbName(field.getName()));

                Object value;
                try {
                    field.setAccessible(true);
                    value = field.get(o);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
                String s;
                if (value instanceof String) {
                    s = "'"+value+"'";
                } else {
                    s = String.valueOf(value);
                }
                values.add(s);
            }
        }
        String table = toDbName(o.getClass().getSimpleName());
        return  "insert into " + table +" ("+StringUtils.join(columnNames, ", ")+") values ("+StringUtils.join(values, ", ")+")";
    }

    public static String toDbType(Class javaType) {
        if (javaType.isPrimitive() && javaType != char.class && javaType != boolean.class) {
            return "number";
        }
        if (Number.class.isAssignableFrom(javaType)) {
            return "number";
        }
        if (String.class == javaType) {
            return "varchar(500)";
        }
        if (boolean.class == javaType) {
            return "char(1)";
        }
        throw new IllegalArgumentException(""+javaType);
    }

    public static String toDbName(String javaName) {
        String s = "";
        for (int i = 0; i < javaName.length(); i ++) {
            char c = javaName.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                s += "_";
                c = Character.toLowerCase(c);
            }
            s += c;
        }
        return s;
    }
}
