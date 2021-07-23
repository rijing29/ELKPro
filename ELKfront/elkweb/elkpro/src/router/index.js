import Vue from 'vue'
import VueRouter from 'vue-router'
import index from '../components/index.vue'
import test from '../components/test.vue'
import datainfo from '../components/datainfo.vue'
import showSoftNameTable from '../components/showSoftNameTable.vue'
import showNodeTypeTable from '../components/showNodeTypeTable.vue'
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
    path:'/showSoftNameTable',
    name:'showSoftNameTable',
    component:showSoftNameTable
  },{
    path:'/showNodeTypeTable',
    name:'showNodeTypeTable',
    component:showNodeTypeTable
  }
]

const router = new VueRouter({
  routes,
  mode:'history'
})

export default router
