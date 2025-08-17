# Custom Yocto Meta layer for the pynq-z2 

# TODOs
[ ] for now development flow is still sub-optimal so next we need to figure out ways to improve iterations
    - a script for creating a fully working sd-card and flashing the wic image
    - a script for pusing the majority of stuff over ssh
[ ] maybe a diagram of the boot process and the general bsp arch

This is an educational project for the following learning objectives:
## Learning objectives
Create a yocto-based build system for building a custom linux distribution for the pynqz2 by:
- finding and composing all necessary meta layers
- create a custom meta layer for providing pynq-z2- specific BSP support
- create a custom machine file tailored for the pynqz2 
- create a custom image recipe
- enable increment fpga bitstream development by enabling device tree overlays for the PL Logic 

## Build
```bash
source init.sh
bitbake pynq-image
```

## Boot files
Boot files will be placed in build/tmp/deploy/pynq-z2/
- TODO 

The build system will also generate a wic image ready to be flashed directly to an SD card

## flash wic to sd-card
```bash
sudo umount /dev/mmcblk0*
sudo bmaptool copy --nobmap pynq-image-pynq-z2.rootfs.wic /dev/mmcblk0 # sudo apt install bmap-tools
sudo umount /dev/mmcblk0*
```
## See wic image contents
```
wic ls build/tmp/deploy/images/pynq-z2/pynq-image-pynq-z2.rootfs.wic:1
```


# Lessons learned

## How Recipes Get Bound to a Machine and Contribute to Building a Bootable Image


###  `EXTRA_IMAGEDEPENDS`

* Adds recipes **that must be built before the image is assembled**
* Typical use: bootloader, FSBL, bitstream, custom tools
* Declared in your `conf/machine/<machine>.conf`

**Example**

```conf
EXTRA_IMAGEDEPENDS += "u-boot fsbl bitstream-generator"
```

Means: before generating the image, BitBake ensures `u-boot`, `fsbl`, and `bitstream-generator` are built.

---

### `WKS_FILE_DEPENDS`

* If you use **Wic (image creator)** to generate your bootable images from a `.wks` kickstart file, and if your `.wks` file depends on artifacts (like bootloader binaries), you declare those recipes here.

**Example**

```conf
WKS_FILE_DEPENDS += "u-boot"
```

This ensures `u-boot` is built before your `.wks` file image creation process runs.

---

## Runtime Dependencies (packages installed into the rootfs)

###  `MACHINE_ESSENTIAL_EXTRA_RDEPENDS`

* Declares **runtime packages** that are machine-essential — installed into the target rootfs by default
* Typically used for machine-critical components like device-tree overlays, board support utils, custom scripts
* Declared in `conf/machine/<machine>.conf`

**Example**

```conf
MACHINE_ESSENTIAL_EXTRA_RDEPENDS += "board-monitor board-firmware"
```

These recipes must produce installable packages (via `PACKAGES` in their recipes), which are then included in the rootfs at image creation time.