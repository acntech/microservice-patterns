const paths = require('./paths')
const { merge } = require('webpack-merge')
const TerserWebpackPlugin = require("terser-webpack-plugin");
const common = require('./webpack.common.js')

const config = {
  mode: 'production',
  devtool: false,
  output: {
    path: paths.build,
    publicPath: '/',
    filename: '[name].[contenthash].bundle.js'
  },
  optimization: {
    minimize: true,
    minimizer: [new TerserWebpackPlugin()],
    runtimeChunk: {
      name: 'runtime'
    },
  },
  performance: {
    hints: false,
    maxEntrypointSize: 512000,
    maxAssetSize: 512000
  },
};

module.exports = merge(common, config);
