'use client';

import Link from 'next/link';

export default function ForbiddenPage() {
  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 flex items-center justify-center px-4">
      <div className="max-w-md w-full text-center">
        <div className="mb-8">
          <h1 className="text-9xl font-bold text-red-600 dark:text-red-400">403</h1>
          <div className="mt-4">
            <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
              접근 권한이 없습니다
            </h2>
            <p className="text-gray-600 dark:text-gray-400 mb-8">
              이 페이지에 접근할 권한이 없습니다. 로그인이 필요하거나 관리자 권한이 필요할 수 있습니다.
            </p>
          </div>
        </div>

        <div className="space-y-4">
          <Link
            href="/login"
            className="block w-full px-6 py-3 bg-indigo-600 text-white font-semibold rounded-lg hover:bg-indigo-500 transition-colors"
          >
            로그인하기
          </Link>
          <Link
            href="/"
            className="block w-full px-6 py-3 bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-300 font-semibold rounded-lg hover:bg-gray-300 dark:hover:bg-gray-600 transition-colors"
          >
            홈으로 돌아가기
          </Link>
          <button
            onClick={() => window.history.back()}
            className="block w-full px-6 py-3 bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-300 font-semibold rounded-lg hover:bg-gray-300 dark:hover:bg-gray-600 transition-colors"
          >
            이전 페이지로
          </button>
        </div>

        <div className="mt-12">
          <p className="text-sm text-gray-500 dark:text-gray-400">
            권한 문제가 계속되면{' '}
            <Link href="/board" className="text-indigo-600 dark:text-indigo-400 hover:underline">
              고객센터
            </Link>
            로 문의해주세요.
          </p>
        </div>
      </div>
    </div>
  );
}
