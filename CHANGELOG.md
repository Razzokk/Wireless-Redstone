### Version 1.12.2-1.1.0:

- ADDED: Frequency Sniffer
- ADDED: Config option to color the frequency that is displayed on transmitters/receivers
- ADDED: Command to clear a frequency from active transmitters (Only use this if the frequency is "stuck")
- ADDED: Wireless Redstone blocks can now be mined faster with a pickaxe
- ADDED: Cooldown for the remote item (0.5s)
- CHANGED: Circuit recipe output from 4 to 2
- FIXED: Potential crashes caused by Remote::onUpdate and BlockFrequency::setPoweredState
- FIXED: Actual values for the frequency between 0 and 65536

### Version 1.12.2-1.0.0:

- FIXED: Active frequency stays active after transmitter exploded

### Version 1.12.2-0.0.1b:

- Initial 1.12.2 version
- RENAMED: Wireless Transmitter -> Redstone Transmitter
- RENAMED: Wireless Receiver -> Redstone Receiver
- RENAMED: Wireless Remote -> Remote
- RENAMED: Wireless Circuit -> Circuit
- RENAMED: Frequency Copier -> Frequency Tool
