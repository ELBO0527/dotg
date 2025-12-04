package elbo.dotg.api17.dto.request.portfolio;

import elbo.dotg.api17.domain.user.User;

public record SavePortfolioRequest(long userId, long monthlySalary, long annualSlary, long monthlySaving, long period,
                                   long extra, long monthlyExpenses, long targetAmount, long currentAmount,
                                   double interestRate) {
}
