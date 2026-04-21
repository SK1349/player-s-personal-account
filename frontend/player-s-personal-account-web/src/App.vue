<template>
  <router-view />
</template>

<script setup>
</script>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  background: #1a1a2e;
  color: #ffffff;
  line-height: 1.6;
}

a {
  text-decoration: none;
  color: inherit;
}

button {
  font-family: inherit;
}

::-webkit-scrollbar {
  width: 8px;
}
::-webkit-scrollbar-track {
  background: #1a1a2e;
}
::-webkit-scrollbar-thumb {
  background: #0f3460;
  border-radius: 4px;
}
</style>
