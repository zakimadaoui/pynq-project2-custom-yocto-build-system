#!/bin/bash

# make sure all the sub-repos are cloned
git submodule update --init --recursive

# source poky environment while specifiying which local.conf and bblayers.conf it should pick
rm build/conf -rf
export TEMPLATECONF="$PWD/meta-pynqz2/conf/templates/pynq"
PWD1=$PWD
echo "TEMPLATECONF=$TEMPLATECONF"
source poky/oe-init-build-env
cd $PWD1

# todo: print the list of bitbake+image commands possible
# bitbake pynq-image
# bitbake pynq-fusing-image

# export an env variable that tells other scripts that the build environment has been initialized properly
export PYNQ_ENV_READY=1