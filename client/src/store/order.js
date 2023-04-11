export default {
  state: {
    orderCheck: null
  },
  getters: {
    getOrderCheck: state => state.orderCheck
  },
  mutations: {
    setOrderCheck (state, orderCheck) { state.orderCheck = orderCheck }
  }
}
