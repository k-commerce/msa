<template>
  <div class="categoryMenu">
    <ul>
      <li v-for="category in categoryList" :key="category">
        <div @click="showChildList(category)">
          <span>{{ category.name }}</span>
          <i class="fas fa-caret-down fa-lg" />
        </div>
        <ul v-show="parent === category">
          <li v-for="child in category.childList" :key="child" @click="goToItemList(child)">
            {{ child.name }}
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data () {
    return {
      categoryList: [],
      parent: null
    }
  },
  methods: {
    getCategoryList () {
      this.$axios.get('/api/categories')
        .then(response => {
          if (response.status === 200) {
            this.categoryList = this.groupByParentId(response.data)
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
    showChildList (category) {
      this.parent = this.parent === category ? 0 : category
    },
    goToItemList (category) {
      this.$router.push({ name: 'itemList', query: { categoryId: category.id } })
      this.$emit('hideMenu')
    }
  },
  created () {
    this.getCategoryList()
  }
}
</script>

<style scoped>
.categoryMenu {
  position: fixed;
  z-index: 2;
  width: 27rem;
  height: 44rem;
  background-color: #f6f6f6;
}

.categoryMenu > ul {
  margin: 0;
  padding: 0;
  list-style-type: none;
}

.categoryMenu > ul > li > div {
  padding: 1rem;
  border: 1px solid #ddd;
  cursor: pointer;
}

.categoryMenu > ul > li > div:hover {
  background-color: aliceblue;
}

.categoryMenu > ul > li > div > i {
  float: right;
}

.categoryMenu > ul > li > ul {
  margin: 0;
  padding: 0;
  list-style-type: none;
  background-color: white;
}

.categoryMenu > ul > li > ul > li {
  padding: 1rem;
  border: 1px solid #ddd;
  cursor: pointer;
}

.categoryMenu > ul > li > ul > li:hover {
  background-color: aliceblue;
}
</style>
