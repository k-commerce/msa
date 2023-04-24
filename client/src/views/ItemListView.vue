<template>
  <main class="itemList" @scroll="scroll">
    <div v-if="category">
      <div>{{ category.name }}</div>
      <ul>
        <li v-for="child in category.childList" :key="child" @click="goToItemList(child)">
          {{ child.name }}
        </li>
      </ul>
    </div>

    <div>
      <span v-for="item in itemList" :key="item" @click="goToItem(item)">
        <img />
        <div>{{ item.name }}</div>
        <div>{{ item.price }} 원</div>
        <div>{{ item.description }}</div>
      </span>
    </div>
  </main>
</template>

<script>
export default {
  data () {
    return {
      cursorId: null,
      name: null,
      categoryId: null,
      category: null,
      itemList: []
    }
  },
  methods: {
    getCategoryList () {
      this.$axios.get('/api/categories')
        .then(response => {
          if (response.status === 200) {
            const categoryList = Object.values(this.groupByParentId(response.data))
            for (const category of categoryList) {
              const childList = category.childList
              for (const child of childList) {
                if (child.id === this.categoryId) {
                  this.category = category
                  return
                }
              }
            }
          }
        })
    },
    groupByParentId (categoryList) {
      return categoryList.reduce((acc, obj) => {
        const { id, parentId } = obj
        if (parentId == null) {
          acc[id] = obj
          acc[id].childList = []
        } else {
          acc[parentId].childList.push(obj)
        }
        return acc
      }, {})
    },
    getItemList () {
      this.$axios.get('/api/items', {
        params: {
          cursorId: this.cursorId,
          name: this.name,
          categoryId: this.categoryId
        }
      }).then(response => {
        if (response.status === 200) {
          const itemList = response.data
          if (itemList.length) {
            this.itemList.push(...itemList)
            this.cursorId = Math.max(...itemList.map(x => x.id))
          }
        }
      })
    },
    goToItemList (category) {
      this.$router.push({ name: 'itemList', query: { categoryId: category.id } })
    },
    goToItem (item) {
      this.$router.push({ name: 'item', params: { id: item.id } })
    },
    scroll (e) {
      const { clientHeight, scrollHeight, scrollTop } = e.target
      if (clientHeight === scrollHeight - scrollTop) {
        this.getItemList()
      }
    }
  },
  created () {
    this.name = this.$route.query.name
    if (this.$route.query.categoryId) {
      this.categoryId = parseInt(this.$route.query.categoryId)
      this.getCategoryList()
    }
    this.getItemList()
  }
}
</script>

<style scoped>
.itemList > div:first-child {
  text-align: center;
}

.itemList > div:first-child > div {
  padding: 1rem;
  border: 1px solid #ddd;
  background-color: #f6f6f6;
}

.itemList > div:first-child > ul {
  margin: 0;
  padding: 0;
  list-style-type: none;
}

.itemList > div:first-child > ul > li {
  padding: 1rem;
  border: 1px solid #ddd;
  cursor: pointer;
}

.itemList > div:first-child > ul > li:hover {
  background-color: aliceblue;
}

.itemList > div:last-child {
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.itemList > div:last-child > span {
  padding: 1rem;
  cursor: pointer;
}

.itemList > div:last-child > span:hover {
  background-color: aliceblue;
}

.itemList > div:last-child > span > img {
  width: 10rem;
  height: 10rem;
}
</style>
