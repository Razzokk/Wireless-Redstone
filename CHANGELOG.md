# Changelog

Format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
versioning follows [Semantic Versioning](https://semver.org/spec/v2.0.0.html)
with the minecraft version prepended to it.

## [Unreleased]

### Fixed

- Forge & NeoForge: redstone wires connecting up or down to transmitters/receivers
- Fabric: redstone wires connecting up or down to redstone utility blocks

### Added

- P2P redstone transmitter and receiver for direct wireless linkage
	- Show target of p2p transmitters when picked up with nbt data
- Linker to link p2p transmitter and receiver

### Changed

- Redstone transmitters and receivers now use the `cube_column` block model instead of `cube_bottom_top`
	- Why? The bottom and top textures have been the same since the initial release,
	  and the corresponding textures have also been removed.
	- If you have a custom resource pack for Wireless Redstone, you can still use the `cube_bottom_top` model,
	  but you will need to provide your own model files, see [Model](https://minecraft.wiki/w/Model).
- Highlight renderers now use the outline of the block instead of just a 1x1x1 block

## [1.20.4-1.1.5] - 2024-07-09

### Fixed

- Forge & NeoForge: race condition on client setup -> item property registration
- Dev only:
	- Forge: not starting in dev environment (module conflicts)

## [1.20.4-1.1.4] - 2024-04-30

### Fixed

- Sniffer not outputting message on newly created world (no redstone ether present)

### Added

- Show frequency of transmitters and receivers when picked up with nbt data

### Changed

- Updated german translation
- Moved block textures to their respective directories
	- If you have a custom resource pack for Wireless Redstone, you have to move and rename them too
- Sniffer chat messages:
	- Changed style of how block positions are shown in chat (can now also be adjusted in lang file)
	- From: `There are no active transmitters on this frequency (<frequency>)`,
	  to: `No active transmitters on frequency <frequency>`
	- From: `Active transmitters on this frequency (<frequency>): <transmitters>`,
	  to: `Active transmitters on frequency <frequency>: <transmitters>`
- Dev only:
	- Separate datagen sub-project
	- Update architectury loom to 1.6
	- Update changelog plugin to 2.2.0
	- Update fabric loader and api to 0.15.10 and 0.97.0+1.20.4 respectively
	- Update forge to 49.0.49
	- Update neoforged to 20.4.233

## [1.20.4-1.1.3] - 2024-03-19

### Added

- [NeoForged](https://neoforged.net/) support

### Changed

- Refactored/cleaned up networking classes

## [1.20.4-1.1.2] - 2024-03-12

Port to MC 1.20.4

## [1.20.1-1.1.2] - 2024-03-12

### Changed

- `No frequency set` message now shows over the action bar
- GitHub repository url is now: https://github.com/Razzokk/wireless-redstone
- Moved icon from `resources/` to `resources/assets/wirelessredstone/`
- Dev only:
	- Yarn mappings for fabric **and** forge (using
	  [Architectury Loom](https://docs.architectury.dev/loom/introduction))
	- Extracted most of the code into the common project -> less fabric and forge specific code
	- Update forge to 47.2.20
	- Update fabric loader and api to 0.15.6 and 0.92.0+1.20.1 respectively
	- Use gradle v8.6

### Added

- Dev only: [Architectury Loom](https://docs.architectury.dev/loom/introduction) for easier multi-loader setup

## [1.20.1-1.1.1] - 2023-07-17

Port 1.20 fabric version to 1.20 forge.

### Changed

- Setup multi-loader repository structure

## [1.20.1-1.1.0] - 2023-07-06

### Added

- Remote item - to power things from inside your inventory!
- Keybinding in frequency GUI -> when editing the frequency, pressing `Enter` closes the GUI and saves the frequency

## [1.20.1-1.0.2] - 2023-06-29

### Added

- Data generation

### Fixed

- Updating strong powered indirectly neighbored redstone components after removal of receiver

## [1.20.1-1.0.1] - 2023-06-27

### Added

- Proper `README.md`
- Automated Modrinth and CurseForge publishing with the [Minotaur](https://github.com/modrinth/minotaur)
  and [CurseForgeGradle](https://github.com/Darkhax/CurseForgeGradle) gradle tasks/plugins
- Changelog semi-automation with the [Gradle Changelog Plugin](https://github.com/JetBrains/gradle-changelog-plugin)
- Fancy [shields.io](https://shields.io/) badges to `README.md`
- Uploading of build artifacts via GitHub actions

### Changed

- Update gradle and fabric-loom
- Separate client more strictly
- Updated link of the homepage, source and issues websites

### Fixed

- Stack size of frequency items limited to 1

## [1.20.1-1.0.0] - 2023-06-16

Port to MC 1.20.1

### Changed

- Increased contrast of T and R letters on transmitter and receiver texture

[Unreleased]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.4-1.1.5...HEAD
[1.20.4-1.1.3]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.4-1.1.2...release/1.20.4-1.1.3
[1.20.4-1.1.2]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.1.2...release/1.20.4-1.1.2
[1.20.4-1.1.4]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.4-1.1.3...release/1.20.4-1.1.4
[1.20.4-1.1.5]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.4-1.1.4...release/1.20.4-1.1.5
[1.20.1-1.1.2]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.1.1...release/1.20.1-1.1.2
[1.20.1-1.1.0]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.0.2...release/1.20.1-1.1.0
[1.20.1-1.0.2]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.0.1...release/1.20.1-1.0.2
[1.20.1-1.0.1]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.0.0...release/1.20.1-1.0.1
[1.20.1-1.0.0]: https://github.com/Razzokk/wireless-redstone/commits/release/1.20.1-1.0.0
[1.20.1-1.1.1]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.1.0...release/1.20.1-1.1.1
