package elbo.dotg.api17;

import elbo.dotg.api17.dto.response.common.ApiResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
            2    1 ㅁㅅㅁ 메소드명[] {
           5     2     ㅅㅁㅅ ㅁㄴㅇㄹ
                3 }
    7    6        4 ㅁㅅㅁ 메소드명1[ㅇㅈㅇ 매개변수] {
                5     ㅅㅁㅅ ㅁㄴㅇㄹ
 8               6 }
                """;
        String t1 = "";
        long tt1 = Long.parseLong(null);
        System.out.println(test1);
    }

    @Test
    void isNull() throws Exception {
        //given

        //when

        //then
    }
}
