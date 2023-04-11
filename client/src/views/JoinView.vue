<template>
  <main class="join">
    <input type="text" placeholder="아이디" v-model="username" />
    <input type="password" placeholder="비밀번호" v-model="password" />
    <input type="password" placeholder="비밀번호 확인" v-model="confirm" />
    <input type="text" placeholder="이름" v-model="name" />
    <input type="text" placeholder="휴대전화번호" v-model="phoneNumber" @input="replacePhoneNumber" />
    <button @click="join">회원가입</button>
  </main>
</template>

<script>
export default {
  data () {
    return {
      username: '',
      password: '',
      confirm: '',
      name: '',
      phoneNumber: ''
    }
  },
  methods: {
    join () {
      if (this.validate()) {
        this.$axios.post('/api/members', {
          username: this.username,
          password: this.password,
          name: this.name,
          phoneNumber: this.phoneNumber
        }).then(response => {
          if (response.status === 200) {
            alert('회원가입이 완료되었습니다.')
            this.$router.push('/login')
          }
        })
      }
    },
    validate () {
      const usernameRegExp = /^[a-z]{1,9}$/
      if (!usernameRegExp.test(this.username)) {
        alert('아이디는 1~9자의 영문 소문자만 사용 가능합니다.')
        return false
      }
      const passwordRegExp = /^[a-zA-Z0-9]{1,9}$/
      if (!passwordRegExp.test(this.password)) {
        alert('비밀번호는 1~9자의 영문, 숫자만 사용 가능합니다.')
        return false
      }
      if (this.password !== this.confirm) {
        alert('비밀번호가 일치하지 않습니다.')
        return false
      }
      const nameRegExp = /^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{1,9}$/
      if (!nameRegExp.test(this.name)) {
        alert('이름은 1~9자의 영문, 숫자, 한글만 사용 가능합니다.')
        return false
      }
      const phoneNumberRegExp = /^[0-9]{11}$/
      if (!phoneNumberRegExp.test(this.phoneNumber)) {
        alert('휴대전화번호는 11자의 숫자만 사용 가능합니다.')
        return false
      }
      return true
    },
    replacePhoneNumber () {
      this.phoneNumber = this.phoneNumber.replace(/[^0-9]/g, '')
    }
  }
}
</script>

<style scoped>
.join {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.join > input {
  width: 15rem;
  height: 3rem;
  margin: 1rem 0;
  padding: 0 1rem;
  border: 1px solid lightgray;
}

.join > button {
  width: 15rem;
  height: 3rem;
  margin: 1rem 0;
}
</style>
