package elbo.dotg.api17.dto.request.board;

import java.util.List;

public record UpdateBoardRequest(String title, String Content, long categoryId, List<String> attachments) {
}
