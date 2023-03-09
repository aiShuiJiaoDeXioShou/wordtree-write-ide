package lh.wordtree.utils;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import lh.wordtree.comm.config.Config;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public abstract class DbUtils {
    private static final SQLiteDataSource dataSource;
    private static final Db db;


    // myBatis 进行实例化操作
    static {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(Config.SQLITE_JDBC_CONFIG_PATH);
        db = Db.use(dataSource);
    }

    /**
     * 获取数据源DataSource对象
     *
     * @return
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    public static Db db() {
        return db;
    }

    public static <T> List<T> paddingAll(List<Entity> entities, Class<T> clazz) {
        return entities.stream().map(entity -> {
            try {
                var instance = clazz.getDeclaredConstructor().newInstance();
                var declaredFields = clazz.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    if (isBool(entity.get(declaredField.getName())) != null) {
                        declaredField.set(instance, isBool(entity.get(declaredField.getName())));
                    } else {
                        declaredField.set(instance, entity.get(declaredField.getName()));
                    }
                }
                return instance;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).toList();
    }

    public static <T> List<T> paddingSetAll(List<Entity> entities, Class<T> clazz) {
        return entities.stream().map(entity -> {
            try {
                var instance = clazz.getDeclaredConstructor().newInstance();
                var declaredMethods = clazz.getDeclaredMethods();
                var fieldNames = entity.getFieldNames().stream().toList();
                for (String fieldName : fieldNames) {
                    var set = Arrays.stream(declaredMethods)
                            .filter(method -> method.getName().toLowerCase().contains(("set" + fieldName).toLowerCase()))
                            .toList();
                    if (set.size() > 0) {
                        var method = set.get(0);
                        method.setAccessible(true);
                        method.invoke(instance, entity.get(fieldName));
                    } else {
                        var field = clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        if (isBool(entity.get(fieldName)) != null) {
                            field.set(instance, isBool(entity.get(fieldName)));
                        } else {
                            field.set(instance, entity.get(fieldName));
                        }
                    }
                }
                return instance;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).toList();
    }

    private static Object isBool(Object obj) {
        if (obj instanceof String bool) {
            if (bool.equals("true")) {
                return true;
            } else if (bool.equals("false")) {
                return false;
            }
        }
        return null;
    }

    public static <T> T padding(Entity entity, Class<T> clazz) {
        try {
            var instance = clazz.getDeclaredConstructor().newInstance();
            var declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (isBool(entity.get(declaredField.getName())) != null) {
                    declaredField.set(instance, isBool(entity.get(declaredField.getName())));
                } else {
                    declaredField.set(instance, entity.get(declaredField.getName()));
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库连接Connection对象
     *
     * @return Connection对象
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return connection;
        }
        return connection;
    }

    /**
     * 将数据库连接对象归还到数据库连接池中
     *
     * @param connection Connection-数据库连接对象
     */
    public static void returnResources(Connection connection) {
        returnResources(null, null, connection);
    }

    /**
     * 将数据库连接对象归还到数据库连接池中
     *
     * @param statement  Statement-SQL语句执行器
     * @param connection Connection-数据库连接对象
     */
    public static void returnResources(Statement statement, Connection connection) {
        returnResources(null, statement, connection);
    }

    /**
     * 将数据库连接对象归还到数据库连接池中
     *
     * @param resultSet  ResultSet-结果集
     * @param statement  Statement-SQL语句执行器
     * @param connection Connection-数据库连接对象
     */
    public static void returnResources(ResultSet resultSet, Statement statement, Connection connection) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
