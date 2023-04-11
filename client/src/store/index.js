import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import auth from '@/store/auth'
import order from '@/store/order'

export default createStore({
  modules: {
    auth,
    order
  },
  plugins: [
    createPersistedState()
  ]
})
