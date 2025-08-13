require recipes-core/images/core-image-minimal.bb

# Good to know IMAGE_FEATURES:
#
# - read-only-rootfs    - tweaks an image to support read-only rootfs
# - weston              - Weston Wayland compositor
# - x11                 - X server
# - x11-base            - X server with minimal environment
# - x11-sato            - OpenedHand Sato environment
# - tools-debug         - debugging tools
# - tools-profile       - profiling tools
# - tools-testapps      - tools usable to make some device tests
# - tools-sdk           - SDK (C/C++ compiler, autotools, etc.)
# - nfs-server          - NFS server
# - nfs-client          - NFS client
# - ssh-server-dropbear - SSH server (dropbear)
# - ssh-server-openssh  - SSH server (openssh)
# - package-management  - installs package management tools and preserves the package manager database
# - debug-tweaks        - makes an image suitable for development, e.g. allowing passwordless root logins
#   - empty-root-password
#   - allow-empty-password
#   - allow-root-login
#   - post-install-logging
# - serial-autologin-root - with 'empty-root-password': autologin 'root' on the serial console
# - dev-pkgs            - development packages (headers, etc.) for all installed packages in the rootfs
# - dbg-pkgs            - debug symbol packages for all installed packages in the rootfs
# - bash-completion-pkgs - bash-completion packages for recipes using bash-completion bbclass
# - ptest-pkgs          - ptest packages for all ptest-enabled recipes
# - splash              - bootup splash screen
#

IMAGE_FEATURES += "allow-root-login ssh-server-openssh"



IMAGE_INSTALL:append = " nano libgpiod libgpiod-tools iputils"
# libgpiod and libgpiod-tools provide utilities for tinkering with gpios
# gpiodetect                            # List GPIO controllers
# gpioinfo -c gpiochip0                 # Show all lines for gpiochip0
# gpioget  -c gpiochip0 21              # Read GPIO line 21
# gpioset  -c gpiochip0 21=1            # Set GPIO line 21 high
# gpiomon  -c gpiochip0 -e rising 21    # Monitor rising-edge events

# add a script that facilitates loading bitstream and PL device tree overlay
IMAGE_INSTALL:append = " bitstream-load"

IMAGE_INSTALL_DEBUGGING = "tools-debug tools-profile"
# TODO: enable this if you need to debug or profile something
# IMAGE_INSTALL:append = " ${IMAGE_INSTALL_DEBUGGING}"

# Users and passwords
# hashed password
# sudo apt install whois && printf "%q\n" $(mkpasswd -m sha256crypt root)
inherit extrausers
ROOTPWD = "\$5\$r7LfFZPt1wE4ilXX\$r106b70e3dAktxohttC1lvJ3yvth70PcQ/sfV8iF.CD"
EXTRA_USERS_PARAMS = "usermod -p '${ROOTPWD}' root;"