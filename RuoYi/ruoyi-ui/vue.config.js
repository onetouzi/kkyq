const port = process.env.port || process.env.npm_config_port || 80

module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/' : '/',
  outputDir: 'dist',
  assetsDir: 'static',
  devServer: {
    host: '0.0.0.0',
    port: port,
    open: true,
    proxy: {
      [process.env.VUE_APP_BASE_API || '/dev-api']: {
        target: `http://localhost:11451`,
        changeOrigin: true,
        pathRewrite: {
          ['^' + (process.env.VUE_APP_BASE_API || '/dev-api')]: ''
        }
      }
    }
  }
}


