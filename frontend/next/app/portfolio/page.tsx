"use client";

import { FormEvent, useState } from "react"
import Header from '@component/header/page';

interface PortfolioRequest {
  userId: number;
  monthlySalary: string;
  annualSalary: string;
  monthlySaving: string;
  period: string;
  extra: string;
  monthlyExpenses: string;
  targetAmount: string;
  currentAmount: string;
  interestRate: string;
}

interface CalculationResult {
  monthsNeeded: number;
  monthlyProgress: number[];
  totalSaved: number;
  interestEarned: number;
}

export default function SavingPortfolioMain() {
  const [portfolioRequest, setPortfolioRequest] = useState<PortfolioRequest>({
    userId: 1,
    monthlySalary: '',
    annualSalary: '',
    monthlySaving: '',
    period: '',
    extra: '',
    monthlyExpenses: '',
    targetAmount: '',
    currentAmount: '',
    interestRate: '3.5'
  });

  const [calculationResult, setCalculationResult] = useState<CalculationResult | null>(null);

  // 숫자를 천 단위 콤마로 포맷팅
  const formatNumber = (value: string): string => {
    const number = value.replace(/\D/g, '');
    return number ? Number(number).toLocaleString() : '';
  };

  // input 변경 핸들러 (천 단위 콤마 적용)
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    const cleanedValue = value.replace(/\D/g, '');
    setPortfolioRequest(prev => ({ ...prev, [name]: cleanedValue }));
  };

  // 복리 계산 함수
  const calculateCompoundInterest = (
    principal: number,
    monthlyDeposit: number,
    annualRate: number,
    months: number
  ): number[] => {
    const monthlyRate = annualRate / 12 / 100;
    const progress: number[] = [];
    let total = principal;

    for (let i = 1; i <= months; i++) {
      total = total * (1 + monthlyRate) + monthlyDeposit;
      progress.push(Math.floor(total));
    }

    return progress;
  };

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const {
      monthlySalary,
      monthlySaving,
      targetAmount,
      currentAmount,
      interestRate,
      monthlyExpenses
    } = portfolioRequest;

    const ms = Number(monthlySalary) || 0;
    const save = Number(monthlySaving) || 0;
    const target = Number(targetAmount) || 0;
    const current = Number(currentAmount) || 0;
    const rate = Number(interestRate) || 0;
    const expenses = Number(monthlyExpenses) || 0;

    if (!target) return;

    // 실제 저축 가능 금액 계산
    const actualMonthlySaving = save || (ms - expenses);

    if (actualMonthlySaving <= 0) {
      alert('월 저축액이 0보다 커야 합니다. 수입과 지출을 확인해주세요.');
      return;
    }

    // 복리 계산으로 목표 달성 개월 수 계산
    let months = 0;
    let totalSaved = current;
    const monthlyRate = rate / 12 / 100;
    const maxMonths = 600; // 최대 50년

    while (totalSaved < target && months < maxMonths) {
      months++;
      totalSaved = totalSaved * (1 + monthlyRate) + actualMonthlySaving;
    }

    if (months >= maxMonths) {
      alert('현재 조건으로는 목표 달성이 어렵습니다. 저축액을 늘리거나 목표 금액을 조정해주세요.');
      return;
    }

    // 월별 진행 상황 계산
    const monthlyProgress = calculateCompoundInterest(
      current,
      actualMonthlySaving,
      rate,
      months
    );

    const interestEarned = totalSaved - current - (actualMonthlySaving * months);

    setCalculationResult({
      monthsNeeded: months,
      monthlyProgress,
      totalSaved: Math.floor(totalSaved),
      interestEarned: Math.floor(interestEarned)
    });

    // 백엔드 API 호출은 주석 처리 (나중에 연동)
    // try {
    //   const result: AxiosResponse<any, any> = await axios.post("/api/v1/portfolios", portfolioRequest);
    //   console.log(result);
    // } catch (error: any) {
    //   alert(error.response.data.message);
    // }
  }

  return (
    <div className="bg-gray-50 dark:bg-gray-900 min-h-screen">
      <Header />

      <div className="mx-auto max-w-7xl pt-24 pb-8 px-4 sm:px-6 lg:px-8">
        {/* 페이지 헤더 */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-2">
            재정 목표 계산기
          </h1>
          <p className="text-gray-600 dark:text-gray-400">
            목표 금액까지 얼마나 걸릴지 계산해보세요
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* 입력 폼 */}
          <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg p-6 lg:p-8">
            <h2 className="text-2xl font-semibold text-gray-900 dark:text-white mb-6">
              정보 입력
            </h2>

            <form className="space-y-6" onSubmit={handleSubmit}>
              {/* 월급 */}
              <div>
                <label htmlFor="monthlySalary" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  월급 (선택)
                </label>
                <div className="relative">
                  <input
                    id="monthlySalary"
                    name="monthlySalary"
                    type="text"
                    value={formatNumber(portfolioRequest.monthlySalary)}
                    onChange={handleChange}
                    placeholder="3,000,000"
                    className="block w-full px-4 py-3 bg-gray-50 dark:bg-gray-700 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                  />
                  <span className="absolute right-4 top-3 text-gray-500 dark:text-gray-400">원</span>
                </div>
              </div>

              {/* 월 소비액 */}
              <div>
                <label htmlFor="monthlyExpenses" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  월 소비액 (선택)
                </label>
                <div className="relative">
                  <input
                    id="monthlyExpenses"
                    name="monthlyExpenses"
                    type="text"
                    value={formatNumber(portfolioRequest.monthlyExpenses)}
                    onChange={handleChange}
                    placeholder="2,000,000"
                    className="block w-full px-4 py-3 bg-gray-50 dark:bg-gray-700 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                  />
                  <span className="absolute right-4 top-3 text-gray-500 dark:text-gray-400">원</span>
                </div>
              </div>

              {/* 월 저축액 */}
              <div>
                <label htmlFor="monthlySaving" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  월 저축액 <span className="text-red-500">*</span>
                </label>
                <div className="relative">
                  <input
                    id="monthlySaving"
                    name="monthlySaving"
                    type="text"
                    value={formatNumber(portfolioRequest.monthlySaving)}
                    onChange={handleChange}
                    required
                    placeholder="1,000,000"
                    className="block w-full px-4 py-3 bg-gray-50 dark:bg-gray-700 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                  />
                  <span className="absolute right-4 top-3 text-gray-500 dark:text-gray-400">원</span>
                </div>
                <p className="mt-1 text-xs text-gray-500 dark:text-gray-400">
                  월급과 소비액을 입력하지 않은 경우 필수 입력
                </p>
              </div>

              {/* 현재 저축액 */}
              <div>
                <label htmlFor="currentAmount" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  현재 저축액 (선택)
                </label>
                <div className="relative">
                  <input
                    id="currentAmount"
                    name="currentAmount"
                    type="text"
                    value={formatNumber(portfolioRequest.currentAmount)}
                    onChange={handleChange}
                    placeholder="5,000,000"
                    className="block w-full px-4 py-3 bg-gray-50 dark:bg-gray-700 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                  />
                  <span className="absolute right-4 top-3 text-gray-500 dark:text-gray-400">원</span>
                </div>
              </div>

              {/* 목표 금액 */}
              <div>
                <label htmlFor="targetAmount" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  목표 금액 <span className="text-red-500">*</span>
                </label>
                <div className="relative">
                  <input
                    id="targetAmount"
                    name="targetAmount"
                    type="text"
                    value={formatNumber(portfolioRequest.targetAmount)}
                    onChange={handleChange}
                    required
                    placeholder="100,000,000"
                    className="block w-full px-4 py-3 bg-gray-50 dark:bg-gray-700 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                  />
                  <span className="absolute right-4 top-3 text-gray-500 dark:text-gray-400">원</span>
                </div>
              </div>

              {/* 연 이자율 */}
              <div>
                <label htmlFor="interestRate" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  연 이자율 (%)
                </label>
                <input
                  id="interestRate"
                  name="interestRate"
                  type="text"
                  value={portfolioRequest.interestRate}
                  onChange={handleChange}
                  placeholder="3.5"
                  className="block w-full px-4 py-3 bg-gray-50 dark:bg-gray-700 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                />
                <p className="mt-1 text-xs text-gray-500 dark:text-gray-400">
                  예금 또는 적금 이자율을 입력하세요 (기본값: 3.5%)
                </p>
              </div>

              {/* 계산 버튼 */}
              <button
                type="submit"
                className="w-full bg-indigo-600 hover:bg-indigo-500 text-white font-semibold py-4 px-6 rounded-lg shadow-sm transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                목표 달성 기간 계산하기
              </button>
            </form>
          </div>

          {/* 계산 결과 */}
          <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg p-6 lg:p-8">
            <h2 className="text-2xl font-semibold text-gray-900 dark:text-white mb-6">
              계산 결과
            </h2>

            {!calculationResult ? (
              <div className="flex flex-col items-center justify-center h-64 text-gray-400 dark:text-gray-500">
                <svg className="w-16 h-16 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                </svg>
                <p className="text-center">정보를 입력하고 계산 버튼을 눌러주세요</p>
              </div>
            ) : (
              <div className="space-y-6">
                {/* 주요 지표 카드 */}
                <div className="grid grid-cols-2 gap-4">
                  <div className="bg-blue-50 dark:bg-blue-900/20 rounded-lg p-4 border border-blue-200 dark:border-blue-700">
                    <p className="text-sm text-blue-600 dark:text-blue-400 font-medium mb-1">목표 달성까지</p>
                    <p className="text-2xl font-bold text-blue-900 dark:text-blue-100">
                      {calculationResult.monthsNeeded}개월
                    </p>
                    <p className="text-xs text-blue-600 dark:text-blue-400 mt-1">
                      약 {Math.floor(calculationResult.monthsNeeded / 12)}년 {calculationResult.monthsNeeded % 12}개월
                    </p>
                  </div>

                  <div className="bg-green-50 dark:bg-green-900/20 rounded-lg p-4 border border-green-200 dark:border-green-700">
                    <p className="text-sm text-green-600 dark:text-green-400 font-medium mb-1">예상 이자 수익</p>
                    <p className="text-2xl font-bold text-green-900 dark:text-green-100">
                      {calculationResult.interestEarned.toLocaleString()}원
                    </p>
                    <p className="text-xs text-green-600 dark:text-green-400 mt-1">
                      복리 적용
                    </p>
                  </div>
                </div>

                {/* 프로그레스 바 */}
                <div>
                  <div className="flex justify-between text-sm text-gray-600 dark:text-gray-400 mb-2">
                    <span>현재: {Number(portfolioRequest.currentAmount || 0).toLocaleString()}원</span>
                    <span>목표: {Number(portfolioRequest.targetAmount).toLocaleString()}원</span>
                  </div>
                  <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-4 overflow-hidden">
                    <div
                      className="bg-indigo-600 h-4 rounded-full transition-all duration-500"
                      style={{
                        width: `${Math.min(
                          ((Number(portfolioRequest.currentAmount) || 0) / Number(portfolioRequest.targetAmount)) * 100,
                          100
                        )}%`
                      }}
                    ></div>
                  </div>
                </div>

                {/* 상세 정보 */}
                <div className="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-4 space-y-3">
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600 dark:text-gray-400">월 저축액</span>
                    <span className="font-semibold text-gray-900 dark:text-white">
                      {Number(portfolioRequest.monthlySaving ||
                        (Number(portfolioRequest.monthlySalary) - Number(portfolioRequest.monthlyExpenses))
                      ).toLocaleString()}원
                    </span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600 dark:text-gray-400">총 저축 예정액</span>
                    <span className="font-semibold text-gray-900 dark:text-white">
                      {(calculationResult.totalSaved - calculationResult.interestEarned).toLocaleString()}원
                    </span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600 dark:text-gray-400">최종 금액 (원금+이자)</span>
                    <span className="font-bold text-indigo-600 dark:text-indigo-400">
                      {calculationResult.totalSaved.toLocaleString()}원
                    </span>
                  </div>
                </div>

                {/* 월별 진행 상황 */}
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-3">
                    월별 예상 진행 상황
                  </h3>
                  <div className="max-h-64 overflow-y-auto space-y-2 pr-2">
                    {calculationResult.monthlyProgress
                      .filter((_, index) => index % Math.max(1, Math.floor(calculationResult.monthsNeeded / 12)) === 0 || index === calculationResult.monthsNeeded - 1)
                      .map((amount, displayIndex) => {
                        const actualIndex = displayIndex * Math.max(1, Math.floor(calculationResult.monthsNeeded / 12));
                        const month = actualIndex === calculationResult.monthsNeeded - 1 ? calculationResult.monthsNeeded : actualIndex + 1;
                        const progress = (amount / Number(portfolioRequest.targetAmount)) * 100;

                        return (
                          <div key={month} className="flex items-center gap-3">
                            <span className="text-sm text-gray-600 dark:text-gray-400 w-16">
                              {month}개월
                            </span>
                            <div className="flex-1 bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                              <div
                                className="bg-indigo-600 h-2 rounded-full transition-all"
                                style={{ width: `${Math.min(progress, 100)}%` }}
                              ></div>
                            </div>
                            <span className="text-sm font-medium text-gray-900 dark:text-white w-32 text-right">
                              {amount.toLocaleString()}원
                            </span>
                          </div>
                        );
                      })}
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}