#!/bin/sh

BITSTREAM="/boot/download-pynq-z2.bit.bin"
OVERLAY="/boot/devicetree/pl.dtbo"
MAX_RETRIES=3
DELAY_SEC=1

# Wait for FPGA manager
retry=0
while [ ! -d /sys/class/fpga_manager/fpga0 ] && [ $retry -lt $MAX_RETRIES ]; do
    sleep $DELAY_SEC
    retry=$((retry+1))
done

if [ ! -d /sys/class/fpga_manager/fpga0 ]; then
    echo "FPGA manager not found after $MAX_RETRIES attempts"
    exit 1
fi

# Load bitstream
echo "Loading FPGA bitstream: $BITSTREAM"
if ! fpgautil -b "$BITSTREAM" -o "$OVERLAY" -f Full -n full; then
    echo "Failed to load bitstream"
    exit 1
fi

# Verify
if ! grep -q "operating" /sys/class/fpga_manager/fpga0/state; then
    echo "FPGA not in operating state"
    exit 1
fi

echo "FPGA configured successfully"
exit 0