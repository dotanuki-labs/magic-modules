# CHANGELOG

> https://keepachangelog.com

All notable changes to this project will be documented in this file.

## [0.0.3] 2020-08-30

### Changed
- Non-application modules will now be mapped to `JavaModules.kt` and `AndroidModules.kt` instead of an unique `Libraries.kt` [(#02)](https://github.com/dotanuki-labs/magic-modules/pull/2)
- Project parsing is now faster [(#02)](https://github.com/dotanuki-labs/magic-modules/pull/2)

### Fixed
- Extract Maven coordinates taking in consideration OS style for file paths [(#03)](https://github.com/dotanuki-labs/magic-modules/pull/3)

## [0.0.2] 2020-05-25

### Added
- Grab more possible plugin declarations when matching modules [(#01)](https://github.com/dotanuki-labs/magic-modules/pull/1)

## [0.0.1] 2020-04-22

### Added
- Initial release, for demo purposes
- `Modules.kt` generated at `buildSrc`
- Separation between application modules and library modules, both Android and JVM