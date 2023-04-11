<template>
  <div class="memberMenu">
    <ul>
      <li @click="goTo('/orderhistory')">주문 내역</li>
      <li @click="goTo('/addressbook')">배송지 관리</li>
      <li>내 정보 관리</li>
      <li @click="logout">로그아웃</li>
    </ul>
  </div>
</template>

<script>
export default {
  methods: {
    goTo (path) {
      this.$router.push(path)
      this.$emit('hideMenu')
    },
    logout () {
      this.$store.commit('setAccessToken', '')
      this.$store.commit('setPrincipal', null)
      this.goTo('/login')
    }
  }
}
</script>

<style scoped>
.memberMenu {
  position: fixed;
  z-index: 2;
  width: 27rem;
  height: 44rem;
  background-color: #f6f6f6;
}

.memberMenu > ul {
  margin: 0;
  padding: 0;
  list-style-type: none;
}

.memberMenu > ul > li {
  padding: 1rem;
  border: 1px solid #ddd;
  cursor: pointer;
}

.memberMenu > ul > li:hover {
  background-color: aliceblue;
}
</style>
