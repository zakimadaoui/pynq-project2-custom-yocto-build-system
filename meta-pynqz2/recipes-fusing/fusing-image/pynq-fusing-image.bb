
#  Copyright (C)2025 Zakaria Madaoui
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SUMMARY = "Generates fusing application firmware as BOOT.bin image"

DEPENDS="fusing-app fsbl-firmware bootgen-native"

FUSING_APP ?= "fusing-app-${MACHINE}-ps7_cortexa9_0"
FUSING_APP_PATH ?= "${DEPLOY_DIR_IMAGE}/${FUSING_APP}.elf"
FUSING_IMAGE_BIN ?= "fusing-app-BOOT.bin"
FSBL_IMAGE_NAME ?= "fsbl-${MACHINE}"
FSBL_ELF_PATH ?= "${DEPLOY_DIR_IMAGE}/${FSBL_IMAGE_NAME}.elf"


# generate the BOOT.bin file
do_create_bif() {
    local bif_file="$1"
    bbnote "Generating BIF file: ${bif_file}"
    
    # Clean up any existing file
    rm -f "${bif_file}"

cat > ${bif_file} << EOF
    fusing_image:
    {
        [bootloader] ${FSBL_ELF_PATH}
        ${FUSING_APP_PATH}
    }
EOF
}

do_compile() {
    # generate the .bif file for fusing image
    local bif_file="${B}/fusing_image.bif"
    do_create_bif "${bif_file}" || bbfatal "BIF generation failed"

    # create the image
    eval bootgen -image ${bif_file} -arch zynq -w -o "${B}/${FUSING_IMAGE_BIN}"
    
    if [ ! -e "${B}/${FUSING_IMAGE_BIN}" ]; then
        bbfatal "bootgen failed generating the ${FUSING_IMAGE_BIN} file"
    fi
}

inherit deploy
do_deploy(){
    install -d ${DEPLOYDIR}
    install -m 0644 "${B}/${FUSING_IMAGE_BIN}" "${DEPLOYDIR}/"
}
addtask do_deploy after do_compile