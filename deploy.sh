#!/bin/sh -eu

sudo apt update -y && sudo apt upgrade -y

sudo apt install -y \
    qemu \
    ebtables \
    dnsmasq-base \
    vagrant \
    libxslt-dev \
    libvirt-dev \
    ruby-dev

vagrant plugin install vagrant-libvirt

cat > vagrant-libvirt.xml <<EOF
<network connections='1'>
  <name>vagrant-libvirt</name>
  <forward mode='nat'>
    <nat>
      <port start='1024' end='65535'/>
    </nat>
  </forward>
  <bridge name='virbr1' stp='on' delay='0'/>
  <ip address='192.168.121.1' netmask='255.255.255.0'>
    <dhcp>
      <range start='192.168.121.2' end='192.168.121.254'/>
    </dhcp>
  </ip>
</network>
EOF

virsh net-define vagrant-libvirt.xml
virsh net-start vagrant-libvirt
virsh net-autostart vagrant-libvirt
