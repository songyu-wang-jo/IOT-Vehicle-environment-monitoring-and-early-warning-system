const path = require('path')

function resolve(dir) {
    return path.join(__dirname, '.', dir)
}

module.exports = {
    chainWebpack: config => {
        config.module.rules.delete("svg"); //重点:删除默认配置中处理svg,
        config.module
            .rule('svg-sprite-loader')
            .test(/\.svg$/)
            .include
            .add(resolve('src/icons')) //处理svg目录
            .end()
            .use('svg-sprite-loader')
            .loader('svg-sprite-loader')
            .options({
                symbolId: 'icon-[name]'
            })
    },
    devServer: {
        proxy: { //配置跨域
            '/api': {
                target: 'http://localhost:8787/', //这里后台的地址模拟的;应该填写你们真实的后台接口
                changOrigin: true, //允许跨域
                pathRewrite: {
                    /* 重写路径，当我们在浏览器中看到请求的地址为：http://localhost:8080/api/core/getData/userInfo 时
                     实际上访问的地址是：http://127.0.0.1:8787/core/getData/userInfo,因为重写了 /api
                     */
                    '^/api': ''
                }
            },
        }
    },
}
