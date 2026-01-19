'use client'

import Header from "@/app/component/header/page"
import apiClient from "@/lib/axios"
import { useAuth } from "@/contexts/AuthContext"
import { FormEvent, useState } from "react"
import { useRouter } from "next/navigation"

interface SigninRequest {
  username: string,
  passwd: string
}

interface SigninResponse {
  status: string;
  message: string;
  data: {
    grantType: string;
    accessToken: string;
    refreshToken: string;
    accessTokenExpiresIn: number;
  };
}

export default function SigninPage() {
  const [signinRequest, setSigninRequest] = useState<SigninRequest>({ username: '', passwd: '' });
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuth();
  const router = useRouter();

  async function handleSubmit(event: FormEvent<HTMLFormElement>)  {
    event.preventDefault();
    setIsLoading(true);

    try {
        const result = await apiClient.post<SigninResponse>("/api/v1/signin", signinRequest);

        if (result.data.status === 'SUCCESS' && result.data.data) {
          const { accessToken, refreshToken } = result.data.data;

          // 토큰 및 사용자 정보 저장
          login(accessToken, { username: signinRequest.username });

          // refreshToken도 저장 (나중에 토큰 갱신에 사용)
          localStorage.setItem('refreshToken', refreshToken);

          alert("로그인 성공!");
          router.push('/'); // 메인 페이지로 이동
        } else {
          alert("로그인에 실패했습니다.");
        }
      } catch (error: any) {
        console.error('로그인 에러:', error);
        const errorMessage = error.response?.data?.message || "로그인에 실패했습니다.";
        alert(errorMessage);
      } finally {
        setIsLoading(false);
      }
  }

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setSigninRequest(prevState => ({
      ...prevState,
      [name]: value
    }));
  }

  return (
    <>
      <div className="flex h-screen dark:bg-gray-800 flex-1 flex-col justify-center px-6 lg:px-8 content-center bg-white">
        <Header />
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <a href="/" className="flex justify-center items-center gap-2 mb-10">
            <div className="h-12 w-12 rounded-lg bg-gradient-to-br from-indigo-600 to-purple-600 flex items-center justify-center">
              <span className="text-white font-bold text-2xl">D</span>
            </div>
            <span className="text-2xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">DOTG</span>
          </a>
          <h2 className="mt-6 dark:text-white text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
            로그인
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-6" onSubmit={handleSubmit} method="POST">
            <div>
              <label htmlFor="username" className="block dark:text-white text-sm font-medium leading-6 text-gray-900">
                아이디
              </label>
              <div className="mt-2">
                <input
                  id="username"
                  name="username"
                  //type="email"
                  autoComplete="username"
                  required
                  value={signinRequest.username}
                  onChange={handleInputChange}
                  className="block w-full dark:bg-slate-800 rounded-md border-0 py-1.5 text-gray-400 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label htmlFor="password" className="block dark:text-white text-sm font-medium leading-6 text-gray-900">
                  비밀번호
                </label>
                <div className="text-sm">
                  <a href="#" className="font-semibold dark:text-white text-indigo-600 hover:text-indigo-500">
                    비밀번호를 잊어버렸어요
                  </a>
                </div>
              </div>
              <div className="mt-2">
                <input
                  id="passwd"
                  name="passwd"
                  type="password"
                  autoComplete="current-password"
                  value={signinRequest.passwd}
                  onChange={handleInputChange}
                  required
                  className="block w-full dark:bg-slate-800 rounded-md border-0 py-1.5 text-gray-400 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                disabled={isLoading}
                className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:bg-gray-400 disabled:cursor-not-allowed"
              >
                {isLoading ? '로그인 중...' : '로그인'}
              </button>
            </div>
          </form>

          <p className="mt-10 text-center text-sm text-gray-500 dark:text-gray-400">
            아직 회원이 아니신가요?{' '}
            <a href="/signup" className="font-semibold leading-6 text-indigo-600 hover:text-indigo-500">
              회원가입하기
            </a>
          </p>
        </div>
      </div>
    </>
  )
}
  