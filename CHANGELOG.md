# Changelog
All notable changes to this project are documented in this file, based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).


## [Unreleased]
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
- Update resource dependency to 0.11.5.


[Unreleased]: https://github.com/metaborg/common/compare/release-0.9.6...HEAD
[0.9.6]: https://github.com/metaborg/common/compare/release-0.9.5...release-0.9.6
[0.9.5]: https://github.com/metaborg/common/compare/release-0.9.4...release-0.9.5
[0.9.4]: https://github.com/metaborg/common/compare/release-0.9.3...release-0.9.4
[0.9.3]: https://github.com/metaborg/common/compare/release-0.9.2...release-0.9.3
