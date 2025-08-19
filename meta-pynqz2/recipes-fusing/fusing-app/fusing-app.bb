SUMMARY = "Fusing App using XSCT"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit check_xsct_enabled xsct_baremetal_app

SRC_URI = "${EMBEDDEDSW_SRCURI}"
SRC_URI += "file://xilskey_efuse.c"
SRC_URI += "file://xilskey_input.h"

S = "${WORKDIR}/git"

# bare metal application library dependencies to be pulled from embeddedsw repo
BAREMETAL_DEPENDS_LIBRARIES = "xilskey xiltimer"
# change the default core from 'ps7_cortexa9_1' to 'ps7_cortexa9_0'. Without this the xilskey library won't compile
XSCTH_PROC:zynq = "ps7_cortexa9_0"
# Use the Empty application project template as a base.
XSCTH_APP = "Empty Application(C)"
# Copy the fusing app code to the empty project before compilation
do_configure:append(){
    cp ${WORKDIR}/xilskey_efuse.c ${B}/${XSCTH_PROJ}/
    cp ${WORKDIR}/xilskey_input.h ${B}/${XSCTH_PROJ}/
}

COMPATIBLE_MACHINE:zynq = ".*"

PACKAGE_ARCH = "${MACHINE_ARCH}"
