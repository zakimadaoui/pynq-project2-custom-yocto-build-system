#  Copyright (C)2025 Zakaria Madaoui
# TODO: change license
LICENSE = "CLOSED" 

FILESEXTRAPATHS:append := ":${THISDIR}/files" 
SRC_URI = " \
    git://github.com/Xilinx/embeddedsw.git;protocol=https;branch=master \
    file://add-pynqz1-pynqz2-support-fsbl.patch \
    file://fsbl-fixups.patch \
    "

PV = "master+git"
SRCREV = "45a18907084e77bb3a450a035d280130d7ff6e26"

S = "${WORKDIR}/git"
B = "${WORKDIR}/git/lib/sw_apps/zynq_fsbl/src"

DEPENDS = "gcc-arm-none-eabi-native"

EXTRA_OEMAKE = "BOARD=zynq-pynqz2 CC=arm-none-eabi-gcc SHELL=/bin/bash"

do_compile() {
    echo "First attempt to build..."
    if ! oe_runmake; then
        echo "First build failed, retrying..."
        oe_runmake
    fi
}

FSBL_ELF_FILE="fsbl-${MACHINE}.elf"
SYSROOT_DIRS+="/boot"
FILES:${PN} += "/boot/${FSBL_ELF_FILE}"

do_install() {
    install -Dm 0644 ${B}/fsbl.elf ${D}/boot/${FSBL_ELF_FILE}
}


inherit deploy
do_deploy() {
    install -Dm 0644 ${B}/fsbl.elf ${DEPLOYDIR}/${FSBL_ELF_FILE}
}

addtask deploy before do_build after do_install

INSANE_SKIP:${PN} += "buildpaths"
# COMPATIBLE_MACHINE = "^(zynq)$"

