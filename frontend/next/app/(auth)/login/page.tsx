'use client'

import Header from "@/app/component/header/page"
import axios, { AxiosResponse } from "axios"
import { FormEvent, useState } from "react"

interface User {
  username: string,
  passwd: string
}

export default function SigninPage() {
  const [signinRequest, setSigninRequest] = useState<User>({ username: '', passwd: '' });
  
  async function handleSubmit(event: FormEvent<HTMLFormElement>)  {
    event.preventDefault();

    try {
        const result: AxiosResponse<any, any> = await axios.post("/api/v1/signin", signinRequest );
        console.log(result)
        alert("로그인 성공")
      } catch (error: any) {
        alert(error.response.data.message) //TODO:form validation으로 바꾸기
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
        <div className="flex h-screen flex-1 flex-col justify-center px-6 lg:px-8 content-center">
          <Header />
          <div className="sm:mx-auto sm:w-full sm:max-w-sm">
            <img
              className="mx-auto h-10 w-auto"
              src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600"
              alt="Your Company"
            />
            <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
              로그인
            </h2>
          </div>
  
          <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
            <form className="space-y-6" onSubmit={handleSubmit} method="POST">
              <div>
                <label htmlFor="username" className="block text-sm font-medium leading-6 text-gray-900">
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
                    className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                  />
                </div>
              </div>
  
              <div>
                <div className="flex items-center justify-between">
                  <label htmlFor="password" className="block text-sm font-medium leading-6 text-gray-900">
                    비밀번호
                  </label>
                  <div className="text-sm">
                    <a href="#" className="font-semibold text-indigo-600 hover:text-indigo-500">
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
                    className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                  />
                </div>
              </div>
  
              <div>
                <button
                  type="submit"
                  className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                >
                  로그인
                </button>
              </div>
            </form>
  
            <p className="mt-10 text-center text-sm text-gray-500">
              아직 회원이 아니신가요?{' '}
              <a href="#" className="font-semibold leading-6 text-indigo-600 hover:text-indigo-500">
                회원가입
              </a>
            </p>
          </div>
        </div>
      </>
    )
  }
  