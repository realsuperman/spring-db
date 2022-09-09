package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedTest {
    static class MyUncheckedException extends RuntimeException{
        public MyUncheckedException(String message) {
            super(message);
        }
    }
    static class Repository{
        public void call(){
            throw new MyUncheckedException("ex");
        }
    }

    static class Service{
        Repository repository = new Repository();
        public void callCatch(){
            try {
                repository.call(); // 언체크 예외는 기본적으로 던짐(근데 여기서는 잡아서 처리)
            }catch(MyUncheckedException e){
                log.info("예외 처리, message={}",e.getMessage(),e);
            }
        }

        public void callThrow(){
            repository.call(); // 언체크드 예외를 잡지 않으면 자연스럽게 상위로 넘어간다
        }
    }

    @Test
    void unchecked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw(){
        Service service = new Service();
        assertThatThrownBy(()->service.callThrow()).isInstanceOf(MyUncheckedException.class);
    }
}