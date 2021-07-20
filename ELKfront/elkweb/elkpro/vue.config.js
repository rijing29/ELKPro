module.exports = {
  lintOnSave: false,
  devServer: {
    port: 8888,
    open: true, // 配置自动启动浏览器
    proxy: {
        '/api': {
            target: 'http://localhost:8080/elk',
            ws: true,
            changeOrigin: true,
            pathRewrite: {
                '^/api': ''
            }
        }
    }
  }
}
