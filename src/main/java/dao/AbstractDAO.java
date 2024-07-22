package dao;

import shared.Anotations.FieldType;
import shared.Anotations.Id;
import shared.SelectConditionBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbstractDAO<T> {
    private final Connection conn;
    private final String table;

    public AbstractDAO(Connection conn, String table) {
        this.conn = conn;
        this.table = table;
    }

    public void createTableIfNotExist(Class<T> cls) {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_name = '" + table  + "' LIMIT 1;";
        try (Statement stmt = conn.createStatement()) {
            boolean isTablePresent = stmt.execute(sql);
            if (!isTablePresent) {
                createTable(cls);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void createTable(Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();

        StringBuilder sql = new StringBuilder("CREATE TABLE ").append(table);

        String types = Arrays.stream(fields).map(field -> {
            field.setAccessible(true);
            return field.getName() + " " + field.getAnnotation(FieldType.class).type();
        }).collect(Collectors.joining(",", "(", ");"));

        sql.append(types);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql.toString());
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table", e);
        }
    }

    public void dropTable() {
        try(Statement stmt = conn.createStatement()) {
            String sql = "DROP TABLE IF EXISTS " + table + ";";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error dropping table", e);
        }
    }

    public int add(T t) {
        Field[] fields = t.getClass().getDeclaredFields();

        StringBuilder names = new StringBuilder();

        String values = Arrays.stream(fields)
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .map(field -> {
            field.setAccessible(true);
            names.append(field.getName()).append(",");
            try {
                return field.getType().equals(String.class) ? "'" + field.get(t).toString() + "'"  : field.get(t).toString();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.joining(",", "(", ");"));
        names.deleteCharAt(names.length() - 1);
        String sql = "INSERT INTO " + table + "(" + names + ")" + " VALUES " + values;

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            throw new RuntimeException("No generated key found");
        } catch (SQLException e) {
            throw new RuntimeException("Error adding into table " + table , e);
        }
    }

    public void update(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        Field primaryKeyField = getPrimaryKeyField(fields);
        primaryKeyField.setAccessible(true);

        String values = Arrays.stream(fields).filter(field -> !field.isAnnotationPresent(Id.class))
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        StringBuilder result = new StringBuilder();
                        result.append(field.getName()).append("=");
                        if (field.getType().equals(String.class)) {
                            result.append("'").append(field.get(t)).append("'");
                        } else {
                            result.append(field.get(t));
                        }
                        return result.toString();
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.joining(", "));

        try (Statement stmt = conn.createStatement()) {
            StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ")
                    .append(values).append(" WHERE ")
                    .append(primaryKeyField.getName())
                    .append("=")
                    .append(primaryKeyField.get(t));
            stmt.executeUpdate(sql.toString());
        } catch (IllegalAccessException | SQLException e) {
            throw new RuntimeException("Unable to update table", e);
        }

    }

    public List<T> get(Class<T> cls, SelectConditionBuilder selectConditionBuilder) {
       List<T> result = new ArrayList<>();
       try (Statement stmt = conn.createStatement()) {
           String sql = selectConditionBuilder.build();
           ResultSet rs = stmt.executeQuery(sql);
           ResultSetMetaData md = rs.getMetaData();

           while(rs.next()) {
               T t = cls.getDeclaredConstructor().newInstance();
               for (int i = 1; i <= md.getColumnCount(); i++) {
                   String columnName = md.getColumnName(i);
                   Field field = cls.getDeclaredField(columnName);
                   field.setAccessible(true);
                   field.set(t, rs.getObject(columnName));
               }
               result.add(t);
           }
       return result;
       } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException |
                NoSuchMethodException e) {
           throw new RuntimeException(e);
       } catch (NoSuchFieldException e) {
           throw new RuntimeException(e);
       }
    }

    public void dropFlat(int id) {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM " + table +  " WHERE FlatID = " + id + ";";
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private Field getPrimaryKeyField(Field[] fields) {
        Optional<Field> primaryKeyField = Arrays.stream(fields).filter(field -> field.isAnnotationPresent(Id.class)).findFirst();
        return primaryKeyField.orElseThrow();
    }


}
