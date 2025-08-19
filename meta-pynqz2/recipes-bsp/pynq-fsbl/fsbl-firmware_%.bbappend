# If QYNQ_FSBL_DEBUG_ENABLE is defined or passed through shelling through BB_ENV_PASSTHROUGH_ADDITIONS, enable debug printing in FSBL
python () {
    debug_enable = d.getVar('QYNQ_FSBL_DEBUG_ENABLE') or ""
    if debug_enable == "1":
        d.setVar('XSCTH_BUILD_DEBUG', '1')
        bb.note("Building FSBL with DEBUG set to 1")
}