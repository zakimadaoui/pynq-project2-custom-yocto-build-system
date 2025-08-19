# Custom Yocto Meta layer for the pynq-z2 

## Learning objectives
Create a yocto-based build system for building a custom linux distribution for the pynqz2 by:
- finding and composing all necessary meta layers
- create a custom meta layer for providing pynq-z2- specific BSP support
- create a custom machine file tailored for the pynqz2 
- create a custom image recipe
- enable incremental fpga bitstream development by enabling device tree overlays for the PL Logic 

## Build
```bash
source init.sh
bitbake pynq-image
```

## Todo
This project is still work in progress...

[ ] Finalize/Push Secure Boot Implementation

[ ] Bring optee support from project1 to this yocto build