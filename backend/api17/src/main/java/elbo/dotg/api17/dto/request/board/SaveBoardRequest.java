package elbo.dotg.api17.dto.request.board;

import java.util.List;

public record SaveBoardRequest(String title, String content, long categoryId, List<String> attachments) {
}
