import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'

axios.interceptors.request.use(
  function (config) {
    if (store.getters.getAccessToken) {
      config.headers.Authorization = 'Bearer ' + store.getters.getAccessToken
    }
    return config
  },
  function (error) {
    return Promise.reject(error)
  }
)

axios.interceptors.response.use(
  function (response) {
    if (response.headers.authorization) {
      store.commit('setAccessToken', response.headers.authorization)
    }
    return response
  },
  function (error) {
    if (error.response.status === 401) {
      alert('로그인이 필요한 요청입니다.')
      store.commit('setAccessToken', '')
      store.commit('setPrincipal', null)
      router.push('/login')
    } else {
      alert(error.response.data.message)
    }
    return Promise.reject(error)
  }
)

const app = createApp(App).use(store).use(router)

app.config.globalProperties.$axios = axios

app.mount('#app')
