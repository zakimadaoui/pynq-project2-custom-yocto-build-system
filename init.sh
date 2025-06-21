#!/bin/bash

# make sure all the sub-repos are cloned
git submodule update --init --recursive

# source poky environment while specifiying which local.conf and bblayers.conf it should pick
TEMPLATECONF=meta-pynqz2/conf/templates source poky/oe-init-build-env

# todo: print the list of bitbake+image commands possible
