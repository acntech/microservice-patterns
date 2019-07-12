#!/bin/bash

SCRIPT="$0"
SCRIPT_DIR="$(dirname ${SCRIPT})"

ORIG_DIR=${PWD}

cd ${SCRIPT_DIR}

yarn dev-start

cd ${ORIG_DIR}
