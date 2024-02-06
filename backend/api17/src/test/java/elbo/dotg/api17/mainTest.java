package elbo.dotg.api17;

import elbo.dotg.api17.dto.response.common.ApiResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class mainTest {

    @Getter
    public class ATest {
        private Double sutja;
        private float floato;
    }
    @Test
    void main(){
        String test1 = """
                1 ㅁㅅㅁ 메소드명[] {
                2     ㅅㅁㅅ ㅁㄴㅇㄹ
                3 }
                4 ㅁㅅㅁ 메소드명1[ㅇㅈㅇ 매개변수] {
                5     ㅅㅁㅅ ㅁㄴㅇㄹ
                6 }
                """;

        System.out.println(test1);
    }
}
