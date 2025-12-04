"use client";

import { FormEvent, useState } from "react"
import Header from '@component/header/page';
import axios, { AxiosResponse } from "axios"

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

export default function savingPortfolioMain() {
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
    interestRate: ''
  });

  const [list, setList] = useState<number[]>([]);

  // input 변경 핸들러
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    const cleanedValue = value.replace(/\D/g, ''); 
    setPortfolioRequest(prev => ({ ...prev, [name]: cleanedValue }));
  };

  async function handleSubmit(event: FormEvent<HTMLFormElement>)  {
    event.preventDefault();

    const { monthlySalary, monthlySaving, targetAmount } = portfolioRequest;
    const ms = Number(monthlySalary) || 0;
    const save = Number(monthlySaving) || 0;
    const target = Number(targetAmount) || 0;

    if (!ms || !save || !target) return;

    // 예시 계산: 목표 금액까지 걸리는 개월 수
    const months = Math.ceil(target / (ms + save));

    // 리스트 만들기 (0~months)
    const result: number[] = [];
    for (let i = 1; i <= months; i++) {
      result.push(i * (ms + save));
    }

    setList(result);

    try {
        const result: AxiosResponse<any, any> = await axios.post("/api/v1/portfolios", portfolioRequest );
        console.log(result);
      } catch (error: any) {
        alert(error.response.data.message) //TODO:form validation으로 바꾸기
      }
  }

  return (
    <div className="bg-white dark:bg-gray-800 min-h-screen">
      <Header />

      <div className="mx-auto max-w-2xl py-12 px-6 sm:py-48 lg:py-56">
        <h1 className="text-3xl font-bold text-center dark:text-white mb-6">계산기</h1>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-6" onSubmit={handleSubmit} method="POST">
            <label htmlFor="monthlySalary" className="block dark:text-white text-sm font-medium leading-6 text-gray-900">
              월급
            </label>
            <div className="mt-2">
              <input
                id="monthlySalary"
                name="monthlySalary"
                type="text"
                autoComplete="monthlySalary"
                required
                value={portfolioRequest.monthlySalary}
                onChange={handleChange}
                className="block w-full dark:bg-slate-800 rounded-md border-0 py-1.5 text-gray-400 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset sm:text-sm sm:leading-6"
              />
            </div>

            <div className="flex items-center justify-between">
              <label htmlFor="monthlySaving" className="block dark:text-white text-sm font-medium leading-6 text-gray-900">
                저축액
              </label>
            </div>
            <div className="mt-2">
              <input
                id="monthlySaving"
                name="monthlySaving"
                type="text"
                value={portfolioRequest.monthlySaving}
                onChange={handleChange}
                required
                className="block w-full dark:bg-slate-800 rounded-md border-0 py-1.5 text-gray-400 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset sm:text-sm sm:leading-6"
              />
            </div>

            <div className="flex items-center justify-between">
              <label htmlFor="targetAmount" className="block dark:text-white text-sm font-medium leading-6 text-gray-900">
                목표 금액
              </label>
            </div>
            <div className="mt-2">
              <input
                id="targetAmount"
                name="targetAmount"
                type="text"
                value={portfolioRequest.targetAmount}
                onChange={handleChange}
                required
                className="block w-full dark:bg-slate-800 rounded-md border-0 py-1.5 text-gray-400 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset sm:text-sm sm:leading-6"
              />
            </div>

            <div>
              <button
                type="submit"
                className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                계산하기
              </button>
            </div>
          </form>

        {/* 결과 리스트 */}
        {list.length > 0 && (
          <ul className="mt-6 space-y-2">
            {list.map((item, index) => (
              <li key={index} className="border rounded p-2 dark:border-gray-700">
                {index + 1}개월: {item.toLocaleString()} 원
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
    </div>
  );
}