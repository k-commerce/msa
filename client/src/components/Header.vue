<template>
  <header>
    <i class="fas fa-bars" @click="showMenu(1)" />
    <i class="fas fa-home" @click="goHome" />
    <span>
      <input type="text" v-model="name" />
      <i class="fas fa-search" @click="search" />
    </span>
    <i class="fas fa-shopping-cart" />
    <i class="fas fa-user" @click="showMenu(2)" />
  </header>
  <CategoryMenu v-show="menu === 1" @hideMenu="hideMenu" />
  <MemberMenu v-show="menu === 2" @hideMenu="hideMenu" />
</template>

<script>
import CategoryMenu from '@/components/CategoryMenu'
import MemberMenu from '@/components/MemberMenu'

export default {
  components: {
    CategoryMenu,
    MemberMenu
  },
  data () {
    return {
      menu: 0,
      name: ''
    }
  },
  computed: {
    isAuthenticated () {
      return this.$store.getters.getAccessToken && this.$store.getters.getPrincipal
    }
  },
  methods: {
    showMenu (menu) {
      if (menu === 2 && !this.isAuthenticated) {
        let redirect = ''
        if (this.$route.name === 'login') {
          redirect = this.$route.fullPath
        } else if (this.$route.name === 'join') {
          redirect = '/login'
        } else {
          redirect = '/login?redirect=' + this.$route.fullPath
        }
        this.$router.push(redirect)
        this.menu = 0
        return
      }
      this.menu = this.menu === menu ? 0 : menu
    },
    hideMenu () {
      this.menu = 0
    },
    goHome () {
      this.$router.push('/')
      this.menu = 0
    },
    search () {
      this.$router.push({ name: 'itemList', query: { name: this.name } })
    }
  }
}
</script>

<style scoped>
header {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 4rem;
  font-size: 2rem;
  color: lightgray;
}

header > i:hover,
header > span > i:hover {
  color: gray;
}

header > span {
  display: flex;
  justify-content: space-between;
  padding: 0.2rem 0.6rem 0.2rem 1rem;
  border: 1px solid lightgray;
  border-radius: 2rem;
}

header > span > input {
  width: 10rem;
  border: none;
  outline: none;
}
</style>
