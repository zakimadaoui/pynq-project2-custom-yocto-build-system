#  Copyright (C)2025 Zakaria Madaoui
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PROVIDES += "virtual/hdf"

# speedup the build
INHIBIT_DEFAULT_DEPS = "1"

FILESEXTRAPATHS:append := ":${THISDIR}/files" 
SRC_URI = "file://hardware_design.xsa"

S = "${WORKDIR}"
B = "${WORKDIR}"

inherit deploy
do_deploy() {
    install -Dm 0644 hardware_design.xsa ${DEPLOYDIR}/Xilinx-${MACHINE}.xsa
}

addtask deploy before do_build after do_install

