# Wireless Redstone

[![GitHub Builds](https://img.shields.io/github/actions/workflow/status/Razzokk/WirelessRedstone/ci.yml?style=flat-square&logo=github)][github-builds]
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/wirelessredstone?style=flat-square&logo=modrinth)][modrinth]
[![Modrinth Versions](https://img.shields.io/modrinth/game-versions/wirelessredstone?style=flat-square)][modrinth]
[![CurseForge Downloads](https://cf.way2muchnoise.eu/wirelessredstone.svg?badge_style=flat)][curseforge]
[![CurseForge Versions](https://cf.way2muchnoise.eu/versions/wirelessredstone.svg?badge_style=flat)][curseforge]

Wireless Redstone adds Redstone components to Minecraft that enable controlling Redstone components remotely.
The concept works like any transmitter/receiver functionality.

**Note**: If you press shift in the frequency GUI all the values to change the frequency are multiplied by 100 (so you can add/subtract 100 and 1000) or you just manually type in your desired frequency.
You can also change the color of the frequency display on a receiver/transmitter in the configs.
Same goes for the highlighting with the frequency sniffer!

## Content

### Redstone Transmitter

The Redstone Transmitter is transmitting a signal into the Redstone Network as soon as it gets turned on via a redstone signal and sets all Receivers on the same frequency also to high state.
If there is more than one Transmitter transmitting on the same frequency then all Receivers on that frequency are high while at least one Transmitter on that frequency is on.
This block can be right-clicked to set the frequency.

### Redstone Receiver

The Redstone Receiver outputs a redstone signal of 15 while there is at least one active Transmitter on the same frequency.
This block can be right-clicked to set the frequency.

### Remote

The Remote is the Item form of the Transmitter, while holding right-click the remote signal goes high.
But it comes with the addition to copy the Frequency of a Transmitter or a Receiver by sneaking and right-click them.
By sneak right-clicking the Remote (not onto a Transmitter/Receiver), you can set the frequency via the gui.

### Frequency Tool

The Frequency Tool is a handy tool that makes it easy for you to take a frequency and set multiple Transmitters and/or Receivers to the same frequency.
When holding it in your hand you can copy a frequency into the Frequency Tool by sneaking and right-clicking onto a Transmitter or Receivers.
To than copy the now stored frequency, simply right-click either a Transmitter or Receiver.
By sneak right-clicking the Frequency Tool (not onto a Transmitter/Receiver), you can set the frequency via the gui.

### Frequency Sniffer

The Frequency Sniffer can be used as a finding tool for active transmitter on a given frequency.
As with other frequency tools too you can set the frequency by either manually via the sneak-rightclick gui or by sneak-rightclick a receiver/transmitter.
By rightclicking this sniffer tool all active transmitters on the set frequency in range will be highlighted with a wireframe around the transmitter block.
There is also a chat message telling where the all active transmitters are located at by showing the blockposition.
If you are admin/op too you can also click on the blockposition to teleport there. Example in the screenshot area.
You can also change the highlight color in the configs!


[github-builds]: https://github.com/Razzokk/Wireless-Redstone/actions
[modrinth]: https://modrinth.com/mod/wirelessredstone
[curseforge]: https://curseforge.com/minecraft/mc-mods/wirelessredstone
