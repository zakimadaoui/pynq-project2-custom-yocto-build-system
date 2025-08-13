SUMMARY = "Script to load FPGA bitstream at boot (SysVinit)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://load-bitstream.sh"
RDEPENDS:${PN} = "bitstream-bin fpga-manager-script"

inherit update-rc.d
INITSCRIPT_NAME = "load-bitstream"
INITSCRIPT_PARAMS = "start 99 5 . stop 20 0 1 6 ."

do_install() {
    # Install init script
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/load-bitstream.sh ${D}${sysconfdir}/init.d/load-bitstream
}

FILES:${PN} += "${sysconfdir}/init.d/load-bitstream"