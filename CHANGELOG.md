# Changelog
Format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
versioning follows [Semantic Versioning](https://semver.org/spec/v2.0.0.html)
with the minecraft version prepended to it.

## [Unreleased]

## [1.20.1-1.0.1] - 2023-06-27

### Added
- Proper README.md
- Automated Modrinth and CurseForge publishing with the [Minotaur](https://github.com/modrinth/minotaur) and [CurseForgeGradle](https://github.com/Darkhax/CurseForgeGradle) gradle tasks/plugins
- Changelog semi-automation with the [Gradle Changelog Plugin](https://github.com/JetBrains/gradle-changelog-plugin) 
- Fancy [shields.io](https://shields.io/) badges to README.md
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

[Unreleased]: https://github.com/Razzokk/WirelessRedstone/compare/release/fabric/1.20.1-1.0.1...HEAD
[1.20.1-1.0.0]: https://github.com/Razzokk/WirelessRedstone/commits/release/fabric/1.20.1-1.0.0
[1.20.1-1.0.1]: https://github.com/Razzokk/WirelessRedstone/compare/release/fabric/1.20.1-1.0.0...release/fabric/1.20.1-1.0.1
