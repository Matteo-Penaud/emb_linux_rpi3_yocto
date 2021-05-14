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
cd <BUILD_DIR>
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
cd <BUILD_DIR>
git clone https://github.com/Matteo-Penaud/emb_linux_rpi3_yocto.git
```
<br><br>

# How to build
1) Source the build environment :
```
source poky/oe-init-build-env ./rpi3-build
```
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
```m
MACHINE = "raspberrypi3"
IMAGE_FSTYPE = "rpi-sdimg"
```
4) uncomment this lines :
```m
DL_DIR ?= "${TOPDIR}/downloads"
SSTATE_DIR ?= "${TOPDIR}/sstate-cache"
TMPDIR = "${TOPDIR}/tmp"
```
5) Add this at the and of the file :
```m
BB_NUMBER_THREADS = "${nproc}"
PARALLEL_MAKE = "-j ${nproc}"

DISTRO_FEATURES_append = " virtualization"
```
---
6) You can now start the build by running :
```
bitbake ynov-rpi3-image
```
The building time will vary depending on your build machine and your internet bandwidth.