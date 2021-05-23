import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'welcome',
        component: () => import("../views/Welcome"),
        meta: {
            title: "汽车安全管理平台"
        },
    },
    {
        path: '/index',
        name: 'index',
        component: () => import("../views/Index"),
        meta: {
            title: "汽车安全管理平台"
        },
    },
    {
        path: '/pcIndex',
        component: () => import('../views/IndexPC'),
        meta: {
            title: "汽车安全管理平台"
        },
        children: [
            {
                path: 'dataView',
                name: 'dataView',
                component: () => import('../components/pc/DataView')
            },
            {
                path: 'warningView',
                name: 'warningView',
                component: () => import('../components/pc/WarningView')
            },
            {
                path: 'deviceManagerView',
                name: 'deviceManagerView',
                component: () => import('../components/pc/DeviceMangerView')
            }
        ]
    },
    {
        path: '/Mogin',
        component: () => import('../components/mobile/Mogin')
    },
    {
        path: '/Pogin',
        component: () => import('../components/pc/Pogin')
    },
    {
        path: '/mIndex',
        component: () => import('../views/IndexMobile'),
        children: [
            {
                path: 'messageView',
                name: 'messageView',
                component: () => import('../components/mobile/DeviceMessage')
            },
            {
                path: 'statusView',
                name: 'statusView',
                component: () => import('../components/mobile/DeviceStatus')
            }
        ]
    },
]

const router = new VueRouter({
    routes,
    mode: 'history'
})


export default router
