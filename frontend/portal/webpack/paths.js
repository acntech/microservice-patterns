const fs = require('fs');
const path = require('path')

const appDirectory = fs.realpathSync(process.cwd());
const resolveAppPath = (relativePath) =>
    path.resolve(appDirectory, relativePath);

module.exports = {
  // Source files
  src: resolveAppPath('src'),

  // Files for production build
  build: resolveAppPath('dist'),

  // Static files
  public: resolveAppPath('public')
};
