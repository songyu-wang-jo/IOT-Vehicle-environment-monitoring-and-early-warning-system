import {createRouter, createMemoryHistory} from 'vue-router'


const routes = [
  {
    path: '/',
    name: 'index',
    component: () => import('../views/Index')
  },
  {
    path: '/pc',
    name: 'pc',
    component: () => import('../views/pc/PC')
  },
  {
    path: '/mobile',
    name: 'mobile',
    component: () => import('../views/mobile/Mobile')
  },
]

const router = createRouter({
  history: createMemoryHistory(process.env.BASE_URL),
  routes
})


export default router
