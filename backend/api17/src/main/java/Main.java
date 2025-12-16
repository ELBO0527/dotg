import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (Client client = new Client()) {
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseModalities("TEXT", "IMAGE")
                    .build();

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash-image",
                    "포르쉐를 탄 원숭이",
                    config);

            for (Part part : response.parts()) {
                if (part.text().isPresent()) {
                    System.out.println(part.text().get());
                } else if (part.inlineData().isPresent()) {
                    var blob = part.inlineData().get();
                    if (blob.data().isPresent()) {
                        Files.write(Paths.get("_01_generated_image.png"), blob.data().get());
                    }
                }
            }
        }
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
