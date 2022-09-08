package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class CheckedTest {
    static class MyCheckedException extends Exception{ // Exception을 상속받은 예외는 체크 예외가 된다
        public MyCheckedException(String message) {
            super(message);
        }
    }

    @Test
    void checked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void chcked_throw(){
        Service service = new Service();
        assertThatThrownBy(()->service.callThrow()).isInstanceOf(MyCheckedException.class);
    }

    static class Service{
        Repository repository = new Repository();

        public void callCatch(){
            try {
                repository.call();
            } catch (MyCheckedException e) {
                log.info("예외 처리, message={}",e);
            }
        }

        public void callThrow() throws MyCheckedException {
            repository.call();
        }
    }

    static class Repository{
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
