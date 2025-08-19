
SD_DEV ?="/dev/mmcblk0"

env: 
ifeq ($(PYNQ_ENV_READY), 1)
	@echo "Pynq build environment OK"
else
	@echo "You must initialize the build environment first using 'source init.sh'"
	exit 1
endif

# Prepare an SD card for booting one of the images
# This will wipe the SD card and create 2 Partitions 
# Boot partion  : FAT32, contains boot files (bootloaders, kernel, initramfs)
# Data partition: ext4, partion for storing data and configuration
sd:
	@echo todo

clean_sd:
	@echo todo

# Build a development linux image (Non secure)
linux_dev:
	@echo todo

# Build a Secure linux image
linux_secure:
	@echo todo 

# Deploy a linux image to an SD card 
deploy_linux: clean_sd
	@echo todo 

# Builds an image for fusing the zynq SoC to enable Secure Boot features
fusing_image:
	@QYNQ_FSBL_DEBUG_ENABLE=1 BB_ENV_PASSTHROUGH_ADDITIONS="$(BB_ENV_PASSTHROUGH_ADDITIONS) QYNQ_FSBL_DEBUG_ENABLE" bitbake pynq-fusing-image
	mkdir -p artifacts/fusing_image
	cp build/tmp/deploy/images/pynq-z2/fusing-app-BOOT.bin artifacts/fusing_image

deploy_fusing:
	scripts/deploy_fusing.sh $(SD_DEV)


.PHONY: env all 


