const express = require('express');
const orders = require('../../data/orders.json');

const router = express.Router();

router.get('/', function (req, res) {
    res.send(orders);
});

router.get('/:id', function (req, res) {
    const order = orders.find((c) => c.orderId = req.params.id);
    if (order) {
        res.send(order);
    } else {
        res.sendStatus(204);
    }

});

module.exports = router;
