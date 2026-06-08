const DEFAULT_API_BASE_URL = 'http://223.130.133.117:8080';
const API_BASE_STORAGE_KEY = 'gdsApiBaseUrl';
const USER_STORAGE_KEY = 'gdsUser';

function getApiBaseUrl() {
  return (localStorage.getItem(API_BASE_STORAGE_KEY) || DEFAULT_API_BASE_URL).replace(/\/$/, '');
}

function setApiBaseUrl(value) {
  const normalized = (value || DEFAULT_API_BASE_URL).trim().replace(/\/$/, '');
  localStorage.setItem(API_BASE_STORAGE_KEY, normalized);
  return normalized;
}

function url(path) {
  return `${getApiBaseUrl()}${path}`;
}

function getSession() {
  return {
    user: JSON.parse(localStorage.getItem(USER_STORAGE_KEY) || 'null')
  };
}

function saveSessionFromLogin(response) {
  const data = response?.data;
  if (!data) return null;

  const user = normalizeUser(data);
  localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(user));
  return user;
}

function logout() {
  localStorage.removeItem(USER_STORAGE_KEY);
}

function authHeaders() {
  return {
    'Content-Type': 'application/json'
  };
}

async function request(path, options = {}) {
  let response;

  try {
    response = await fetch(url(path), {
      ...options,
      headers: {
        ...authHeaders(),
        ...(options.headers || {})
      }
    });
  } catch (error) {
    throw new Error('서버에 연결할 수 없습니다. 백엔드 실행 상태, API 주소, CORS 설정을 확인하세요.');
  }

  const contentType = response.headers.get('content-type') || '';

  if (!response.ok) {
    const errorMessage = await readErrorMessage(response, contentType);
    throw new Error(errorMessage);
  }

  if (contentType.includes('application/json')) {
    return response.json();
  }

  return response;
}

async function readErrorMessage(response, contentType) {
  if (contentType.includes('application/json')) {
    const body = await response.json().catch(() => null);
    return body?.message || `${response.status} ${response.statusText}`;
  }

  const text = await response.text().catch(() => '');
  return text || `${response.status} ${response.statusText}`;
}

function assertSuccess(response, fallbackMessage) {
  if (response?.status && response.status !== 'success') {
    throw new Error(response.message || fallbackMessage || '요청 실패');
  }
  return response;
}

function unwrapList(response) {
  if (Array.isArray(response)) return response;
  if (Array.isArray(response?.data)) return response.data;
  return [];
}

function normalizeUser(user) {
  return {
    userId: user.userId || user.id || user.user_id || '',
    name: user.name || '',
    userType: user.userType || user.user_type || ''
  };
}

function normalizeGame(game) {
  return {
    gameId: game.gameId || game.id || game.game_id || '',
    title: game.title || '',
    developerName: game.developerName || game.developer || game.developer_name || '',
    price: Number(game.price || 0),
    genre: game.genre || '',
    distributionStatus: game.distributionStatus || game.deploymentStatus || game.statusName || '',
    detail: game.detail || game.description || '',
    ageRating: game.ageRating ?? game.age_rating ?? '',
    demoAvailable: Boolean(game.demoAvailable ?? game.demo_available ?? false)
  };
}

function requireLogin() {
  const { user } = getSession();
  if (!user?.userId) {
    throw new Error('로그인이 필요합니다.');
  }
  return user;
}

const api = {
  getApiBaseUrl,
  setApiBaseUrl,

  async login(userId, password) {
    const response = await request('/api/login', {
      method: 'POST',
      body: JSON.stringify({ userId, password })
    });

    assertSuccess(response, '로그인 실패');
    const user = saveSessionFromLogin(response);
    return { response, user };
  },

  async register(userId, password, name) {
    const response = await request('/api/register', {
      method: 'POST',
      body: JSON.stringify({ userId, password, name })
    });

    return assertSuccess(response, '회원가입 실패');
  },

  async getGames() {
    const response = await request('/api/games');
    assertSuccess(response, '게임 목록 조회 실패');
    return unwrapList(response).map(normalizeGame);
  },

  async searchGames(keyword) {
    const response = await request(`/api/games/search?keyword=${encodeURIComponent(keyword)}`);
    assertSuccess(response, '게임 검색 실패');
    return unwrapList(response).map(normalizeGame);
  },

  async getGameDetail(gameId) {
    const response = await request(`/api/games/${encodeURIComponent(gameId)}`);
    assertSuccess(response, '게임 상세 조회 실패');
    return normalizeGame(response.data || {});
  },

  async getCart() {
    const user = requireLogin();
    const response = await request(`/api/cart?userId=${encodeURIComponent(user.userId)}`);
    assertSuccess(response, '장바구니 조회 실패');
    return unwrapList(response).map(normalizeGame);
  },

  async addCart(gameId) {
    const user = requireLogin();
    const response = await request('/api/cart', {
      method: 'POST',
      body: JSON.stringify({ userId: user.userId, gameId })
    });

    return assertSuccess(response, '장바구니 추가 실패');
  },

  async deleteCart(gameId) {
    const user = requireLogin();
    const response = await request(`/api/cart/${encodeURIComponent(gameId)}?userId=${encodeURIComponent(user.userId)}`, {
      method: 'DELETE'
    });

    return assertSuccess(response, '장바구니 삭제 실패');
  },

  async purchase({ paymentKey, orderId, amount }) {
    const user = requireLogin();
    const response = await request('/api/purchase', {
      method: 'POST',
      body: JSON.stringify({
        userId: user.userId,
        paymentKey,
        orderId,
        amount: Number(amount || 0)
      })
    });

    return assertSuccess(response, '결제 처리 실패');
  },

  async getLibrary(page = 1, size = 100) {
    const user = requireLogin();
    const response = await request(`/api/users/${encodeURIComponent(user.userId)}/library?page=${page}&size=${size}`);
    assertSuccess(response, '라이브러리 조회 실패');
    return unwrapList(response).map(normalizeGame);
  },

  getDownloadUrl(gameId) {
    const user = requireLogin();
    return url(`/api/users/${encodeURIComponent(user.userId)}/library/${encodeURIComponent(gameId)}/download`);
  }
};

window.api = api;
window.getSession = getSession;
window.logout = logout;
window.normalizeGame = normalizeGame;