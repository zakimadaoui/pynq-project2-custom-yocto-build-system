#!/bin/sh
### BEGIN INIT INFO
# Provides:          load-bitstream
# Required-Start:    $local_fs
# Required-Stop:
# Default-Start:     5
# Default-Stop:      0 1 6
# Short-Description: Load FPGA bitstream
### END INIT INFO

BITSTREAM="/boot/download-pynq-z2.bit.bin"
OVERLAY="/boot/devicetree/pl.dtbo"

case "$1" in
    start)
        echo "$(date) - Loading bitstream"
        if [ ! -f "$BITSTREAM" ]; then
            echo "Bitstream missing: $BITSTREAM"
            exit 1
        fi
        
        # Wait for FPGA manager
        while [ ! -d /sys/class/fpga_manager/fpga0 ]; do
            sleep 1
        done
        
        if ! fpgautil -b "$BITSTREAM" -o "$OVERLAY" -f Full -n full ; then
            echo "Bitstream load failed"
            exit 1
        fi
        ;;
    stop)
        # Cleanup if needed
        ;;
    *)
        echo "Usage: $0 {start|stop}"
        exit 1
        ;;
esac

exit 0