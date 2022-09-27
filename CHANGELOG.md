### Version 1.12.2-1.1.4:

- FIX: receivers/transmitters before mod version 1.12.2-1.1.3 breaking when using mod version >= 1.12.2-1.1.3

### Version 1.12.2-1.1.3:

WARNING: This version will break working transmitters/receivers from earlier versions of this mod

- FIX: Frequencies getting corrupted and receivers/transmitters not updating anymore

### Version 1.12.2-1.1.2:

- FIXED: Gui closable with inventory key

### Version 1.12.2-1.1.1:

- FIXED: NBT data used correctly when placing nbt stored receiver/transmitter in world
- FIXED: (Project Red interaction) Red alloy wire can now be placed on the side of receiver/transmitter

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
