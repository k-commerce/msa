export default {
  state: {
    accessToken: '',
    principal: null
  },
  getters: {
    getAccessToken: state => state.accessToken,
    getPrincipal: state => state.principal
  },
  mutations: {
    setAccessToken (state, accessToken) { state.accessToken = accessToken },
    setPrincipal (state, principal) { state.principal = principal }
  }
}
