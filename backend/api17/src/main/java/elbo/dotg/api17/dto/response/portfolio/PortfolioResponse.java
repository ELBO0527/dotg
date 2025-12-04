package elbo.dotg.api17.dto.response.portfolio;

import elbo.dotg.api17.domain.portfolio.Portfolio;
import elbo.dotg.api17.domain.user.User;

public record PortfolioResponse(Long id, long userId, long monthlySalary, long annualSalary, long monthlySaving,
                                long period, long extra, long monthlyExpenses, long targetAmount, long currentAmount,
                                double interestRate) {
    public static PortfolioResponse from(final Portfolio portfolio) {
        return new PortfolioResponse(portfolio.getId()
                , portfolio.getUser().getId()
                , portfolio.getMonthlySalary()
                , portfolio.getAnnualSalary()
                , portfolio.getMonthlySaving()
                , portfolio.getPeriod()
                , portfolio.getExtra()
                , portfolio.getMonthlyExpenses()
                , portfolio.getTargetAmount()
                , portfolio.getCurrentAmount()
                , portfolio.getInterestRate());
    }
}
