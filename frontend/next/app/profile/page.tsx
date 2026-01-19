"use client";

import { useState, useEffect } from 'react';
import Header from '@component/header/page';
import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import apiClient from '@/lib/axios';

interface UserProfile {
  username: string;
  name: string;
  email?: string;
  createdAt?: string;
}

export default function ProfilePage() {
  const { user, isAuthenticated } = useAuth();
  const router = useRouter();
  const [isEditing, setIsEditing] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [profile, setProfile] = useState<UserProfile>({
    username: '',
    name: '',
    email: '',
  });
  const [editedProfile, setEditedProfile] = useState<UserProfile>({
    username: '',
    name: '',
    email: '',
  });

  useEffect(() => {
    // localStorage에서 직접 확인
    const token = localStorage.getItem('accessToken');
    const storedUser = localStorage.getItem('user');

    if (!token || !storedUser) {
      router.push('/login');
      return;
    }

    try {
      const userData = JSON.parse(storedUser);
      setProfile({
        username: userData.username || '',
        name: userData.name || '',
        email: userData.email || '',
        createdAt: userData.createdAt || new Date().toISOString(),
      });
      setEditedProfile({
        username: userData.username || '',
        name: userData.name || '',
        email: userData.email || '',
      });
    } catch (error) {
      console.error('사용자 정보 파싱 실패:', error);
      router.push('/login');
    }
  }, [router]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setEditedProfile(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSave = async () => {
    setIsLoading(true);
    try {
      // TODO: API 엔드포인트로 프로필 업데이트 요청
      // const response = await apiClient.put('/api/v1/users/profile', editedProfile);

      // 임시로 localStorage 업데이트
      const updatedUser = {
        ...profile,
        ...editedProfile,
      };
      localStorage.setItem('user', JSON.stringify(updatedUser));
      setProfile(updatedUser);
      setIsEditing(false);
      alert('프로필이 수정되었습니다!');
    } catch (error) {
      console.error('프로필 수정 실패:', error);
      alert('프로필 수정에 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancel = () => {
    setEditedProfile(profile);
    setIsEditing(false);
  };

  // 로딩 중이거나 토큰이 없으면 아무것도 렌더링하지 않음
  if (typeof window !== 'undefined') {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      return null;
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900">
      <Header />

      <div className="pt-24 pb-12 px-4 sm:px-6 lg:px-8">
        <div className="max-w-3xl mx-auto">
          {/* 헤더 */}
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
              내 프로필
            </h1>
            <p className="mt-2 text-sm text-gray-600 dark:text-gray-400">
              개인 정보를 관리하고 계정 설정을 변경하세요
            </p>
          </div>

          {/* 프로필 카드 */}
          <div className="bg-white dark:bg-gray-800 rounded-2xl shadow-lg overflow-hidden">
            {/* 프로필 헤더 */}
            <div className="bg-indigo-600 px-8 py-12">
              <div className="flex items-center gap-6">
                <div className="w-24 h-24 rounded-full bg-white dark:bg-gray-800 flex items-center justify-center shadow-xl">
                  <span className="text-4xl font-bold text-indigo-600">
                    {profile.username.charAt(0).toUpperCase()}
                  </span>
                </div>
                <div>
                  <h2 className="text-2xl font-bold text-white">{profile.username}</h2>
                  <p className="text-indigo-100 mt-1">{profile.email || '이메일 없음'}</p>
                  {profile.createdAt && (
                    <p className="text-indigo-200 text-sm mt-2">
                      가입일: {new Date(profile.createdAt).toLocaleDateString('ko-KR')}
                    </p>
                  )}
                </div>
              </div>
            </div>

            {/* 프로필 정보 */}
            <div className="px-8 py-8">
              <div className="flex justify-between items-center mb-6">
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                  개인 정보
                </h3>
                {!isEditing ? (
                  <button
                    onClick={() => setIsEditing(true)}
                    className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-500 transition-colors flex items-center gap-2"
                  >
                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                    </svg>
                    수정하기
                  </button>
                ) : (
                  <div className="flex gap-2">
                    <button
                      onClick={handleCancel}
                      disabled={isLoading}
                      className="px-4 py-2 bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-300 rounded-lg hover:bg-gray-300 dark:hover:bg-gray-600 transition-colors disabled:opacity-50"
                    >
                      취소
                    </button>
                    <button
                      onClick={handleSave}
                      disabled={isLoading}
                      className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-500 transition-colors disabled:opacity-50 flex items-center gap-2"
                    >
                      {isLoading ? (
                        <>
                          <svg className="animate-spin h-4 w-4" fill="none" viewBox="0 0 24 24">
                            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                          </svg>
                          저장 중...
                        </>
                      ) : (
                        '저장하기'
                      )}
                    </button>
                  </div>
                )}
              </div>

              <div className="space-y-6">
                {/* 아이디 (수정 불가) */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    아이디
                  </label>
                  <input
                    type="text"
                    value={profile.username}
                    disabled
                    className="w-full px-4 py-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-gray-100 dark:bg-gray-700 text-gray-500 dark:text-gray-400 cursor-not-allowed"
                  />
                  <p className="mt-1 text-xs text-gray-500 dark:text-gray-400">
                    아이디는 변경할 수 없습니다
                  </p>
                </div>

                {/* 이름 */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    이름
                  </label>
                  <input
                    type="text"
                    name="name"
                    value={isEditing ? editedProfile.name : profile.name}
                    onChange={handleInputChange}
                    disabled={!isEditing}
                    className={`w-full px-4 py-3 rounded-lg border ${
                      isEditing
                        ? 'border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-gray-900 dark:text-white focus:ring-2 focus:ring-indigo-500 focus:border-transparent'
                        : 'border-gray-300 dark:border-gray-600 bg-gray-50 dark:bg-gray-700 text-gray-700 dark:text-gray-300'
                    } transition-all`}
                    placeholder="이름을 입력하세요"
                  />
                </div>

                {/* 이메일 */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                    이메일
                  </label>
                  <input
                    type="email"
                    name="email"
                    value={isEditing ? editedProfile.email : profile.email}
                    onChange={handleInputChange}
                    disabled={!isEditing}
                    className={`w-full px-4 py-3 rounded-lg border ${
                      isEditing
                        ? 'border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-800 text-gray-900 dark:text-white focus:ring-2 focus:ring-indigo-500 focus:border-transparent'
                        : 'border-gray-300 dark:border-gray-600 bg-gray-50 dark:bg-gray-700 text-gray-700 dark:text-gray-300'
                    } transition-all`}
                    placeholder="이메일을 입력하세요"
                  />
                </div>
              </div>
            </div>
          </div>

          {/* 추가 정보 카드 */}
          <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="bg-white dark:bg-gray-800 rounded-xl shadow p-6">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-full bg-indigo-100 dark:bg-indigo-900 flex items-center justify-center">
                  <svg className="w-6 h-6 text-indigo-600 dark:text-indigo-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                  </svg>
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">작성한 게시글</p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-white">0</p>
                </div>
              </div>
            </div>

            <div className="bg-white dark:bg-gray-800 rounded-xl shadow p-6">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-full bg-purple-100 dark:bg-purple-900 flex items-center justify-center">
                  <svg className="w-6 h-6 text-purple-600 dark:text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path>
                  </svg>
                </div>
                <div>
                  <p className="text-sm text-gray-600 dark:text-gray-400">작성한 댓글</p>
                  <p className="text-2xl font-bold text-gray-900 dark:text-white">0</p>
                </div>
              </div>
            </div>
          </div>

          {/* 위험 구역 */}
          <div className="mt-8 bg-red-50 dark:bg-red-900/20 rounded-xl p-6 border border-red-200 dark:border-red-800">
            <h3 className="text-lg font-semibold text-red-900 dark:text-red-300 mb-2">
              위험 구역
            </h3>
            <p className="text-sm text-red-700 dark:text-red-400 mb-4">
              아래 작업은 되돌릴 수 없습니다. 신중하게 진행하세요.
            </p>
            <button
              onClick={() => {
                if (confirm('정말로 계정을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
                  alert('계정 삭제 기능은 아직 구현되지 않았습니다.');
                }
              }}
              className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
            >
              계정 삭제
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
