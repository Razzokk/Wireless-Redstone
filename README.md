# Wireless Redstone

[![GitHub Builds](https://img.shields.io/github/actions/workflow/status/Razzokk/wireless-redstone/ci.yml?style=flat-square&logo=github)][github-builds]
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/wirelessredstone?style=flat-square&logo=modrinth)][modrinth]
[![Modrinth Versions](https://img.shields.io/modrinth/game-versions/wirelessredstone?style=flat-square)][modrinth]
[![CurseForge Downloads](https://cf.way2muchnoise.eu/wirelessredstone.svg?badge_style=flat)][curseforge]
[![CurseForge Versions](https://cf.way2muchnoise.eu/versions/wirelessredstone.svg?badge_style=flat)][curseforge]

<p align="center">
	<img alt="Wireless Redstone" src="resources/logo.png" width="500px" />
</p>

Wireless Redstone adds Redstone components to Minecraft, allowing Redstone components to be controlled remotely. The
concept works like any transceiver functionality.

**Note**: If you press `Shift` in the Frequency GUI, all values to change the frequency will be multiplied by 100 (so
you can add/subtract 100 and 1000) or you can just type in your desired frequency manually. You can also change the
color of the frequency display on a receiver/transmitter in the Configs. The same goes for highlighting with the
Frequency Sniffer!

## Content

### Redstone Transmitter

The **Redstone Transmitter** transmits a signal to the Redstone Network when it is turned on by a Redstone signal and
sets all receivers on the same frequency to high as well. If there is more than one transmitter transmitting on the same
frequency, all receivers on that frequency will be high as long as at least one transmitter on that frequency is on.
This block can be right-clicked to set the frequency.

### Redstone Receiver

The **Redstone Receiver** will output a Redstone signal of 15 as long as there is at least one active transmitter on the
same frequency. This block can be right-clicked to set the frequency.

### Remote

The **Remote** is the item form of the Transmitter, while holding right-click the remote signal goes high. But it comes
with the addition of being able to copy the frequency of a Transmitter or a Receiver by sneaking and right-clicking it.
By sneaking and right-clicking the remote (not on a transmitter/receiver), you can set the frequency through the GUI.

### Frequency Tool

The **Frequency Tool** is a handy tool that makes it easy for you to take a frequency and set multiple transmitters
and/or receivers to the same frequency. When holding it in your hand you can copy a frequency into the Frequency Tool by
sneaking and right-clicking on a transmitter or receivers. To copy the now-stored frequency, simply right-click either a
transmitter or receiver. By sneaking and right-clicking the frequency tool (not on a transmitter/receiver), you can set
the frequency through the GUI.

### Frequency Sniffer

The **Frequency Sniffer** can be used as a tool to find active transmitters on a given frequency. As with other
frequency tools too you can set the frequency either manually over the gui or copying it from a transmitter/receiver.
When you right-click the sniffer tool all active transmitters on the set frequency within range are highlighted with a
wireframe around the transmitter block. There is also a chat message that tells you where all the active transmitters
are located at by showing the block position. If you are an admin/op, you can also click on the block position to
teleport there. See example in the screenshot section. You can also change the highlight color in the configs!

### Circuit

The **Circuit** is for crafting purposes only.

[github-builds]: https://github.com/Razzokk/wireless-redstone/actions
[modrinth]: https://modrinth.com/mod/wirelessredstone
[curseforge]: https://curseforge.com/minecraft/mc-mods/wirelessredstone
