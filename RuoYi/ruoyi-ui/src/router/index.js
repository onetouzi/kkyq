import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const Flower = () => import('@/views/pill/flower/index.vue')
const FlowerClient = () => import('@/views/pill/flowerClient/index.vue')
const FlowerOrder = () => import('@/views/pill/flowerOrder/index.vue')
const FlowerAllOrder = () => import('@/views/pill/flowerAllOrder/index.vue')
const FlowerInventory = () => import('@/views/pill/flowerInventory/index.vue')
const FlowerAlerts = () => import('@/views/pill/flowerAlerts/index.vue')
const FlowerReports = () => import('@/views/pill/flowerReports/index.vue')

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/pill/flower'
    },
    {
      path: '/pill/flower',
      component: Flower
    },
    {
      path: '/pill/flowerClient',
      component: FlowerClient
    },
    {
      path: '/pill/flowerOrder',
      component: FlowerOrder
    },
    {
      path: '/pill/flowerAllOrder',
      component: FlowerAllOrder
    },
    {
      path: '/pill/flowerInventory',
      component: FlowerInventory
    },
    {
      path: '/pill/flowerAlerts',
      component: FlowerAlerts
    },
    {
      path: '/pill/flowerReports',
      component: FlowerReports
    }
  ]
})


