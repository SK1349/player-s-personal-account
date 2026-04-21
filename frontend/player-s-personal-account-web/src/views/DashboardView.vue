<template>
  <div class="dashboard">
    <aside class="sidebar">
      <div class="user-mini">
        <img :src="user.avatarUrl || 'https://via.placeholder.com/80'" class="avatar-small" />
        <div>
          <div class="nickname">{{ user.nickname || 'Игрок' }}</div>
          <div class="level">Ур. {{ user.level || 1 }}</div>
        </div>
      </div>

      <nav class="tabs">
        <button
          v-for="tab in tabs"
          :key="tab.id"
          :class="{ active: activeTab === tab.id }"
          @click="handleTabChange(tab.id)"
        >
          {{ tab.icon }} {{ tab.name }}
        </button>
      </nav>

      <button class="logout-btn" @click="handleLogout">🚪 Выйти</button>
    </aside>

    <main class="content">
      <div v-if="activeTab === 'profile'" class="tab-panel">
        <ProfileInfo :user="user" />
      </div>

      <div v-else-if="activeTab === 'stats'" class="tab-panel">
        <StatsPanel :stats="stats" />
      </div>

      <div v-else-if="activeTab === 'achievements'" class="tab-panel">
        <div v-if="achievementsLoading" class="loading-state">
          <span class="loader">⏳</span> Загрузка достижений...
        </div>

        <div v-else-if="achievementsError" class="error-state">
          <span class="error-icon">⚠️</span>
          <p>{{ achievementsError }}</p>
          <button @click="fetchAchievements" class="retry-btn">Попробовать снова</button>
        </div>

        <AchievementsList
          v-else
          :achievements="achievements"
          :key="achievements.length"
          :show-code="false"
        />
      </div>

      <!-- история матчей -->
      <div v-else-if="activeTab === 'matches'" class="tab-panel">
        <MatchHistory :matches="matches" />
      </div>

      <!-- настройки -->
      <div v-else-if="activeTab === 'settings'" class="tab-panel">
        <ProfileSettings :user="user" @saved="onProfileSaved" />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import { achievementsApi } from '../api/achievements'
import api from '../api/api'
import ProfileInfo from '../components/ProfileInfo.vue'
import StatsPanel from '../components/StatsPanel.vue'
import AchievementsList from '../components/AchievementsList.vue'
import MatchHistory from '../components/MatchHistory.vue'
import ProfileSettings from '../components/ProfileSettings.vue'

const router = useRouter()
const authStore = useAuthStore()

const tabs = [
  { id: 'profile', name: 'Профиль', icon: '👤' },
  { id: 'stats', name: 'Статистика', icon: '📊' },
  { id: 'achievements', name: 'Достижения', icon: '🏆' },
  { id: 'matches', name: 'Матчи', icon: '⚔️' },
  { id: 'settings', name: 'Настройки', icon: '⚙️' },
]

const activeTab = ref('profile')
const user = ref({})

const stats = ref(null)
const achievements = ref([])
const matches = ref([])

const achievementsLoading = ref(false)
const achievementsError = ref(null)

const mockMatches = [
  { id: 1, opponent: 'Team Alpha', result: 'win', score: '16:12', date: '2026-04-18', map: 'Dust2' },
  { id: 2, opponent: 'ProGaming', result: 'loss', score: '14:16', date: '2026-04-17', map: 'Mirage' },
  { id: 3, opponent: 'Noobs United', result: 'win', score: '16:5', date: '2026-04-15', map: 'Inferno' }
]

onMounted(async () => {
  if (authStore.userId) {
    await authStore.fetchProfile()
    user.value = authStore.user || {}
    loadTabData(activeTab.value)
  } else {
    user.value = { nickname: 'TestPlayer', level: 5, rating: 1500, avatarUrl: '' }
  }
})

const loadTabData = async (tabId) => {
  if (!authStore.userId) return

  try {
    if (tabId === 'stats') {
      const res = await api.get(`/users/${authStore.userId}/stats`).catch(() => ({ data: null }))
      stats.value = res.data || { wins: 0, losses: 0, kdr: 0, totalMatches: 0, winRate: 0 }
    }
    if (tabId === 'achievements') {
      await fetchAchievements()
    }
    if (tabId === 'matches') {
      const res = await api.get(`/users/${authStore.userId}/matches`).catch(() => ({ data: mockMatches }))
      matches.value = res.data || mockMatches
    }
  } catch (e) {
    console.error(`Ошибка загрузки вкладки ${tabId}:`, e)
  }
}

const handleTabChange = (tabId) => {
  activeTab.value = tabId
  loadTabData(tabId)
}

const fetchAchievements = async () => {
  if (!authStore.userId) return

  achievementsLoading.value = true
  achievementsError.value = null

  try {
    const [playerRes, catalogRes] = await Promise.all([
      achievementsApi.getPlayerAchievements(authStore.userId),
      achievementsApi.getAllAchievements().catch(() => ({ data: [] }))
    ])

    const unlockedMap = new Map(
      playerRes.data.map(ach => [ach.achievementCode, ach.unlockedAt])
    )

    achievements.value = catalogRes.data.map(catalogItem => ({
      id: catalogItem.id,
      code: catalogItem.code,
      name: catalogItem.name,
      description: catalogItem.description,
      iconUrl: catalogItem.iconUrl,
      unlockedAt: unlockedMap.get(catalogItem.code) || null
    }))

  } catch (e) {
    console.error('Ошибка загрузки достижений:', e)
    achievementsError.value = e.response?.data?.error || 'Не удалось загрузить достижения'
    achievements.value = []
  } finally {
    achievementsLoading.value = false
  }
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

const onProfileSaved = (updatedUser) => {
  user.value = { ...user.value, ...updatedUser }
}

watch(
  () => activeTab.value,
  (newTab, oldTab) => {
    if (newTab !== oldTab) {
      loadTabData(newTab)
    }
  }
)
</script>

<style scoped>
.dashboard {
  display: flex;
  height: 100vh;
  background: #1a1a2e;
  color: #fff;
}

.sidebar {
  width: 220px;
  background: #16213e;
  padding: 20px;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #0f3460;
}

.user-mini {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 20px;
  border-bottom: 1px solid #0f3460;
  margin-bottom: 20px;
}

.avatar-small {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #e94560;
}

.nickname {
  font-weight: 600;
  font-size: 1.1rem;
}

.level {
  color: #888;
  font-size: 0.9rem;
}

.tabs {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tabs button {
  background: none;
  border: none;
  color: #ccc;
  padding: 12px 16px;
  text-align: left;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s;
  font-size: 1rem;
}

.tabs button:hover {
  background: #0f3460;
  color: #fff;
}

.tabs button.active {
  background: #e94560;
  color: #fff;
}

.logout-btn {
  background: none;
  border: 1px solid #ff4d4d;
  color: #ff4d4d;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  margin-top: auto;
  transition: all 0.2s;
}

.logout-btn:hover {
  background: #ff4d4d;
  color: #fff;
}

.content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}

.tab-panel {
  animation: fadeIn 0.3s ease;
}

.loading-state,
.error-state {
  text-align: center;
  padding: 40px;
  color: #aaa;
}

.error-state {
  color: #ff6b6b;
}

.retry-btn {
  margin-top: 16px;
  padding: 10px 24px;
  background: #e94560;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.retry-btn:hover {
  background: #d63850;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>