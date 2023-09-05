VM_CPUS = 8
VM_MEMORY = 16384

Vagrant.configure('2') do |config|
  $script = <<-SHELL
    # Install packages
    sudo apt update -y
    sudo apt upgrade -y
    sudo apt install -y \
      ca-certificates \
      curl \
      gnupg \
      maven \
      graphviz
    sudo install -m 0755 -d /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/debian/gpg | \
      sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    sudo chmod a+r /etc/apt/keyrings/docker.gpg
    echo \
      "deb [arch="$(dpkg --print-architecture)" signed-by=/etc/apt/keyrings/docker.gpg] \
      https://download.docker.com/linux/debian \
      "$(. /etc/os-release && echo "$VERSION_CODENAME")" stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub > google_key.pub
    sudo install -D -o root -g root -m 644 google_key.pub /etc/apt/keyrings/google_key.pub
    echo "deb [arch=amd64 signed-by=/etc/apt/keyrings/google_key.pub] \
          http://dl.google.com/linux/chrome/deb/ stable main" | \
          sudo tee /etc/apt/sources.list.d/google-chrome.list
    rm google_key.pub
    sudo apt update -y
    sudo apt-get install -y \
      docker-ce \
      docker-ce-cli \
      containerd.io \
      google-chrome-stable \
      firefox-esr \
      xvfb
    sudo apt-get autoremove -y
    sudo usermod -aG docker vagrant
    cat <<EOF | sudo tee /etc/systemd/system/x11.service
[Unit]
Description=X11 Display service
After=network.target
StartLimitIntervalSec=0

[Service]
Type=simple
Restart=always
RestartSec=1
User=vagrant
ExecStart=Xvfb :0 -ac -screen 0 1024x768x24

[Install]
WantedBy=multi-user.target
EOF
    sudo systemctl daemon-reload
    sudo systemctl enable x11
    sudo systemctl start x11


    # Install WordNet
    mkdir Desktop
    cd Desktop
    curl -sO https://wordnetcode.princeton.edu/3.0/WordNet-3.0.tar.gz
    tar xvf WordNet-3.0.tar.gz
    rm -rf WordNet-3.0.tar.gz

    # Setup application
    mkdir -p ~/workspace
    ln -s /vagrant ~/workspace/FSE19-submission-material
    cd ~/workspace/FSE19-submission-material/tedd/src/main/resources
    cp app.example.properties app.properties
    sed -i 's|/home/anonymous|/home/vagrant|g' app.properties
    echo 'export DISPLAY=:0' >> ~/.bashrc
  SHELL

  config.vm.box = 'debian/bullseye64'

  config.vm.provider 'libvirt' do |v|
    v.memory = VM_MEMORY
    v.cpus = VM_CPUS
  end

  config.vm.provider 'virtualbox' do |v, override|
    v.memory = VM_MEMORY
    v.cpus = VM_CPUS
    override.vm.synced_folder '.', '/vagrant', type: 'virtualbox'
  end

  config.vm.provision :shell, inline: $script, privileged: false
end
