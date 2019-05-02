#!/bin/bash

SCRIPT="$0"
SCRIPT_DIR="$(dirname ${SCRIPT})"

ORIG_DIR=${PWD}

export CUSTOMERS_API_URL="http://localhost:9000"
export PRODUCTS_API_URL="http://localhost:9040"
export ORDERS_API_URL="http://localhost:9010"
export RESERVATIONS_API_URL="http://localhost:9040"

cd ${SCRIPT_DIR}

yarn dev-start

cd ${ORIG_DIR}
