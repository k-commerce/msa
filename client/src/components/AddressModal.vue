<template>
  <div class="addressModal">
    <input type="text" placeholder="배송지 이름" v-model="name" />
    <input type="text" placeholder="우편번호" readonly v-model="postcode" @click="findPostcode" />
    <input type="text" placeholder="도로명 주소 / 지번 주소" readonly v-model="selected" @click="findPostcode" />
    <input type="text" placeholder="상세 주소" v-model="detailed" />

    <span>
      <button v-if="!address" @click="createAddress">추가</button>
      <button v-else @click="updateAddress">수정</button>
      <button @click="close">취소</button>
    </span>

    <DaumPostcode ref="daumPostcode" @oncomplete="oncomplete" />
  </div>
</template>

<script>
import DaumPostcode from '@/components/DaumPostcode'

export default {
  components: {
    DaumPostcode
  },
  props: {
    address: Object
  },
  data () {
    return {
      id: null,
      name: '',
      postcode: '',
      selected: '',
      detailed: ''
    }
  },
  methods: {
    createAddress () {
      if (this.validate()) {
        this.$axios.post('/api/addresses', {
          name: this.name,
          postcode: this.postcode,
          selected: this.selected,
          detailed: this.detailed
        }).then(response => {
          if (response.status === 200) {
            alert('배송지를 추가하였습니다.')
            this.close()
          }
        })
      }
    },
    updateAddress () {
      if (this.validate()) {
        this.$axios.put('/api/addresses/' + this.id, {
          name: this.name,
          postcode: this.postcode,
          selected: this.selected,
          detailed: this.detailed
        }).then(response => {
          if (response.status === 200) {
            alert('배송지를 수정하였습니다.')
            this.close()
          }
        })
      }
    },
    validate () {
      const nameRegExp = /^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{1,9}$/
      if (!nameRegExp.test(this.name)) {
        alert('배송지 이름은 1~9자의 영문, 숫자, 한글만 사용 가능합니다.')
        return false
      }
      const postcodeRegExp = /^[0-9]{5}$/
      if (!postcodeRegExp.test(this.postcode)) {
        alert('우편번호는 5자의 숫자로 입력해 주세요.')
        return false
      }
      if (this.selected.length < 1 || this.selected.length > 100) {
        alert('도로명 주소 / 지번 주소는 1~100자로 입력해 주세요.')
        return false
      }
      if (this.detailed.length > 100) {
        alert('상세 주소는 100자 이하로 입력해 주세요.')
        return false
      }
      return true
    },
    findPostcode () {
      this.$refs.daumPostcode.open()
    },
    oncomplete ({ postcode, selected }) {
      this.postcode = postcode
      this.selected = selected
    },
    close () {
      this.$emit('close')
    }
  },
  created () {
    if (this.address) {
      this.id = this.address.id
      this.name = this.address.name
      this.postcode = this.address.postcode
      this.selected = this.address.selected
      this.detailed = this.address.detailed
    }
  }
}
</script>

<style scoped>
.addressModal {
  position: fixed;
  width: 27rem;
  height: 44rem;
  background-color: white;
}

.addressModal > input {
  width: 22rem;
  height: 3rem;
  padding: 0 1rem;
  border: 1px solid lightgray;
}

.addressModal > input:first-child {
  margin: 1rem 0 0 0;
}

.addressModal > span {
  display: inline-flex;
  justify-content: space-between;
  width: 24rem;
  margin: 1rem 0;
}

.addressModal > span > button {
  width: 6rem;
  height: 3rem;
}
</style>
