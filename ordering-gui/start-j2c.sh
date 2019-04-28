#!/bin/bash

SCRIPT="$0"
SCRIPT_DIR="$(dirname ${SCRIPT})"

ORIG_DIR=${PWD}

J2C_AWS_URL=http://j2c-p1-elb-public-1592238471.us-east-2.elb.amazonaws.com

export CUSTOMERS_API_URL=${J2C_AWS_URL}
export PRODUCTS_API_URL=${J2C_AWS_URL}
export ORDERS_API_URL=${J2C_AWS_URL}
export RESERVATIONS_API_URL=${J2C_AWS_URL}

cd ${SCRIPT_DIR}

yarn dev-start

cd ${ORIG_DIR}
