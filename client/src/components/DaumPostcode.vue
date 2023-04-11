<template>
  <div class="daumPostcode" ref="daumPostcode">
    <img
      src="//t1.daumcdn.net/postcode/resource/images/close.png"
      @click="close"
    />
  </div>
</template>

<script>
export default {
  methods: {
    open () {
      const daumPostcode = this.$refs.daumPostcode

      new window.daum.Postcode({
        oncomplete: (data) => {
          const postcode = data.zonecode

          let selected = ''
          if (data.userSelectedType === 'R') {
            selected = data.roadAddress
          } else {
            selected = data.jibunAddress
          }

          daumPostcode.style.display = 'none'

          this.$emit('oncomplete', { postcode, selected })
        },
        width: '100%',
        height: '100%'
      }).embed(daumPostcode)

      daumPostcode.style.display = 'block'
    },
    close () {
      this.$refs.daumPostcode.style.display = 'none'
    }
  }
}
</script>

<style scoped>
.daumPostcode {
  display: none;
  overflow: auto;
  position: relative;
  width: 24rem;
  height: 24rem;
  margin: 0 auto;
  border: 1px solid black;
}

.daumPostcode > img {
  position: absolute;
  right: 0;
  z-index: 1;
  cursor: pointer;
}
</style>
