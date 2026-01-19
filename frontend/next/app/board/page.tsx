"use client";

import { useEffect, useState } from 'react';
import Header from '@component/header/page';
import apiClient from '@/lib/axios';

interface Board {
  id: number;
  title: string;
  content: string;
  attachments: string[];
  viewCount: number;
  categoryName: string;
  userName: string;
}

interface BoardFormData {
  title: string;
  content: string;
  categoryId: number;
  attachments: string[];
}

export default function BoardMain() {
  const [boards, setBoards] = useState<Board[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedBoard, setSelectedBoard] = useState<Board | null>(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const [isWriteModalOpen, setIsWriteModalOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [formData, setFormData] = useState<BoardFormData>({
    title: '',
    content: '',
    categoryId: 1,
    attachments: []
  });

  // 게시글 목록 조회
  const fetchBoards = async () => {
    try {
      setLoading(true);
      const response = await apiClient.get('/api/v1/boards');
      setBoards(response.data.data || []);
    } catch (error) {
      console.error('게시글 조회 실패:', error);
      // 임시 목 데이터 (백엔드 미연동시)
      setBoards([
        {
          id: 1,
          title: '첫 번째 게시글입니다',
          content: '게시글 내용입니다.',
          attachments: [],
          viewCount: 42,
          categoryName: '공지사항',
          userName: '관리자'
        },
        {
          id: 2,
          title: '재정 계산기 사용법',
          content: '포트폴리오 페이지에서 재정 계획을 세울 수 있습니다.',
          attachments: [],
          viewCount: 128,
          categoryName: '가이드',
          userName: '운영자'
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

  // 게시글 상세 조회
  const viewBoardDetail = async (id: number) => {
    try {
      const response = await apiClient.get(`/api/v1/boards/${id}`);
      setSelectedBoard(response.data.data);
      setIsDetailModalOpen(true);
    } catch (error) {
      console.error('게시글 상세 조회 실패:', error);
      // 임시로 목록에서 찾기
      const board = boards.find(b => b.id === id);
      if (board) {
        setSelectedBoard(board);
        setIsDetailModalOpen(true);
      }
    }
  };

  // 게시글 작성
  const handleCreateBoard = async () => {
    console.log('=== 게시글 작성 시작 ===');
    console.log('폼 데이터:', formData);

    if (!formData.title.trim() || !formData.content.trim()) {
      alert('제목과 내용을 입력해주세요.');
      return;
    }

    try {
      console.log('API 요청 전송 중...');
      const response = await apiClient.post('/api/v1/boards', formData);
      console.log('API 응답:', response.data);
      alert('게시글이 작성되었습니다.');
      setIsWriteModalOpen(false);
      resetForm();
      fetchBoards();
    } catch (error: any) {
      console.error('게시글 작성 실패:', error);
      console.error('에러 응답:', error.response?.data);
      alert(`게시글 작성에 실패했습니다: ${error.response?.data?.message || error.message}`);
    }
  };

  // 게시글 수정
  const handleUpdateBoard = async () => {
    if (!selectedBoard || !formData.title.trim() || !formData.content.trim()) {
      alert('제목과 내용을 입력해주세요.');
      return;
    }

    try {
      await apiClient.put(`/api/v1/boards/${selectedBoard.id}`, formData);
      alert('게시글이 수정되었습니다.');
      setIsWriteModalOpen(false);
      setIsEditMode(false);
      resetForm();
      fetchBoards();
    } catch (error) {
      console.error('게시글 수정 실패:', error);
      alert('게시글 수정에 실패했습니다.');
    }
  };

  // 게시글 삭제
  const handleDeleteBoard = async (id: number) => {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    try {
      await apiClient.delete(`/api/v1/boards/${id}`);
      alert('게시글이 삭제되었습니다.');
      setIsDetailModalOpen(false);
      fetchBoards();
    } catch (error) {
      console.error('게시글 삭제 실패:', error);
      alert('게시글 삭제에 실패했습니다.');
    }
  };

  // 폼 초기화
  const resetForm = () => {
    setFormData({
      title: '',
      content: '',
      categoryId: 1,
      attachments: []
    });
  };

  // 수정 모드로 전환
  const openEditMode = (board: Board) => {
    setFormData({
      title: board.title,
      content: board.content,
      categoryId: 1,
      attachments: board.attachments
    });
    setIsEditMode(true);
    setIsDetailModalOpen(false);
    setIsWriteModalOpen(true);
  };

  // 검색 필터링
  const filteredBoards = boards.filter(board =>
    board.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
    board.content.toLowerCase().includes(searchTerm.toLowerCase())
  );

  useEffect(() => {
    fetchBoards();
  }, []);

  return (
    <div className="bg-gray-50 dark:bg-gray-900 min-h-screen">
      <Header />

      <div className="mx-auto max-w-7xl pt-24 pb-8 px-4 sm:px-6 lg:px-8">
        {/* 페이지 헤더 */}
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-2">
              게시판
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              자유롭게 소통하고 정보를 공유하세요
            </p>
          </div>
          <button
            onClick={() => {
              resetForm();
              setIsEditMode(false);
              setIsWriteModalOpen(true);
            }}
            className="bg-indigo-600 hover:bg-indigo-500 text-white font-semibold py-3 px-6 rounded-lg shadow-sm transition-colors"
          >
            글쓰기
          </button>
        </div>

        {/* 검색 바 */}
        <div className="mb-6">
          <div className="relative">
            <input
              type="text"
              placeholder="제목 또는 내용으로 검색..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full px-4 py-3 pl-12 bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            />
            <svg
              className="absolute left-4 top-3.5 w-5 h-5 text-gray-400"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
        </div>

        {/* 게시글 목록 */}
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg overflow-hidden">
          {loading ? (
            <div className="flex justify-center items-center h-64">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
            </div>
          ) : filteredBoards.length === 0 ? (
            <div className="flex flex-col items-center justify-center h-64 text-gray-400 dark:text-gray-500">
              <svg className="w-16 h-16 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              <p>게시글이 없습니다</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50 dark:bg-gray-700">
                  <tr>
                    <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      번호
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      카테고리
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      제목
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      작성자
                    </th>
                    <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                      조회수
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200 dark:divide-gray-700">
                  {filteredBoards.map((board) => (
                    <tr
                      key={board.id}
                      onClick={() => viewBoardDetail(board.id)}
                      className="hover:bg-gray-50 dark:hover:bg-gray-700 cursor-pointer transition"
                    >
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                        {board.id}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className="px-2 py-1 text-xs font-medium rounded-full bg-indigo-100 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-200">
                          {board.categoryName}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-900 dark:text-white">
                        <div className="flex items-center">
                          {board.title}
                          {board.attachments && board.attachments.length > 0 && (
                            <svg className="w-4 h-4 ml-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13" />
                            </svg>
                          )}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600 dark:text-gray-400">
                        {board.userName}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600 dark:text-gray-400">
                        {board.viewCount}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* 상세보기 모달 */}
      {isDetailModalOpen && selectedBoard && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 p-4">
          <div className="bg-white dark:bg-gray-800 rounded-xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
            {/* 모달 헤더 */}
            <div className="sticky top-0 bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 px-6 py-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
                {selectedBoard.title}
              </h2>
              <button
                onClick={() => setIsDetailModalOpen(false)}
                className="text-gray-400 hover:text-gray-600 dark:hover:text-gray-200"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            {/* 모달 본문 */}
            <div className="px-6 py-4">
              <div className="flex items-center justify-between mb-4 text-sm text-gray-600 dark:text-gray-400">
                <div className="flex items-center space-x-4">
                  <span className="font-medium">{selectedBoard.userName}</span>
                  <span className="px-2 py-1 text-xs rounded-full bg-indigo-100 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-200">
                    {selectedBoard.categoryName}
                  </span>
                </div>
                <div className="flex items-center space-x-2">
                  <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                  <span>{selectedBoard.viewCount}</span>
                </div>
              </div>

              <div className="prose dark:prose-invert max-w-none mb-6">
                <p className="text-gray-700 dark:text-gray-300 whitespace-pre-wrap">
                  {selectedBoard.content}
                </p>
              </div>

              {selectedBoard.attachments && selectedBoard.attachments.length > 0 && (
                <div className="border-t border-gray-200 dark:border-gray-700 pt-4">
                  <h3 className="text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">첨부파일</h3>
                  <ul className="space-y-2">
                    {selectedBoard.attachments.map((attachment, index) => (
                      <li key={index} className="flex items-center text-sm text-indigo-600 dark:text-indigo-400">
                        <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13" />
                        </svg>
                        {attachment}
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>

            {/* 모달 푸터 */}
            <div className="border-t border-gray-200 dark:border-gray-700 px-6 py-4 flex justify-end space-x-3">
              <button
                onClick={() => openEditMode(selectedBoard)}
                className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
              >
                수정
              </button>
              <button
                onClick={() => handleDeleteBoard(selectedBoard.id)}
                className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition"
              >
                삭제
              </button>
              <button
                onClick={() => setIsDetailModalOpen(false)}
                className="px-4 py-2 bg-gray-300 dark:bg-gray-600 text-gray-700 dark:text-gray-200 rounded-lg hover:bg-gray-400 dark:hover:bg-gray-500 transition"
              >
                닫기
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 작성/수정 모달 */}
      {isWriteModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 p-4">
          <div className="bg-white dark:bg-gray-800 rounded-xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
            {/* 모달 헤더 */}
            <div className="sticky top-0 bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 px-6 py-4 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
                {isEditMode ? '게시글 수정' : '게시글 작성'}
              </h2>
              <button
                onClick={() => {
                  setIsWriteModalOpen(false);
                  setIsEditMode(false);
                  resetForm();
                }}
                className="text-gray-400 hover:text-gray-600 dark:hover:text-gray-200"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            {/* 모달 본문 */}
            <div className="px-6 py-4 space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  제목 <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={formData.title}
                  onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                  placeholder="제목을 입력하세요"
                  className="w-full px-4 py-3 bg-gray-50 dark:bg-gray-700 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  내용 <span className="text-red-500">*</span>
                </label>
                <textarea
                  value={formData.content}
                  onChange={(e) => setFormData({ ...formData, content: e.target.value })}
                  placeholder="내용을 입력하세요"
                  rows={12}
                  className="w-full px-4 py-3 bg-gray-50 dark:bg-gray-700 border border-gray-300 dark:border-gray-600 rounded-lg text-gray-900 dark:text-white placeholder-gray-400 focus:ring-2 focus:ring-indigo-500 focus:border-transparent resize-none"
                />
              </div>
            </div>

            {/* 모달 푸터 */}
            <div className="border-t border-gray-200 dark:border-gray-700 px-6 py-4 flex justify-end space-x-3">
              <button
                onClick={() => {
                  setIsWriteModalOpen(false);
                  setIsEditMode(false);
                  resetForm();
                }}
                className="px-4 py-2 bg-gray-300 dark:bg-gray-600 text-gray-700 dark:text-gray-200 rounded-lg hover:bg-gray-400 dark:hover:bg-gray-500 transition"
              >
                취소
              </button>
              <button
                onClick={isEditMode ? handleUpdateBoard : handleCreateBoard}
                className="px-6 py-2 bg-indigo-600 hover:bg-indigo-500 text-white rounded-lg transition-colors"
              >
                {isEditMode ? '수정하기' : '작성하기'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
