# Changelog
Format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
versioning follows [Semantic Versioning](https://semver.org/spec/v2.0.0.html)
with the minecraft version prepended to it.

## [Unreleased]

### Added
- Sides of transmitters and receivers can be disabled/enabled
- Wrench item to configure the sides
- Show frequency of transmitters and receivers when picked up with nbt data

### Changed
- Separate datagen sub-project
- Moved block textures to their respective directories
  - If you have a custom resource pack for Wireless Redstone, you have to move and rename them too

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
	- Yarn mappings for fabric **and** forge (using [Architectury Loom](https://docs.architectury.dev/loom/introduction))
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

[Unreleased]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.4-1.1.3...HEAD
[1.20.4-1.1.2]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.1.2...release/1.20.4-1.1.2
[1.20.4-1.1.3]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.4-1.1.2...release/1.20.4-1.1.3
[1.20.1-1.1.1]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.1.0...release/1.20.1-1.1.1
[1.20.1-1.0.0]: https://github.com/Razzokk/wireless-redstone/commits/release/1.20.1-1.0.0
[1.20.1-1.0.1]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.0.0...release/1.20.1-1.0.1
[1.20.1-1.0.2]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.0.1...release/1.20.1-1.0.2
[1.20.1-1.1.0]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.0.2...release/1.20.1-1.1.0
[1.20.1-1.1.2]: https://github.com/Razzokk/wireless-redstone/compare/release/1.20.1-1.1.1...release/1.20.1-1.1.2
