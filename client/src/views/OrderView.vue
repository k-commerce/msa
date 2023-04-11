<template>
  <main class="order">
    <AddressListModal v-if="addressListModal" @close="close" />

    <span>
      <div>주문 상품</div>
      <span v-for="orderItem in orderItemList" :key="orderItem">
        <img />
        <span>
          <div>{{ orderItem.name }}</div>
          <div>{{ orderItem.price }}원 X {{ orderItem.quantity }}개</div>
          <div>= {{ orderItem.price * orderItem.quantity }}원</div>
        </span>
      </span>
    </span>

    <span>
      <div>배송 정보</div>
      <label><input type="checkbox" v-model="checked" @change="check" />주문자와 동일</label>
      <input type="text" placeholder="받는 사람" v-model="name" @input="oninput" />
      <input type="text" placeholder="연락처" v-model="phoneNumber" @input="oninput" />
      <button @click="open">배송지 목록</button>
      <input type="text" placeholder="우편번호" readonly v-model="postcode" @click="findPostcode" />
      <input type="text" placeholder="도로명 주소 / 지번 주소" readonly v-model="selected" @click="findPostcode" />
      <input type="text" placeholder="상세 주소" v-model="detailed" />
    </span>
    <DaumPostcode ref="daumPostcode" @oncomplete="oncomplete" />

    <span>
      <div>결제 방식</div>
      <label><input type="radio" value="CARD" v-model="payment" />신용카드</label>
      <label><input type="radio" value="DEPOSIT" v-model="payment" />무통장입금</label>
    </span>

    <span>
      <div>결제 금액</div>
      <span>총 {{ totalPrice }} 원</span>
      <button @click="createOrder">주문하기</button>
    </span>
  </main>
</template>

<script>
import DaumPostcode from '@/components/DaumPostcode'
import AddressListModal from '@/components/AddressListModal'

export default {
  components: {
    DaumPostcode,
    AddressListModal
  },
  data () {
    return {
      addressListModal: false,
      orderItemList: [],
      checked: false,
      name: '',
      phoneNumber: '',
      postcode: '',
      selected: '',
      detailed: '',
      payment: '',
      totalPrice: 0
    }
  },
  computed: {
    orderCheck () {
      return this.$store.getters.getOrderCheck
    },
    principal () {
      return this.$store.getters.getPrincipal
    }
  },
  methods: {
    getItemList () {
      this.$axios.get('/api/items', {
        params: {
          itemIdList: Object.keys(this.orderCheck).join(',')
        }
      }).then(response => {
        if (response.status === 200) {
          this.initOrderItemList(response.data)
        }
      })
    },
    initOrderItemList (itemList) {
      for (const { id, name, price, description } of itemList) {
        const quantity = this.orderCheck[id]
        this.orderItemList.push({ itemId: id, name, price, description, quantity })
        this.totalPrice += price * quantity
      }
    },
    createOrder () {
      this.$axios.post('/api/orders', {
        orderCheck: this.orderCheck,
        name: this.name,
        phoneNumber: this.phoneNumber,
        postcode: this.postcode,
        selected: this.selected,
        detailed: this.detailed,
        payment: this.payment
      }).then(response => {
        if (response.status === 200) {
          alert('주문이 완료되었습니다.')
          this.$router.push('/orderhistory')
        }
      })
    },
    check () {
      if (this.checked === true) {
        this.name = this.principal.name
        this.phoneNumber = this.principal.phoneNumber
      } else {
        this.name = ''
        this.phoneNumber = ''
      }
    },
    oninput () {
      this.checked = false
    },
    findPostcode () {
      this.$refs.daumPostcode.open()
    },
    oncomplete ({ postcode, selected }) {
      this.postcode = postcode
      this.selected = selected
    },
    open () {
      this.$refs.daumPostcode.close()
      this.addressListModal = true
    },
    close (address) {
      this.addressListModal = false
      if (address) {
        this.postcode = address.postcode
        this.selected = address.selected
        this.detailed = address.detailed
      }
    }
  },
  created () {
    this.getItemList()
  }
}
</script>

<style scoped>
.order {
  text-align: center;
}

.order > span {
  display: inline-block;
  width: 24rem;
  margin: 1rem 0;
  text-align: left;
}

.order > span > div {
  margin: 0 0 1rem 0;
}

.order > span > span {
  display: flex;
}

.order > span > span > img {
  width: 8rem;
  height: 8rem;
  margin: 0 1rem 0 0;
}

.order > span > label {
  margin: 0 1rem 0 0;
}

.order > span > input[type='text'] {
  width: 22rem;
  height: 3rem;
  padding: 0 1rem;
  border: 1px solid lightgray;
}

.order > span > button {
  width: 24rem;
  height: 3rem;
  margin: 1rem 0 0 0;
}
</style>
