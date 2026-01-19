import axios from 'axios';

// Axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: 'http://localhost:29929',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터: 모든 요청에 JWT 토큰 자동 추가
apiClient.interceptors.request.use(
  (config) => {
    // 로그인/회원가입 요청은 토큰 불필요
    const noAuthUrls = ['/api/v1/signin', '/api/v1/signup'];
    const isNoAuthUrl = noAuthUrls.some(url => config.url?.includes(url));

    if (!isNoAuthUrl) {
      const token = localStorage.getItem('accessToken');
      if (token && token.trim().length > 0) {
        // Bearer 없이 토큰만 전송 (백엔드가 Authorization 헤더 전체를 토큰으로 사용)
        config.headers.Authorization = token.trim();
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터: 에러 처리
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // 토큰이 만료되거나 유효하지 않은 경우
      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');

      // 로그인 페이지로 리다이렉트 (현재 페이지가 로그인 페이지가 아닌 경우에만)
      if (typeof window !== 'undefined' && window.location.pathname !== '/login') {
        window.location.href = '/login?message=로그인이 필요합니다';
      }
    } else if (error.response?.status === 403) {
      // 접근 권한이 없는 경우 - 전용 페이지로 리다이렉트
      if (typeof window !== 'undefined' && window.location.pathname !== '/forbidden') {
        window.location.href = '/forbidden';
      }
    } else if (error.response?.status === 404) {
      // 리소스를 찾을 수 없는 경우 - API 404는 로그만 남김 (페이지 404는 Next.js가 처리)
      console.error('요청한 리소스를 찾을 수 없습니다:', error.config?.url);
    } else if (error.response?.status >= 500) {
      // 서버 오류 - 전용 페이지로 리다이렉트
      if (typeof window !== 'undefined' && window.location.pathname !== '/server-error') {
        window.location.href = '/server-error';
      }
    }
    return Promise.reject(error);
  }
);

export default apiClient;
