#  Copyright (C)2025 Zakaria Madaoui
# TODO: change license

# This recipe converts the .bit bitstream file to a binary bitstream firmware (.bin) that can be used as overlay

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# bitstream is a dependency for this recipe to work
DEPENDS += "virtual/bitstream bootgen-native"

BITSTREAM_BIT_BIN = "download-${MACHINE}.bit.bin"

# generate the bitstream .bit.bin file
do_compile() {
    __BITSTREAM_BIF="${B}/bitstream_image.bif"

    cp ${RECIPE_SYSROOT}/boot/bitstream/download-${MACHINE}.bit ${B}/download-${MACHINE}.bit

    rm -f "${__BITSTREAM_BIF}"
    echo "bitstream_image:"                                              > "${__BITSTREAM_BIF}"
    echo "{"                                                            >> "${__BITSTREAM_BIF}"
    echo "    download-${MACHINE}.bit"                                  >> "${__BITSTREAM_BIF}"
    echo "}"                                                            >> "${__BITSTREAM_BIF}"
    
    eval bootgen -image ${__BITSTREAM_BIF} -arch zynq -process_bitstream bin -w -o "${B}/${BITSTREAM_BIT_BIN}"
    
    if [ ! -e "${B}/${BITSTREAM_BIT_BIN}" ]; then
        bbfatal "bootgen failed generating the .bit.bin file"
    fi
}

FILES:${PN} += "/boot/${BITSTREAM_BIT_BIN}"
do_install(){
    install -d ${D}/boot
    install -m 0644 "${B}/${BITSTREAM_BIT_BIN}" "${D}/boot/${BITSTREAM_BIT_BIN}"
}

inherit deploy
do_deploy(){
    install -d ${DEPLOYDIR}
    install -m 0644 "${B}/${BITSTREAM_BIT_BIN}" "${DEPLOYDIR}/"
}
addtask do_deploy after do_compile