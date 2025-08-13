#  Copyright (C)2025 Zakaria Madaoui

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append = "\
    file://system-user.dtsi \
"

do_configure:append () {
    echo '/include/ "system-user.dtsi"' >> ${DT_FILES_PATH}/system-top.dts
}

do_configure[vardepsexclude] = "BB_ORIGENV"