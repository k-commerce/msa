import { createRouter, createWebHistory } from 'vue-router'
import store from '@/store'
import HomeView from '@/views/HomeView'
import JoinView from '@/views/JoinView'
import LoginView from '@/views/LoginView'
import ItemListView from '@/views/ItemListView'
import ItemView from '@/views/ItemView'
import OrderView from '@/views/OrderView'
import OrderHistoryView from '@/views/OrderHistoryView'
import AddressBookView from '@/views/AddressBookView'

const isAuthenticated = () => store.getters.getAccessToken && store.getters.getPrincipal

const requireAuthentication = () => (to, from, next) => next(isAuthenticated() ? null : '/login?redirect=' + to.fullPath)

const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/join', name: 'join', component: JoinView },
  { path: '/login', name: 'login', component: LoginView },
  { path: '/items', name: 'itemList', component: ItemListView },
  { path: '/items/:id', name: 'item', component: ItemView },
  { path: '/order', name: 'order', component: OrderView, beforeEnter: requireAuthentication() },
  { path: '/orderhistory', name: 'orderHistory', component: OrderHistoryView, beforeEnter: requireAuthentication() },
  { path: '/addressbook', name: 'addressBook', component: AddressBookView, beforeEnter: requireAuthentication() },
  {
    path: '/:pathMatch(.*)*',
    redirect: to => {
      alert('요청하신 페이지를 찾을 수 없습니다.')
      return '/'
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
