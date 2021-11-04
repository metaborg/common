# Changelog
All notable changes to this project are documented in this file, based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).


## [Unreleased]


## [0.9.9] - 2021-11-04
### Fixed
- `KeyedMessages#ofTryExtractMessagesFrom` returning `Optional.of(messages)` even when `messages.isEmpty()`.

### Added
- `ResourceUtil#copy`


## [0.9.8] - 2021-10-11
### Changed
- `ClassToInstanceMap#get` to allow compatible types, instead of requiring the type of the instance to be exactly the same as the requested type.
- `resource` requirement to `0.12.0`.

### Added
- `ClassToInstanceMap#put(Class, Object)` for adding a value to the map with an explicit type. For example, the interface type of the value, instead of the concrete type of the value.


## [0.9.7] - 2021-10-01
### Changed
- Gradle wrapper to `6.9.1`.

### Added
- `StringBuilderOutputStream`


## [0.9.6] - 2021-09-24
### Added
- `flatMapThrowing`, `filter`, `filterThrowing`, `stream` to `Option`.
- `ThrowingPredicate`, a throwing version of `Predicate`.


## [0.9.5] - 2021-09-20
### Added
- `tryLock` methods to `CloseableReentrantReadWriteLock` and `LockHandle`


## [0.9.4] - 2021-09-20
### Added
- `FloatUtil` based on Guava's `Floats` with `min` and `max` methods taking vararg arrays of floats.
- `CloseableReentrantReadWriteLock` and `LockHandle` for locking in try-with-resources statements.


## [0.9.3] - 2021-09-01
### Changed
- `resource` requirement to `0.11.5`.


[Unreleased]: https://github.com/metaborg/common/compare/release-0.9.9...HEAD
[0.9.9]: https://github.com/metaborg/common/compare/release-0.9.8...release-0.9.9
[0.9.8]: https://github.com/metaborg/common/compare/release-0.9.7...release-0.9.8
[0.9.7]: https://github.com/metaborg/common/compare/release-0.9.6...release-0.9.7
[0.9.6]: https://github.com/metaborg/common/compare/release-0.9.5...release-0.9.6
[0.9.5]: https://github.com/metaborg/common/compare/release-0.9.4...release-0.9.5
[0.9.4]: https://github.com/metaborg/common/compare/release-0.9.3...release-0.9.4
[0.9.3]: https://github.com/metaborg/common/compare/release-0.9.2...release-0.9.3
