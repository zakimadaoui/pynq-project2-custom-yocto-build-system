# TODOs
[x] i need a startup script that loads the bitstream overlay
[ ] clean up this file and organize learned knowledge and project description...
[ ] for now development flow is still sub-optimal so next we need to figure out ways to improve iterations
    - a script for creating a fully working sd-card and flashing the wic image
    - a script for pusing the majority of stuff over ssh
[ ] maybe a diagram of the boot process and the general bsp arch

# Requirements
Create a yocto-based build system for creating a linux distribution for the pynqz2
- must be possible to create a minimal linux system.
- must be possible to include a bitstream
- must be possible to easily update the device tree
- must be possible to use either SD card or TFTP for just the linux kernel and other stuff

- create a distro conf file that:
    - reduces unneeded junk
    - remove the poky printed text at the start and reply with my own
- create an image recipe that includes
    - add needed utilities such as ssh, network stuff, strace (good for kernel moduel debuging from userspace calls), nano, gpio tools and kenel support for that if needed
    - set passowrd ?

- be able to ssh over ethernet
- be able to load fpga overlay and control leds

<!-- TODO after this -->
learn more about xilinx hardware
do some fpga development and device tree changes
continue improving the development flow
secure boot and arm trusted zone


# flash wic to sd-card
```bash
sudo umount /dev/mmcblk0*
sudo bmaptool copy --nobmap pynq-image-pynq-z2.rootfs.wic /dev/mmcblk0 # sudo apt install bmap-tools
sudo umount /dev/mmcblk0*
```
# See wic image contents
```
wic ls build/tmp/deploy/images/pynq-z2/pynq-image-pynq-z2.rootfs.wic:1
```


# Lessons learned
- for some reason. Bootbin receipe is able to get u-boot.elf, but u-boot do-delploy is not getting called as usual so this makes me wonder a bit ...
 
- building the u-boot recipe idependently produces the output files i want to see in the deploy dir, but building full image doens't...
 
-> answer:
this is because xilinx-boot bin image is the only thing that depends on uboot. So uboot is build and placed in the sysroot or the bootbin recipes work directory instead of the global deploy dir :)

Soo: when a recipe A DEPENDS on another recipe B. Then recipe B will generate files which will make it to recipies A  RECIPE_SYSROOT folder
this is how u-boot.elf is visible to xilinx-bootbin, because xilinx-bootbin DEPENDS on "u-boot-xlnx" recipe which provides u-boot.elf in the correct place
if you want u-boot to provide its stuff to the deploy dir, you should add "u-boot-xlnx" or "virtual/bootloader" to EXTRA_IMAGEDEPENDS in your machine file and also maybe u-boot.elf to  IMAGE_BOOT_FILES if you want them to make it to the wic

## Important concept of how recipes work: 

https://docs.yoctoproject.org/dev-manual/new-recipe.html#sharing-files-between-recipes

Q: where they generate files, which env variables in the recipe point to those files and which env variables in the DEPending recipes that also point to paths where copies of those files are available.

- where does do_fetch put files ? is it `B` or `WORKDIR` ?
- where does do_compile and do_configure put dir `D` i.e destination dir
- where does do_install produce files dir `D` i.e destination dir
- how does FILES variable affect do sysfs
- where does do_deploy put files ?



## How Recipes Get Bound to a Machine and Contribute to Building a Bootable Image

There are **two main categories**:

1. **Build-time dependencies**
2. **Image runtime contents**

---

## 📂 1️⃣ Build-Time Dependencies (needed to assemble the bootable image)

### ✅ `EXTRA_IMAGEDEPENDS`

* Adds recipes **that must be built before the image is assembled**
* Typical use: bootloader, FSBL, bitstream, custom tools
* Declared in your `conf/machine/<machine>.conf`

**Example**

```conf
EXTRA_IMAGEDEPENDS += "u-boot fsbl bitstream-generator"
```

Means: before generating the image, BitBake ensures `u-boot`, `fsbl`, and `bitstream-generator` are built.

---

### ✅ `WKS_FILE_DEPENDS`

* If you use **Wic (image creator)** to generate your bootable images from a `.wks` kickstart file, and if your `.wks` file depends on artifacts (like bootloader binaries), you declare those recipes here.

**Example**

```conf
WKS_FILE_DEPENDS += "u-boot"
```

This ensures `u-boot` is built before your `.wks` file image creation process runs.

---

## 📂 2️⃣ Runtime Dependencies (packages installed into the rootfs)

### ✅ `MACHINE_ESSENTIAL_EXTRA_RDEPENDS`

* Declares **runtime packages** that are machine-essential — installed into the target rootfs by default
* Typically used for machine-critical components like device-tree overlays, board support utils, custom scripts
* Declared in `conf/machine/<machine>.conf`

**Example**

```conf
MACHINE_ESSENTIAL_EXTRA_RDEPENDS += "board-monitor board-firmware"
```

These recipes must produce installable packages (via `PACKAGES` in their recipes), which are then included in the rootfs at image creation time.

---

## 📂 3️⃣ (Optionally) Image Contents via Image Recipe Variables

In your custom image recipe:

```bitbake
IMAGE_INSTALL += "my-custom-app another-package"
```

To pull in extra runtime content.
**But this is at the image recipe level, not machine level.**

---

## 📌 Clean Recap Table

| Purpose                          | Variable                           | Where It Acts  |
| :------------------------------- | :--------------------------------- | :------------- |
| Build-time dependency for image  | `EXTRA_IMAGEDEPENDS`               | Machine  config |
| Build-time dependency for Wic    | `WKS_FILE_DEPENDS`                 | Machine config |
| Installed into rootfs at runtime | `MACHINE_ESSENTIAL_EXTRA_RDEPENDS` | Machine config |
| Installed via image recipe       | `IMAGE_INSTALL`                    | Image recipe   |

---




