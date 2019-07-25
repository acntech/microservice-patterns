const express = require('express');
const products = require('../../data/products.json');

const router = express.Router();

router.get('/', function (req, res) {
    res.send(products);
});

router.get('/:id', function (req, res) {
    const product = products.find((c) => c.productId = req.params.id);
    if (product) {
        res.send(product);
    } else {
        res.sendStatus(204);
    }

});

module.exports = router;
