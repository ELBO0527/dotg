package elbo.dotg.api17;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

class mainTest {

    @Builder
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
