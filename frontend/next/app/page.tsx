import Header from '@component/header/page'

export default function HomePage() {
  return (
    <div className="bg-white h-screen dark:bg-gray-800">
      <Header />
      <div className="relative isolate px-6 pt-14 lg:px-8">
        <div
          className="absolute inset-x-0 -top-40 -z-10 transform-gpu overflow-hidden blur-3xl sm:-top-80"
          aria-hidden="true"
        >
          <div
            className="relative left-[calc(50%-11rem)] aspect-[1155/678] w-[36.125rem] -translate-x-1/2 rotate-[30deg] bg-gradient-to-tr from-[#ff80b5] to-[#9089fc] opacity-30 sm:left-[calc(50%-30rem)] sm:w-[72.1875rem]"
            style={{
              clipPath:
                'polygon(74.1% 44.1%, 100% 61.6%, 97.`5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)',
            }}
          />
        </div>
        <div className="mx-auto h-full max-w-4xl py-56 sm:py-48 lg:py-56">
          <div className="hidden sm:mb-8 sm:flex sm:justify-center">
            <div className="relative dark:bg-gray-800/50 dark:text-indigo-400 dark:ring-indigo-500/30 rounded-full px-5 py-2 text-sm leading-6 text-gray-600 ring-1 ring-indigo-600/20 bg-indigo-50/80 backdrop-blur-sm hover:ring-indigo-600/30 transition-all duration-300 cursor-pointer group">
              <span className="font-medium text-indigo-600 dark:text-indigo-400">🚀 새로운 기능 업데이트</span>
              <a href="/portfolio" className="ml-2 font-semibold text-indigo-700 dark:text-indigo-300 group-hover:text-indigo-800 dark:group-hover:text-indigo-200">
                <span className="absolute inset-0" aria-hidden="true" />
                확인하기 <span aria-hidden="true">&rarr;</span>
              </a>
            </div>
          </div>
          <div className="text-center">
            <h1 className="text-5xl dark:text-white py-3 font-extrabold tracking-tight text-gray-900 sm:text-7xl">
              <span className="block">투자의 시작,</span>
              <span className="block bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
                DOTG와 함께
              </span>
            </h1>
            <p className="mt-8 dark:text-slate-400 text-xl leading-8 text-gray-600 max-w-2xl mx-auto">
              똑똑한 자산 관리와 투자 계획을 위한 올인원 플랫폼.
              복리 계산기부터 커뮤니티까지, 당신의 금융 파트너가 되어드립니다.
            </p>
            <div className="mt-12 flex items-center justify-center gap-x-6">
              <a
                href="/portfolio"
                className="rounded-lg bg-indigo-600 px-8 py-3.5 text-base font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 transition-colors"
              >
                지금 시작하기
              </a>
              <a
                href="/board"
                className="text-base dark:text-slate-300 font-semibold leading-6 text-gray-900 hover:text-indigo-600 dark:hover:text-indigo-400 transition-colors group"
              >
                커뮤니티 보기
                <span aria-hidden="true" className="inline-block transition-transform group-hover:translate-x-1 ml-1">→</span>
              </a>
            </div>
          </div>
        </div>
        <div
          className="absolute inset-x-0 top-[calc(100%-13rem)] -z-10 transform-gpu overflow-hidden blur-3xl sm:top-[calc(100%-30rem)]"
          aria-hidden="true"
        >
          <div
            className="relative left-[calc(50%+3rem)] aspect-[1155/678] w-[36.125rem] -translate-x-1/2 bg-gradient-to-tr from-[#ff80b5] to-[#9089fc] opacity-30 sm:left-[calc(50%+36rem)] sm:w-[72.1875rem]"
            style={{
              clipPath:
                'polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)',
            }}
          />
        </div>
      </div>
    </div>
  )
}
