### Version 1.16.5-1.1.4:

- FIX: Frequencies getting corrupted and receivers/transmitters not updating anymore

### Version 1.16.5-1.1.3:
skipped

### Version 1.16.5-1.1.2:

- FIXED: Gui closable with inventory key

### Version 1.16.5-1.1.1:

- FIXED: Post-placement state changes

### Version 1.16.5-1.1.0:

- UPDATED: to MC 1.16.5
- NOTE: Due to huge internal changes I suggest making a world backup!
- ADDED: Frequency Sniffer
- ADDED: Config option to color the frequency that is displayed on transmitters/receivers
- ADDED: Command to clear a frequency from active transmitters (Only use this if the frequency is "stuck")
- ADDED: Wireless Redstone blocks can now be mined faster with a pickaxe
- ADDED: Cooldown for the remote item (0.5s)
- CHANGED: Circuit recipe output from 4 to 2
- FIXED: Potential crashes caused by Remote::onUpdate and BlockFrequency::setPoweredState
- FIXED: Actual values for the frequency between 0 and 65536

- RENAMED: Wireless Transmitter -> Redstone Transmitter
- RENAMED: Wireless Receiver -> Redstone Receiver
- RENAMED: Wireless Remote -> Remote
- RENAMED: Wireless Circuit -> Circuit
- RENAMED: Frequency Copier -> Frequency Tool

### Version 1.16.3-1.0.1:

- FIXED: Crash on tile entity write

### Version 1.16.3-1.0.0:

- FIXED: Blocks not dropping when destroyed
- Internal code refactoring

### Version 1.16.3-0.0.2b:

- FIXED: bug where the frequency was not set when using the frequency gui

### Version 1.16.3-0.0.1b:

- Initial 1.16.3 version, ported from 1.15.2