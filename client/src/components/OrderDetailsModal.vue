<template>
  <div class="orderDetailsModal">
    <i class="fas fa-times fa-lg" @click="close" />

    <div>{{ order.createdDate.substring(0, 10) + ' ' + order.createdDate.substring(11, 19) }}</div>

    <span>
      <div>결제 정보</div>
      <hr />
      <div v-for="orderItem in order.orderItemList" :key="orderItem">
        <span>{{ orderItem.itemName }}</span>
        <span>{{ orderItem.orderPrice }} 원</span>
      </div>
      <hr />
      <div>
        <span>결제 방식</span>
        <span>{{ order.payment === 'CARD' ? '신용카드' : '무통장입금' }}</span>
      </div>
      <div>
        <span>결제 금액</span>
        <span>{{ totalPrice }} 원</span>
      </div>
    </span>

    <span>
      <div>배송 정보</div>
      <hr />
      <div>{{ order.name }}</div>
      <div>{{ order.phoneNumber.substring(0, 3) + '-' + order.phoneNumber.substring(3, 7) + '-' + order.phoneNumber.substring(7, 11) }}</div>
      <div>{{ '(' + order.address.postcode + ') ' + order.address.selected + ' ' + order.address.detailed }}</div>
    </span>
  </div>
</template>

<script>
export default {
  props: {
    order: Object
  },
  data () {
    return {
      totalPrice: 0
    }
  },
  methods: {
    close () {
      this.$emit('close')
    }
  },
  created () {
    for (const orderItem of this.order.orderItemList) {
      this.totalPrice += orderItem.orderPrice
    }
  }
}
</script>

<style scoped>
.orderDetailsModal {
  position: fixed;
  width: 25rem;
  height: 42rem;
  padding: 1rem;
  background-color: #f6f6f6;
}

.orderDetailsModal > i {
  position: absolute;
  right: 1rem;
  cursor: pointer;
}

.orderDetailsModal > span {
  display: block;
  margin: 1rem 0 0 0;
  padding: 1rem;
  border-radius: 1rem;
  background-color: white;
}

.orderDetailsModal > span > div {
  display: flex;
  justify-content: space-between;
  height: 2rem;
}

.orderDetailsModal > span > hr {
  height: 1px;
  border: none;
  background-color: #ddd;
}
</style>
