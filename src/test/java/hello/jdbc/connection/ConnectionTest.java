package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {
    @Test
    void driverManager() throws SQLException {
        Connection conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        Connection conn2 = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        log.info("connection={}, close={}",conn,conn.getClass());
        log.info("connection={}, close={}",conn2,conn2.getClass());
    }
    @Test
    void dataSourceDriverManager() throws SQLException {
        // DriverManagerDataSource - 항상 새로운 커넥션을 획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        // 커넥션 풀링(히카리)
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10); // 풀 사이즈 지정
        dataSource.setPoolName("MyPool");
        // 이 위의 코드들은 데이터소스 설정 부분이다
        useDataSource(dataSource); // 데이터소스 사용
        Thread.sleep(1000);
    }

    private void useDataSource(DataSource dataSource) throws SQLException { // 설정과 사용의 분리
        Connection conn = dataSource.getConnection();
        Connection conn2 = dataSource.getConnection();
        Connection conn3 = dataSource.getConnection();
        Connection conn4 = dataSource.getConnection();
        Connection conn5 = dataSource.getConnection();
        Connection conn6 = dataSource.getConnection();
        Connection conn7 = dataSource.getConnection();
        Connection conn8 = dataSource.getConnection();
        Connection conn9 = dataSource.getConnection();
        Connection conn10 = dataSource.getConnection();

        log.info("connection={}, close={}",conn,conn.getClass());
        log.info("connection={}, close={}",conn2,conn2.getClass());
    }
}
