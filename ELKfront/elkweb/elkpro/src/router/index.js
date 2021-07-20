import Vue from 'vue'
import VueRouter from 'vue-router'
import index from '../components/index.vue'
import test from '../components/test.vue'
import datainfo from '../components/datainfo.vue'
import showTable from '../components/showTable.vue'
Vue.use(VueRouter)

const routes = [
  {
    path:'/',
    name:'index',
    component:index
  },{
    path:'/test',
    name:'test',
    component:test
  },{
    path:'/datainfo',
    name:'datainfo',
    component:datainfo
  },{
    path:'/showTable',
    name:'showTable',
    component:showTable
  }
]

const router = new VueRouter({
  routes,
  mode:'history'
})

export default router
