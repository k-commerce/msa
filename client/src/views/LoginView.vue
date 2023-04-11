<template>
  <main class="login">
    <input type="text" placeholder="아이디" v-model="username" />
    <input type="password" placeholder="비밀번호" v-model="password" />
    <button @click="auth">로그인</button>
    <hr />
    <button @click="$router.push('/join')">회원가입</button>
  </main>
</template>

<script>
export default {
  data () {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    auth () {
      if (this.validate()) {
        this.$axios.post('/api/auth', {
          username: this.username,
          password: this.password
        }).then(response => {
          if (response.status === 200) {
            this.$store.commit('setPrincipal', response.data)
            this.$router.push(this.$route.query.redirect || '/')
          }
        })
      }
    },
    validate () {
      const usernameRegExp = /^[a-z]{1,9}$/
      const passwordRegExp = /^[a-zA-Z0-9]{1,9}$/
      if (!usernameRegExp.test(this.username) || !passwordRegExp.test(this.password)) {
        alert('로그인에 실패하였습니다.')
        return false
      }
      return true
    }
  }
}
</script>

<style scoped>
.login {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.login > input {
  width: 15rem;
  height: 3rem;
  margin: 1rem 0;
  padding: 0 1rem;
  border: 1px solid lightgray;
}

.login > button {
  width: 15rem;
  height: 3rem;
  margin: 1rem 0;
}

.login > hr {
  width: 18rem;
  height: 1px;
  border: none;
  background-color: lightgray;
}
</style>
