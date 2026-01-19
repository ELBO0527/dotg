'use client'

import Header from "@/app/component/header/page"
import apiClient from "@/lib/axios"
import { FormEvent, useState } from "react"
import { useRouter } from "next/navigation"

interface SignUpRequest {
  username: string;
  passwd: string;
  name: string;
}

interface SignUpResponse {
  status: string;
  message: string;
  data: string;
}

export default function SignUpPage() {
  const [signupRequest, setSignupRequest] = useState<SignUpRequest>({
    username: '',
    passwd: '',
    name: ''
  });
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    // 비밀번호 확인
    if (signupRequest.passwd !== passwordConfirm) {
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }

    // 입력 검증
    if (signupRequest.username.length < 4) {
      alert('아이디는 4글자 이상이어야 합니다.');
      return;
    }

    if (signupRequest.passwd.length < 6) {
      alert('비밀번호는 6글자 이상이어야 합니다.');
      return;
    }

    if (!signupRequest.name.trim()) {
      alert('이름을 입력해주세요.');
      return;
    }

    setIsLoading(true);

    try {
      const result = await apiClient.post<SignUpResponse>("/api/v1/signup", signupRequest);

      if (result.data.status === 'SUCCESS') {
        alert("회원가입 성공! 로그인 페이지로 이동합니다.");
        router.push('/login');
      } else {
        alert(result.data.message || "회원가입에 실패했습니다.");
      }
    } catch (error: any) {
      console.error('회원가입 에러:', error);
      const errorMessage = error.response?.data?.message || "회원가입에 실패했습니다.";
      alert(errorMessage);
    } finally {
      setIsLoading(false);
    }
  }

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setSignupRequest(prevState => ({
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
            회원가입
          </h2>
          <p className="mt-2 text-center text-sm text-gray-600 dark:text-gray-400">
            새로운 계정을 만들어보세요
          </p>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-6" onSubmit={handleSubmit} method="POST">
            {/* 아이디 */}
            <div>
              <label htmlFor="username" className="block dark:text-white text-sm font-medium leading-6 text-gray-900">
                아이디 <span className="text-red-500">*</span>
              </label>
              <div className="mt-2">
                <input
                  id="username"
                  name="username"
                  type="text"
                  autoComplete="username"
                  required
                  value={signupRequest.username}
                  onChange={handleInputChange}
                  placeholder="4글자 이상"
                  className="block w-full dark:bg-slate-800 rounded-md border-0 px-3 py-1.5 text-gray-900 dark:text-gray-100 shadow-sm ring-1 ring-inset ring-gray-300 dark:ring-gray-600 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            {/* 이름 */}
            <div>
              <label htmlFor="name" className="block dark:text-white text-sm font-medium leading-6 text-gray-900">
                이름 <span className="text-red-500">*</span>
              </label>
              <div className="mt-2">
                <input
                  id="name"
                  name="name"
                  type="text"
                  autoComplete="name"
                  required
                  value={signupRequest.name}
                  onChange={handleInputChange}
                  placeholder="홍길동"
                  className="block w-full dark:bg-slate-800 rounded-md border-0 px-3 py-1.5 text-gray-900 dark:text-gray-100 shadow-sm ring-1 ring-inset ring-gray-300 dark:ring-gray-600 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            {/* 비밀번호 */}
            <div>
              <label htmlFor="passwd" className="block dark:text-white text-sm font-medium leading-6 text-gray-900">
                비밀번호 <span className="text-red-500">*</span>
              </label>
              <div className="mt-2">
                <input
                  id="passwd"
                  name="passwd"
                  type="password"
                  autoComplete="new-password"
                  required
                  value={signupRequest.passwd}
                  onChange={handleInputChange}
                  placeholder="6글자 이상"
                  className="block w-full dark:bg-slate-800 rounded-md border-0 px-3 py-1.5 text-gray-900 dark:text-gray-100 shadow-sm ring-1 ring-inset ring-gray-300 dark:ring-gray-600 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            {/* 비밀번호 확인 */}
            <div>
              <label htmlFor="passwordConfirm" className="block dark:text-white text-sm font-medium leading-6 text-gray-900">
                비밀번호 확인 <span className="text-red-500">*</span>
              </label>
              <div className="mt-2">
                <input
                  id="passwordConfirm"
                  name="passwordConfirm"
                  type="password"
                  autoComplete="new-password"
                  required
                  value={passwordConfirm}
                  onChange={(e) => setPasswordConfirm(e.target.value)}
                  placeholder="비밀번호를 다시 입력하세요"
                  className="block w-full dark:bg-slate-800 rounded-md border-0 px-3 py-1.5 text-gray-900 dark:text-gray-100 shadow-sm ring-1 ring-inset ring-gray-300 dark:ring-gray-600 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
              {passwordConfirm && signupRequest.passwd !== passwordConfirm && (
                <p className="mt-1 text-xs text-red-500">비밀번호가 일치하지 않습니다</p>
              )}
              {passwordConfirm && signupRequest.passwd === passwordConfirm && (
                <p className="mt-1 text-xs text-green-500">비밀번호가 일치합니다</p>
              )}
            </div>

            {/* 회원가입 버튼 */}
            <div>
              <button
                type="submit"
                disabled={isLoading}
                className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:bg-gray-400 disabled:cursor-not-allowed"
              >
                {isLoading ? '회원가입 중...' : '회원가입'}
              </button>
            </div>
          </form>

          <p className="mt-10 text-center text-sm text-gray-500 dark:text-gray-400">
            이미 회원이신가요?{' '}
            <a href="/login" className="font-semibold leading-6 text-indigo-600 hover:text-indigo-500">
              로그인하기
            </a>
          </p>
        </div>
      </div>
    </>
  )
}
