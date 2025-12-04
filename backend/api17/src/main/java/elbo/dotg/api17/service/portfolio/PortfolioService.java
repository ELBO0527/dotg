package elbo.dotg.api17.service.portfolio;

import elbo.dotg.api17.advice.exception.board.BoardNotFoundException;
import elbo.dotg.api17.advice.exception.portfolio.PortFolioNotFoundException;
import elbo.dotg.api17.advice.exception.user.UserNotFoundException;
import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.domain.order.Orders;
import elbo.dotg.api17.domain.portfolio.Portfolio;
import elbo.dotg.api17.dto.request.board.UpdateBoardRequest;
import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import elbo.dotg.api17.dto.request.portfolio.SavePortfolioRequest;
import elbo.dotg.api17.dto.response.category.CategoryResponse;
import elbo.dotg.api17.dto.response.order.OrderResponse;
import elbo.dotg.api17.dto.response.portfolio.PortfolioResponse;
import elbo.dotg.api17.repository.portfolio.PortfolioRepository;
import elbo.dotg.api17.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public List<PortfolioResponse> findAllPortfolios(){
        List<Portfolio> portfolios = portfolioRepository.findAll();
        return portfolios.stream().map(PortfolioResponse::from).collect(Collectors.toList());
    }

    public PortfolioResponse findPortfolioById(final long id){
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(PortFolioNotFoundException::new);
        return PortfolioResponse.from(portfolio);
    }

    @Transactional
    public PortfolioResponse updatePortfolioById(final long id, final SavePortfolioRequest boardUpdateRequest){
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(PortFolioNotFoundException::new);
        //portfolio.updatePorfolio();
        return PortfolioResponse.from(portfolioRepository.findById(id).orElseThrow(PortFolioNotFoundException::new));
    }

    @Transactional
    public long savePortfolio(SavePortfolioRequest savePortfolioRequest) {
        Portfolio portfolio = Portfolio.of(
                userRepository.findById(savePortfolioRequest.userId()).orElseThrow(UserNotFoundException::new)
                , savePortfolioRequest.monthlySalary()
                , savePortfolioRequest.monthlySaving()
                , savePortfolioRequest.monthlyExpenses()
                , savePortfolioRequest.period()
                , savePortfolioRequest.extra()
                , savePortfolioRequest.currentAmount()
                , savePortfolioRequest.targetAmount()
                , savePortfolioRequest.annualSlary()
                , savePortfolioRequest.interestRate());

        portfolioRepository.save(portfolio);
        return portfolio.getId();
    }

    @Transactional
    public long deletePortfolio(final long id) {
        portfolioRepository.findById(id).orElseThrow(PortFolioNotFoundException::new);
        portfolioRepository.deleteById(id);
        return id;
    }


}
