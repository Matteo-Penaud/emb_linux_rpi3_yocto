# Prerequisites
## Preparing the environment
First thing first, grab your brain, get a mug of coffee (or 2), and take your cat out (otherwise you may end with a lot of issues, trust me).
<br><br>
You're good ?

Let's begin !
<br><br>
Make sure your build host meets the following requirements (https://docs.yoctoproject.org/brief-yoctoprojectqs/index.html) :
- 50 GB of free disk space
- Runs a supported Linux distib (recent release of Fedora, openSUSE, CentOS, Debian, or Ubuntu)
- Has those packages installed :
    - Git 1.8.3.1 or greater
    - tar 1.28 or greater
    - Python 3.6.0 or greater
    - gcc 5.0 or greater
<br><br>
1) Install those packages :

```
sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint3 xterm python3-subunit mesa-common-dev make python3-pip
```

2) Clone the Poky repository from git :
```
cd <PROJECT_DIR>
git clone -b gatesgarth git://git.yoctoproject.org/poky.git ./poky
```

3) Clone the needed layers :
```
mkdir layers
cd layers
git clone -b gatesgarth git://git.openembedded.org/meta-openembedded
git clone -b gatesgarth git://git.yoctoproject.org/meta-raspberrypi
git clone -b gatesgarth git://git.yoctoproject.org/meta-virtualization
```

4) Clone this repo and copy the folders where they should be :
```
cd <PROJECT_DIR>
git clone https://github.com/Matteo-Penaud/emb_linux_rpi3_yocto.git
```
<br><br>

# How to build
1) Source the build environment :
```
source poky/oe-init-build-env ./rpi3-build
```
From here, you have 2 methods :

## 1 - Use this conf folder
You can replace the conf folder of your build directory by the one in this repo.

It's configurated FOR MY SYSTEM, so you will have some changes to do :

2) Modify the layers path in the bblayers.conf file to match your environment

3) At the very end of the local.conf file, change the CPU thread count to match your build host.


## 2 - DIY!
I prefer this one, it's better to learn !

2) Add the layers:
```
bitbake-layers add-layer ../layers/meta-raspberrypi
bitbake-layers add-layer ../layers/meta-openembedded/meta-oe
bitbake-layers add-layer ../layers/meta-openembedded/meta-python
bitbake-layers add-layer ../layers/meta-openembedded/meta-networking
bitbake-layers add-layer ../layers/meta-openembedded/meta-filesystems
bitbake-layers add-layer ../layers/meta-openembedded/meta-multimedia
bitbake-layers add-layer ../layers/meta-virtualization
bitbake-layers add-layer ../emb_linux_rpi3_yocto/meta-ynov-rpi3
```
### In the file conf/local.conf :
3) change the machine variable to "raspberrypi3" (or "raspberrypi4" for RPi 4), and add IMAGE_FSTYPE variable with the value "rpi-sdimg". You should have this :
```
MACHINE = "raspberrypi3"
IMAGE_FSTYPES = "rpi-sdimg"
```
4) uncomment this lines :
```
DL_DIR ?= "${TOPDIR}/downloads"
SSTATE_DIR ?= "${TOPDIR}/sstate-cache"
TMPDIR = "${TOPDIR}/tmp"
```
5) Add this at the and of the file :
```conf
# Enable multi-tasking during the build. Replace the "4" by your CPU's core count (run 'nproc' if you don't know it)
# Ex for a 4c/8t CPU : 
#   BB_NUMBER_THREADS = "8"
#   PARALLEL_MAKE = "-j 8"
BB_NUMBER_THREADS = "4"
PARALLEL_MAKE = "-j 4"

# Enables virtualization in the image (in order to use docker)
DISTRO_FEATURES_append = " virtualization"
```
---
6) You can now start the build by running :
```
bitbake ynov-rpi3-image
```
The building time will vary depending on your build machine and your internet bandwidth.

<br><br>
# Flash the image
When you're done with the build, you can flash your sdcard with your image :
```
cd <BUILD_DIR>/tmp/deploy/images/raspberrypi3 (or raspberrypi4)
sudo dd if=ynov-rpi3-image-raspberrypi3.rpi-sdimg of=/dev/sdX
```
Replace the X in sdX by the letter of your sdcard.
You might need to do a ```sync``` in order to write on the sdcard.

Once the RPi booted up, login with root (no password required).

To check if the hello-world layer is correctly installed and configured, run the hello command :
```
hello
```
It should returns "Hello World from Ynov (Matteo PENAUD)!".

<br><br>
# Known issues
Something I noticed will testing this build is that sometimes, the boot ends up with "Kernel panic" error.

To correct it, try formating your sdcard (Free space) and reflashing.