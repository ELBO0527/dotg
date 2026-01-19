"use client";

import Header from '@component/header/page';

export default function IntroducePage() {
  return (
    <div className="bg-white dark:bg-gray-900 min-h-screen">
      <Header />

      {/* Hero Section */}
      <div className="relative isolate px-6 pt-24 lg:px-8">
        <div className="mx-auto max-w-5xl py-16 sm:py-24">
          <div className="text-center mb-16">
            <h1 className="text-5xl font-extrabold tracking-tight text-gray-900 dark:text-white sm:text-6xl mb-6">
              <span className="block">DOTG</span>
              <span className="block text-3xl sm:text-4xl mt-2 bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                Do On The Go
              </span>
            </h1>
            <p className="mt-6 text-xl leading-8 text-gray-600 dark:text-gray-300 max-w-3xl mx-auto">
              여기에 DOTG에 대한 간단한 소개를 작성하세요.
              당신의 서비스가 제공하는 핵심 가치를 2-3줄로 설명해보세요.
            </p>
          </div>

          {/* 서비스 소개 섹션 */}
          <div className="mt-20">
            <h2 className="text-3xl font-bold text-gray-900 dark:text-white text-center mb-12">
              우리의 서비스
            </h2>
            <div className="grid grid-cols-1 gap-8 md:grid-cols-2 lg:grid-cols-3">
              {/* 카드 1 */}
              <div className="relative group">
                <div className="h-full p-8 bg-white dark:bg-gray-800 rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-300 border border-gray-100 dark:border-gray-700">
                  <div className="w-12 h-12 bg-indigo-600 rounded-lg flex items-center justify-center mb-6">
                    <span className="text-2xl">📊</span>
                  </div>
                  <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-4">
                    복리 계산기
                  </h3>
                  <p className="text-gray-600 dark:text-gray-300 leading-relaxed">
                    여기에 복리 계산기 기능에 대한 설명을 작성하세요.
                    사용자가 얻을 수 있는 가치를 강조해보세요.
                  </p>
                </div>
              </div>

              {/* 카드 2 */}
              <div className="relative group">
                <div className="h-full p-8 bg-white dark:bg-gray-800 rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-300 border border-gray-100 dark:border-gray-700">
                  <div className="w-12 h-12 bg-indigo-600 rounded-lg flex items-center justify-center mb-6">
                    <span className="text-2xl">💬</span>
                  </div>
                  <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-4">
                    커뮤니티
                  </h3>
                  <p className="text-gray-600 dark:text-gray-300 leading-relaxed">
                    여기에 커뮤니티 기능에 대한 설명을 작성하세요.
                    사용자들이 어떻게 소통하고 정보를 공유하는지 설명해보세요.
                  </p>
                </div>
              </div>

              {/* 카드 3 */}
              <div className="relative group">
                <div className="h-full p-8 bg-white dark:bg-gray-800 rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-300 border border-gray-100 dark:border-gray-700">
                  <div className="w-12 h-12 bg-indigo-600 rounded-lg flex items-center justify-center mb-6">
                    <span className="text-2xl">📈</span>
                  </div>
                  <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-4">
                    시장 정보
                  </h3>
                  <p className="text-gray-600 dark:text-gray-300 leading-relaxed">
                    여기에 시장 정보 기능에 대한 설명을 작성하세요.
                    제공되는 데이터와 분석 도구에 대해 설명해보세요.
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* 비전/미션 섹션 */}
          <div className="mt-24">
            <div className="bg-indigo-600 rounded-3xl p-12 text-center">
              <h2 className="text-3xl font-bold text-white mb-6">
                우리의 비전
              </h2>
              <p className="text-xl text-white/90 max-w-3xl mx-auto leading-relaxed">
                여기에 DOTG의 비전과 미션을 작성하세요.
                앞으로 나아가고자 하는 방향과 사용자에게 제공하고 싶은 가치를 설명해보세요.
              </p>
            </div>
          </div>

          {/* 팀/연혁 섹션 */}
          <div className="mt-24">
            <h2 className="text-3xl font-bold text-gray-900 dark:text-white text-center mb-12">
              연혁
            </h2>
            <div className="max-w-3xl mx-auto">
              <div className="space-y-8">
                {/* 타임라인 아이템 1 */}
                <div className="flex gap-6">
                  <div className="flex flex-col items-center">
                    <div className="w-4 h-4 rounded-full bg-indigo-600"></div>
                    <div className="w-0.5 h-full bg-indigo-200 dark:bg-indigo-900"></div>
                  </div>
                  <div className="pb-8">
                    <p className="text-sm font-semibold text-indigo-600 dark:text-indigo-400 mb-1">
                      2026년 1월
                    </p>
                    <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-2">
                      프로젝트 시작
                    </h3>
                    <p className="text-gray-600 dark:text-gray-300">
                      여기에 프로젝트 시작에 대한 설명을 작성하세요.
                    </p>
                  </div>
                </div>

                {/* 타임라인 아이템 2 */}
                <div className="flex gap-6">
                  <div className="flex flex-col items-center">
                    <div className="w-4 h-4 rounded-full bg-indigo-600"></div>
                    <div className="w-0.5 h-full bg-indigo-200 dark:bg-indigo-900"></div>
                  </div>
                  <div className="pb-8">
                    <p className="text-sm font-semibold text-indigo-600 dark:text-indigo-400 mb-1">
                      2026년 2월 (예정)
                    </p>
                    <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-2">
                      베타 서비스 런칭
                    </h3>
                    <p className="text-gray-600 dark:text-gray-300">
                      여기에 베타 서비스 런칭 계획을 작성하세요.
                    </p>
                  </div>
                </div>

                {/* 타임라인 아이템 3 */}
                <div className="flex gap-6">
                  <div className="flex flex-col items-center">
                    <div className="w-4 h-4 rounded-full bg-indigo-600"></div>
                  </div>
                  <div>
                    <p className="text-sm font-semibold text-indigo-600 dark:text-indigo-400 mb-1">
                      2026년 상반기 (예정)
                    </p>
                    <h3 className="text-xl font-bold text-gray-900 dark:text-white mb-2">
                      정식 서비스 오픈
                    </h3>
                    <p className="text-gray-600 dark:text-gray-300">
                      여기에 정식 서비스 오픈 계획을 작성하세요.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* CTA 섹션 */}
          <div className="mt-24 text-center">
            <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-6">
              지금 시작하세요
            </h2>
            <p className="text-xl text-gray-600 dark:text-gray-300 mb-8">
              여기에 사용자를 독려하는 메시지를 작성하세요.
            </p>
            <div className="flex items-center justify-center gap-x-6">
              <a
                href="/signup"
                className="rounded-xl bg-indigo-600 px-8 py-4 text-base font-semibold text-white shadow-sm hover:bg-indigo-500 transition-colors"
              >
                회원가입하기
              </a>
              <a
                href="/portfolio"
                className="text-base font-semibold leading-6 text-gray-900 dark:text-white hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors"
              >
                둘러보기 <span aria-hidden="true">→</span>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
