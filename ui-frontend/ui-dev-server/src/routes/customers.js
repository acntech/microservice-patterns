const express = require('express');
const customers = require('../../data/customers.json');

const router = express.Router();

router.get('/', function (req, res) {
    res.send(customers);
});

router.get('/:id', function (req, res) {
    const customer = customers.find((c) => c.customerId = req.params.id);
    if (customer) {
        res.send(customer);
    } else {
        res.sendStatus(204);
    }

});

module.exports = router;
