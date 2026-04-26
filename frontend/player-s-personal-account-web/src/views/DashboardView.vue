<template>
  <div class="dashboard">
    <aside class="sidebar">
      <div class="user-mini">
        <img
          :src="getFullImageUrl(user.avatarUrl)"
          :key="user.avatarUrl"
          :alt="user.nickname"
          class="avatar-small"
          @error="handleImageError"
        />
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
          <img :src="tab.icon" :alt="tab.name" class="tab-icon" />
          <span class="tab-name">{{ tab.name }}</span>
        </button>
      </nav>

      <button class="logout-btn" @click="handleLogout">Выйти</button>
    </aside>

    <main class="content">
      <div v-if="activeTab === 'profile'" class="tab-panel">
        <ProfileInfo :user="user" />
      </div>

      <div v-else-if="activeTab === 'stats'" class="tab-panel">
        <StatsPanel :stats="stats" :matches="rawMatches" :user="user" />
      </div>

      <div v-else-if="activeTab === 'achievements'" class="tab-panel">
        <AchievementsList
          :achievements="achievements"
          :key="achievements.length"
          :show-code="false"
        />
      </div>

      <div v-else-if="activeTab === 'matches'" class="tab-panel">
        <MatchHistory :matches="matches" />
      </div>

      <div v-else-if="activeTab === 'settings'" class="tab-panel">
        <ProfileSettings :user="user" @saved="onProfileSaved" />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import { achievementsApi } from '../api/achievements'
import api from '../api/api'
import ProfileInfo from '../components/ProfileInfo.vue'
import StatsPanel from '../components/StatsPanel.vue'
import AchievementsList from '../components/AchievementsList.vue'
import MatchHistory from '../components/MatchHistory.vue'
import ProfileSettings from '../components/ProfileSettings.vue'

import profileIcon from '../assets/profile.png'
import statsIcon from '../assets/statistics.png'
import achievementIcon from '../assets/achievement.png'
import matchesIcon from '../assets/battlefield.png'
import settingsIcon from '../assets/settings.png'
import userIcon from '../assets/user.png'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const API_BASE = 'http://localhost:8084'

const tabs = [
  { id: 'profile', name: 'Профиль', icon: profileIcon },
  { id: 'stats', name: 'Статистика', icon: statsIcon },
  { id: 'achievements', name: 'Достижения', icon: achievementIcon },
  { id: 'matches', name: 'Матчи', icon: matchesIcon },
  { id: 'settings', name: 'Настройки', icon: settingsIcon },
]

const activeTab = ref(route.query.tab || 'profile')
const user = ref({})
const stats = ref(null)
const achievements = ref([])
const matches = ref([])
const rawMatches = ref([])

const getFullImageUrl = (url) => {
  if (!url) return userIcon
  if (url.startsWith('http')) return url
  return `${API_BASE}${url}`
}

const handleImageError = (e) => {
  if (e.target.src.includes('user') || e.target.src.includes('')) {
    return
  }
  e.target.src = userIcon
}

onMounted(async () => {
  try {
    if (!authStore.isAuthenticated || !authStore.userId) {
      router.replace('/login')
      return
    }

    await authStore.fetchProfile()
    user.value = authStore.user || {}
    await loadTabData(activeTab.value)

  } catch (e) {
    if (e.response?.status === 401 || e.response?.status === 403) {
      authStore.logout()
      router.replace('/login')
      return
    }
    user.value = authStore.user || {}
  }
})

const loadTabData = async (tabId) => {
  if (!authStore.userId) return

  try {
    if (tabId === 'stats') {
      const res = await api.get(`/user-stats/${authStore.userId}`)
      stats.value = res.data || {
        matchesPlayed: 0, wins: 0, losses: 0, draws: 0,
        totalKills: 0, totalDeaths: 0, winRate: 0, kdRatio: 0
      }

      try {
        const matchesRes = await api.get(`/users/${authStore.userId}/history`)
        rawMatches.value = matchesRes.data || []
      } catch (e) {
        console.warn('⚠️ Не удалось загрузить историю для графика:', e)
        rawMatches.value = []
      }
    }

    if (tabId === 'achievements') {
      await fetchAchievements()
    }

    if (tabId === 'matches') {
      try {
        const res = await api.get(`/users/${authStore.userId}/history`)

        matches.value = (res.data || []).map(match => ({
          id: match.matchId || match.id,
          opponent: match.opponentNickname || 'Противник',
          opponentAvatar: match.opponentAvatarUrl || null,
          result: (match.result || 'LOSS').toLowerCase(),
          score: `${match.kills || 0}:${match.deaths || 0}`,
          date: match.playedAt ? new Date(match.playedAt).toLocaleDateString('ru-RU', {
            day: '2-digit', month: '2-digit', year: 'numeric'
          }) : 'Не указана',
          map: match.mapOrMode || 'Unknown'
        }))

        rawMatches.value = res.data || []

      } catch (e) {
        console.error('Ошибка загрузки матчей:', e)
      }
    }
  } catch (e) {
    console.error(`Ошибка загрузки вкладки ${tabId}:`, e)
  }
}

const handleTabChange = (tabId) => {
  activeTab.value = tabId
  router.replace({ query: { tab: tabId } })
  loadTabData(tabId)
}

const fetchAchievements = async () => {
  if (!authStore.userId) return

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
    achievements.value = []
  }
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

const onProfileSaved = (updatedUser) => {
  user.value = { ...user.value, ...updatedUser }
  authStore.user = updatedUser
  localStorage.setItem('user', JSON.stringify(updatedUser))
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
  width: 240px;
  background: #16213e;
  padding: 20px;
  display: flex;
  flex-direction: column;
  border-right: 2px solid #3a507a;
}

.user-mini {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 20px;
  border-bottom: 1px solid #3a507a;
  margin-bottom: 24px;
}

.avatar-small {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ff4d4d;
}

.nickname {
  font-weight: 600;
  font-size: 1.1rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 140px;
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
  color: #fff;
  padding: 12px 16px;
  text-align: left;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s;
  font-size: 1rem;
  display: flex;
  align-items: center;
  gap: 12px;
}

.tabs button:hover {
    background: #0f3460;
    color: #fff;
}

.tabs button.active {
    background: #ff4d4d;
    color: #fff;
}

.tab-icon {
  width: 24px;
  height: 24px;
  object-fit: contain;
  flex-shrink: 0;
}

.tab-name { white-space: nowrap; }

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

.logout-btn:hover { background: #ff4d4d; color: #fff; }

.content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}

.tab-panel { animation: fadeIn 0.3s ease; }

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>