'use client';

import { useEffect } from 'react';
import Link from 'next/link';

export default function Error({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  useEffect(() => {
    console.error(error);
  }, [error]);

  // 에러 메시지에서 상태 코드 추출 시도
  const is403 = error.message.includes('403') || error.message.includes('Forbidden');
  const is500 = error.message.includes('500') || error.message.includes('Internal Server');

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 flex items-center justify-center px-4">
      <div className="max-w-md w-full text-center">
        <div className="mb-8">
          {is403 ? (
            <>
              <h1 className="text-9xl font-bold text-red-600 dark:text-red-400">403</h1>
              <div className="mt-4">
                <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
                  접근 권한이 없습니다
                </h2>
                <p className="text-gray-600 dark:text-gray-400 mb-8">
                  이 페이지에 접근할 권한이 없습니다. 로그인이 필요하거나 권한이 부족할 수 있습니다.
                </p>
              </div>
            </>
          ) : is500 ? (
            <>
              <h1 className="text-9xl font-bold text-yellow-600 dark:text-yellow-400">500</h1>
              <div className="mt-4">
                <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
                  서버 오류
                </h2>
                <p className="text-gray-600 dark:text-gray-400 mb-8">
                  서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.
                </p>
              </div>
            </>
          ) : (
            <>
              <div className="text-9xl mb-4">⚠️</div>
              <div className="mt-4">
                <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">
                  문제가 발생했습니다
                </h2>
                <p className="text-gray-600 dark:text-gray-400 mb-8">
                  예상치 못한 오류가 발생했습니다. 다시 시도해주세요.
                </p>
              </div>
            </>
          )}
        </div>

        <div className="space-y-4">
          <button
            onClick={reset}
            className="block w-full px-6 py-3 bg-indigo-600 text-white font-semibold rounded-lg hover:bg-indigo-500 transition-colors"
          >
            다시 시도
          </button>
          {is403 && (
            <Link
              href="/login"
              className="block w-full px-6 py-3 bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-300 font-semibold rounded-lg hover:bg-gray-300 dark:hover:bg-gray-600 transition-colors"
            >
              로그인 하기
            </Link>
          )}
          <Link
            href="/"
            className="block w-full px-6 py-3 bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-300 font-semibold rounded-lg hover:bg-gray-300 dark:hover:bg-gray-600 transition-colors"
          >
            홈으로 돌아가기
          </Link>
        </div>

        {error.message && (
          <div className="mt-8 p-4 bg-red-50 dark:bg-red-900/20 rounded-lg border border-red-200 dark:border-red-800">
            <p className="text-sm text-red-800 dark:text-red-300 font-mono">
              {error.message}
            </p>
          </div>
        )}

        <div className="mt-8">
          <p className="text-sm text-gray-500 dark:text-gray-400">
            문제가 계속되면{' '}
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
