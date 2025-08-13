#!/bin/bash

# make sure all the sub-repos are cloned
git submodule update --init --recursive

# source poky environment while specifiying which local.conf and bblayers.conf it should pick
rm build/conf -rf
export TEMPLATECONF="$PWD/meta-pynqz2/conf/templates/pynq"
echo "TEMPLATECONF=$TEMPLATECONF"
source poky/oe-init-build-env

# todo: print the list of bitbake+image commands possible
