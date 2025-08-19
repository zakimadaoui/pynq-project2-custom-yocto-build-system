#!/bin/bash

set -ex

mount_dir=$(mktemp -d) 
SD_DEV=$1

if [[ -z "$SD_DEV" ]]; then
    echo "You must define SD_DEV variable to point to the sdcard device. Eg. SD_DEV=/dev/mmcblk0"
    exit 1
fi

sudo mount ${SD_DEV}p1 ${mount_dir} 
cp build/tmp/deploy/images/pynq-z2/fusing-app-BOOT.bin ${mount_dir} 
sync 
sudo umount ${mount_dir} 
rm -rf ${mount_dir}