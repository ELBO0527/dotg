package elbo.dotg.api17.controller.portfolio;

import elbo.dotg.api17.dto.request.portfolio.SavePortfolioRequest;
import elbo.dotg.api17.dto.response.common.ApiResponse;
import elbo.dotg.api17.dto.response.portfolio.PortfolioResponse;
import elbo.dotg.api17.service.portfolio.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static elbo.dotg.api17.dto.response.common.ApiResponse.success;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/portfolios")
@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;

    @GetMapping
    public ApiResponse<List<PortfolioResponse>> findAllPortfolios(){
        return success(portfolioService.findAllPortfolios());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> savePortfolio(@RequestBody SavePortfolioRequest savePortfolioRequest){
        return success(portfolioService.savePortfolio(savePortfolioRequest));
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<PortfolioResponse> updatePortfolio(@PathVariable long id, @RequestBody SavePortfolioRequest savePortfolioRequest){
        return success(portfolioService.updatePortfolioById(id, savePortfolioRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ApiResponse<Long> deletePortfolio(@PathVariable long id){
        return success(portfolioService.deletePortfolio(id));
    }
}
