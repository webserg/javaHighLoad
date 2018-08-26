var path = require('path');
module.exports = {
    entry: './src/main/js/app.js',
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: ['babel-loader']
            }
        ]
    },
    resolve: {
        extensions: ['*', '.js', '.jsx']
    },
    output: {
        path: __dirname + '/src/main/resources/static/build',
        publicPath: '/',
        filename: 'bundle.js'
    },

};

