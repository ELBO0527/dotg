package elbo.dotg.api17.domain.portfolio;

import elbo.dotg.api17.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "portfolio", indexes = @Index(name = "i_portfolio", columnList = "portfolio_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "portfolio_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private long monthlySalary;  // 월급

    @Column
    private long annualSalary;  // 연봉

    @Column
    private long monthlySaving;  // 월 저축액

    @Column
    private long period;

    @Column
    private long extra;

    @Column
    private long monthlyExpenses;  // 월 소비액

    @Column
    private long targetAmount;   // 목표 금액

    @Column
    private long currentAmount;  // 현재 저축액

    @Column
    private double interestRate;   // 연이자율

    private Portfolio(User user, long monthlySalary, long annualSalary, long monthlySaving, long period, long extra, long monthlyExpenses, long targetAmount, long currentAmount, double interestRate) {
        this.user = user;
        this.monthlySalary = monthlySalary;
        this.annualSalary = annualSalary;
        this.monthlySaving = monthlySaving;
        this.period = period;
        this.extra = extra;
        this.monthlyExpenses = monthlyExpenses;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.interestRate = interestRate;
    }

    public static Portfolio of(User user, long monthlySalary, long annualSalary, long monthlySaving, long period, long extra, long monthlyExpenses, long targetAmount, long currentAmount, double interestRate) {
        return new Portfolio(user, monthlySalary, annualSalary, monthlySaving, period, extra, monthlyExpenses, targetAmount, currentAmount, interestRate);
    }

    public void updatePorfolio(Portfolio portfolio) {
        this.id = portfolio.getId();
        this.user = portfolio.getUser();
        this.monthlySalary = portfolio.getMonthlySalary();
        this.annualSalary = portfolio.getAnnualSalary();
        this.monthlySaving = portfolio.getMonthlySaving();
        this.period = portfolio.getPeriod();
        this.extra = portfolio.getExtra();
        this.monthlyExpenses = portfolio.getMonthlyExpenses();
        this.targetAmount = portfolio.getTargetAmount();
        this.currentAmount = portfolio.getCurrentAmount();
        this.interestRate = portfolio.getInterestRate();
    }
}
