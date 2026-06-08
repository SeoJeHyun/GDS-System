// 기존 GDS-System 백엔드 전용 프론트 API 어댑터입니다.
// 백엔드가 같은 Spring Boot에서 정적 파일을 제공하면 ''로 바꿔도 됩니다.
const API_BASE_URL = 'http://223.130.133.117:8080';
console.log('[GDS FRONT] API_BASE_URL =', API_BASE_URL);

function getSession() {
  return {
    sessionId: localStorage.getItem('gdsSessionId') || '',
    token: localStorage.getItem('gdsAccessToken') || '',
    user: JSON.parse(localStorage.getItem('gdsUser') || 'null')
  };
}

function saveSessionFromLogin(response) {
  const data = response?.data;
  // 실제 GDS-System은 ApiResponseDTO<UserDTO> 형태로 user DTO만 반환할 수 있음.
  const user = data?.user || data;
  if (!user) return;
  const normalized = normalizeUser(user);
  localStorage.setItem('gdsUser', JSON.stringify(normalized));
  localStorage.setItem('gdsSessionId', data?.sessionId || 'local-session');
  localStorage.setItem('gdsAccessToken', data?.accessToken || 'local-token');
}

function logout() {
  localStorage.removeItem('gdsUser');
  localStorage.removeItem('gdsSessionId');
  localStorage.removeItem('gdsAccessToken');
  localStorage.removeItem('gdsCartView');
  location.href = './index.html';
}

function authHeaders() {
  const { sessionId, token } = getSession();
  const headers = { 'Content-Type': 'application/json' };
  if (sessionId) headers['Session-Id'] = sessionId;
  if (token) headers.Authorization = token.startsWith('Bearer ') ? token : `Bearer ${token}`;
  return headers;
}

function url(path) {
  return `${API_BASE_URL}${path}`;
}

async function request(path, options = {}) {
  const res = await fetch(url(path), {
    ...options,
    headers: { ...authHeaders(), ...(options.headers || {}) }
  });

  const contentType = res.headers.get('content-type') || '';
  if (!res.ok) {
    let msg = `${res.status} ${res.statusText}`;
    if (contentType.includes('application/json')) {
      const body = await res.json().catch(() => null);
      msg = body?.message || msg;
    } else {
      const text = await res.text().catch(() => '');
      if (text) msg = text.slice(0, 200);
    }
    throw new Error(msg);
  }

  if (contentType.includes('application/json')) return res.json();
  return res;
}

function normalizeUser(user) {
  return {
    id: user.id || user.userId || user.user_id || '',
    name: user.name || '',
    userType: user.userType || user.user_type || ''
  };
}

function normalizeGame(game) {
  const genre = Array.isArray(game.genre) ? game.genre : [game.genre].filter(Boolean);
  return {
    id: game.id || game.gameId || game.game_id || '',
    title: game.title || '',
    developer: game.developer || game.developerName || game.developer_name || '',
    genre,
    price: Number(game.price || 0),
    demo: game.demo || (game.demoAvailable || game.isDemoAvailable ? 'available' : 'none'),
    distributionStatus: game.distributionStatus || game.deploymentStatus || game.statusName || '',
    description: game.description || game.detail || '',
    fileSizeGb: game.fileSizeGb ?? game.fileSizeGB ?? 0,
    isDownloadable: Boolean(game.isDownloadable),
    downloadUrl: game.downloadUrl || ''
  };
}

function unwrapList(response) {
  if (Array.isArray(response)) return response;
  if (Array.isArray(response?.data)) return response.data;
  if (Array.isArray(response?.data?.data)) return response.data.data;
  return [];
}

function unwrapStatus(response) {
  return response?.status || 'success';
}

function unwrapMessage(response, fallback = '') {
  return response?.message || fallback;
}

const api = {
  async login(userId, password) {
    const res = await request('/api/login', {
      method: 'POST',
      body: JSON.stringify({ userId, password })
    });
    if (unwrapStatus(res) !== 'success') throw new Error(unwrapMessage(res, '로그인 실패'));
    saveSessionFromLogin(res);
    return res;
  },

  async signup(userId, password, name) {
    return request('/api/register', {
      method: 'POST',
      body: JSON.stringify({ userId, password, name })
    });
  },

  async getGames() {
    const res = await request('/api/games');
    return unwrapList(res).map(normalizeGame);
  },

  async searchGames(keyword) {
    const q = encodeURIComponent(keyword || '');
    const res = await request(`/api/games/search?keyword=${q}`);
    return unwrapList(res).map(normalizeGame);
  },

  async getGameDetail(gameId) {
    const res = await request(`/api/games/${encodeURIComponent(gameId)}`);
    return normalizeGame(res?.data || res);
  },

  async getCart() {
    const { user } = getSession();
    if (!user || !user.id) throw new Error('로그인이 필요합니다.');

    // 백엔드 명세에 맞게 쿼리 파라미터로 userId 전달
    const res = await request(`/api/cart?userId=${encodeURIComponent(user.id)}`);
    return unwrapList(res).map(normalizeGame);
  },

  async addCart(gameId) {
    const { user } = getSession();
    if (!user || !user.id) throw new Error('로그인이 필요합니다.');

    const res = await request('/api/cart', {
      method: 'POST',
      body: JSON.stringify({ userId: user.id, gameId })
    });
    return res;
  },

  async deleteCart(gameId) {
    const { user } = getSession();
    if (!user || !user.id) throw new Error('로그인이 필요합니다.');

    // 백엔드 명세에 맞게 쿼리 파라미터로 userId 전달
    return request(`/api/cart/${encodeURIComponent(gameId)}?userId=${encodeURIComponent(user.id)}`, { method: 'DELETE' });
  },

  async purchase(amount = 0) {
    const { user } = getSession();
    if (!user || !user.id) throw new Error('로그인이 필요합니다.');

    return request('/api/purchase', {
      method: 'POST',
      body: JSON.stringify({
        userId: user.id,
        paymentKey: `payment_${Date.now()}`,
        orderId: `order_${Date.now()}`,
        amount
      })
    });
  },

  async getLibrary(userId, page = 1, size = 100) {
    const res = await request(`/api/users/${encodeURIComponent(userId)}/library?page=${page}&size=${size}`);
    return {
      ...res,
      data: unwrapList(res).map(normalizeGame)
    };
  },

  downloadUrl(userId, gameId) {
    return url(`/api/users/${encodeURIComponent(userId)}/library/${encodeURIComponent(gameId)}/download`);
  }
};

window.api = api;
window.getSession = getSession;
window.logout = logout;
window.normalizeGame = normalizeGame;