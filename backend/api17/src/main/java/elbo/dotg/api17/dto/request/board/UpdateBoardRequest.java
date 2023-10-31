package elbo.dotg.api17.dto.request.board;

import elbo.dotg.api17.domain.user.User;

public record UpdateBoardRequest(String title, String Content, long userId, long categoryId) {
}
