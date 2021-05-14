DESCRIPTION = "Hello Ynov programme"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

# File name + md5
SRC_URI = "file://hello.c;md5=2a50399b3f3ef3f906711f997cde50f4"

S = "${WORKDIR}"

do_compile() {
	${CC} hello.c ${LDFLAGS} -o hello
}

# Install hello in /usr/bin
do_install() {
	install -d ${D}${bindir}
        install -m 0755 hello ${D}${bindir}
}
