package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedAppTest {

    static class Controller{
        Service service = new Service();
        public void request(){
            service.logic();
        }
    }

    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic(){
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient{
        public void call(){
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository{
        public void call(){
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSqlException(e);
            }
        }
        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSqlException extends RuntimeException{
        public RuntimeSqlException(String message) {
            super(message);
        }

        public RuntimeSqlException(Throwable cause) {
            super(cause);
        }
    }

    @Test
    void unChecked(){
        Controller controller = new Controller();
        assertThatThrownBy(()->controller.request()).isInstanceOf(RuntimeSqlException.class);
    }

    @Test
    void printEx(){
        Controller contoller = new Controller();
        try{
            contoller.request();
        }catch(Exception e){
            log.info("ex",e);
        }
    }
}
