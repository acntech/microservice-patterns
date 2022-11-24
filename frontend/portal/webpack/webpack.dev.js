const paths = require('./paths')

const webpack = require('webpack')
const { merge } = require('webpack-merge')
const common = require('./webpack.common.js')

const config = {
  mode: 'development',

  // Control how source maps are generated
  devtool: 'source-map',

  // Spin up a server for quick development
  devServer: {
    static: paths.public,
    port: 3000,
    historyApiFallback: true,
    open: false,
    hot: true,
    compress: true,
    proxy: [
      {
        context: ['/api', '/login', '/logout', '/oauth2', '/error'],
        target: 'http://localhost:9000',
        secure: false
      }
    ]
  },
  plugins: [
    // Only update what has changed on hot reload
    new webpack.HotModuleReplacementPlugin()
  ]
};

module.exports = merge(common, config);
