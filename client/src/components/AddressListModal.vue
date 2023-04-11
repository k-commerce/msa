<template>
  <div class="addressListModal">
    <button @click="close(null)">직접 입력</button>

    <span v-for="address in addressList" :key="address">
      <span>{{ address.name }}</span>
      <div>{{ '(' + address.postcode + ') ' + address.selected + ' ' + address.detailed }}</div>
      <button @click="close(address)">선택</button>
    </span>
  </div>
</template>

<script>
export default {
  data () {
    return {
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
    close (address) {
      this.$emit('close', address)
    }
  },
  created () {
    this.getAddressList()
  }
}
</script>

<style scoped>
.addressListModal {
  overflow: auto;
  position: fixed;
  width: 27rem;
  height: 44rem;
  background-color: white;
}

.addressListModal > button {
  width: 24rem;
  height: 3rem;
  margin: 1rem 0;
}

.addressListModal > span {
  display: inline-block;
  width: 22rem;
  height: 10rem;
  margin: 0 0 1rem 0;
  padding: 1rem;
  border: 1px solid lightgray;
  text-align: left;
}

.addressListModal > span > span {
  display: block;
  height: 2rem;
}

.addressListModal > span > div {
  height: 6rem;
}

.addressListModal > span > button {
  width: 6rem;
  height: 2rem;
}
</style>
