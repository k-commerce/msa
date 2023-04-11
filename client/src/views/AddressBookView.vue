<template>
  <main class="addressBook">
    <AddressModal v-if="addressModal" :address="address" @close="close" />

    <button @click="open(null)">배송지 추가</button>

    <span v-for="address in addressList" :key="address">
      <span>{{ address.name }}</span>
      <div>{{ '(' + address.postcode + ') ' + address.selected + ' ' + address.detailed }}</div>
      <span>
        <button @click="open(address)">수정</button>
        <button @click="deleteAddress(address)">삭제</button>
      </span>
    </span>
  </main>
</template>

<script>
import AddressModal from '@/components/AddressModal'

export default {
  components: {
    AddressModal
  },
  data () {
    return {
      addressModal: false,
      address: null,
      addressList: []
    }
  },
  methods: {
    getAddressList () {
      this.$axios.get('/api/addresses', {
      }).then(response => {
        if (response.status === 200) {
          this.addressList = response.data
        }
      })
    },
    deleteAddress (address) {
      this.$axios.delete('/api/addresses/' + address.id, {
      }).then(response => {
        if (response.status === 200) {
          alert('배송지를 삭제하였습니다.')
          this.getAddressList()
        }
      })
    },
    open (address) {
      this.addressModal = true
      this.address = address
    },
    close () {
      this.getAddressList()
      this.addressModal = false
    }
  },
  created () {
    this.getAddressList()
  }
}
</script>

<style scoped>
.addressBook {
  text-align: center;
}

.addressBook > button {
  width: 24rem;
  height: 3rem;
  margin: 1rem 0;
}

.addressBook > span {
  display: inline-block;
  width: 22rem;
  height: 10rem;
  margin: 0 0 1rem 0;
  padding: 1rem;
  border: 1px solid lightgray;
  text-align: left;
}

.addressBook > span > span {
  display: flex;
  justify-content: space-between;
  height: 2rem;
}

.addressBook > span > div {
  height: 6rem;
}

.addressBook > span > span > button {
  width: 6rem;
}
</style>
