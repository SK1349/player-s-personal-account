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
          @click="activeTab = tab.id"
        >
          {{ tab.icon }} {{ tab.name }}
        </button>
      </nav>

      <button class="logout-btn" @click="handleLogout">🚪 Выйти</button>
    </aside>

    <main class="content">
      <!-- Вкладка: Основная информация -->
      <div v-if="activeTab === 'profile'" class="tab-panel">
        <ProfileInfo :user="user" />
      </div>

      <div v-else-if="activeTab === 'stats'" class="tab-panel">
        <StatsPanel :stats="mockStats" />
      </div>

      <div v-else-if="activeTab === 'achievements'" class="tab-panel">
        <AchievementsList :achievements="mockAchievements" />
      </div>

      <div v-else-if="activeTab === 'matches'" class="tab-panel">
        <MatchHistory :matches="mockMatches" />
      </div>

      <div v-else-if="activeTab === 'settings'" class="tab-panel">
        <ProfileSettings :user="user" @saved="onProfileSaved" />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
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

const mockStats = ref({
  wins: 24,
  losses: 8,
  draws: 2,
  kdr: 2.75,
  totalMatches: 34,
  winRate: 70.6
})

const mockAchievements = ref([
  { id: 1, code: 'FIRST_WIN', name: 'Первая победа', description: 'Выиграйте первый матч', iconUrl: '', unlockedAt: '2026-04-01' },
  { id: 2, code: 'SNIPER', name: 'Снайпер', description: '100 хедшотов подряд', iconUrl: '', unlockedAt: '2026-04-10' },
  { id: 3, code: 'VETERAN', name: 'Ветеран', description: '50 матчей в рейтинге', iconUrl: '', unlockedAt: null } // null = ещё не получено
])

const mockMatches = ref([
  { id: 1, opponent: 'Team Alpha', result: 'win', score: '16:12', date: '2026-04-18', map: 'Dust2' },
  { id: 2, opponent: 'ProGaming', result: 'loss', score: '14:16', date: '2026-04-17', map: 'Mirage' },
  { id: 3, opponent: 'Noobs United', result: 'win', score: '16:5', date: '2026-04-15', map: 'Inferno' }
])

onMounted(async () => {
  if (authStore.userId) {
    await authStore.fetchProfile()
    user.value = authStore.user || {}
  } else {
    user.value = { nickname: 'TestPlayer', level: 5, rating: 1500, avatarUrl: '' }
  }
})

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

const onProfileSaved = (updatedUser) => {
  user.value = { ...user.value, ...updatedUser }
}
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

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>