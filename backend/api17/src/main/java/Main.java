import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(solution(new String[]{"muzi", "ryan", "frodo", "neo"}, new String[]{"muzi frodo", "muzi frodo", "ryan muzi", "ryan muzi", "ryan muzi", "frodo muzi", "frodo ryan", "neo muzi"}));
    }

    public static int solution(String[] friends, String[] gifts) {
        int[][] gifted = new int[friends.length][friends.length];
        int[] index = new int[friends.length];

        int time = 0;
        int answer = 0;

        List<String> arr = new ArrayList<>(Arrays.asList(friends));

        for (int i = 0; i < gifts.length; i++ ) {
            String[] arryGifts = gifts[i].split(" ");
            String friendA = arryGifts[0];
            String friendB = arryGifts[1];

            gifted[arr.indexOf(friendA)][arr.indexOf(friendB)] += 1;

            index[arr.indexOf(friendA)] += 1;
            index[arr.indexOf(friendB)] -= 1;
        }

        for(int i=0; i<friends.length; i++){

            if(gifted[i][friends.length-i] != gifted[friends.length-i][i]) {

            }
        }

        return answer;
    }
}
