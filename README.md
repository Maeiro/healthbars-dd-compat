# Health Bars Dynamic Difficulty Compat

NeoForge addon that integrates Dynamic Difficulty mob levels into Health Bars.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.x
- Health Bars
- Dynamic Difficulty

## Build

```bash
gradlew build
```

## Release Checklist

1) Update `gradle.properties` (`mod_version`)
2) Update `src/main/resources/META-INF/neoforge.mods.toml` (`version`)
3) Build: `gradlew build`
4) Tag and push: `git tag vX.Y.Z` then `git push origin vX.Y.Z`

## Usage

Drop the built jar into your `mods` folder alongside Health Bars and Dynamic Difficulty.

## Download

Grab the latest release from:
https://github.com/Maeiro/healthbars-dd-compat/releases/latest

## Config

Client config file: `config/healthbars_dd_compat-client.toml`

- `level_prefix = false` -> `Mob Lv. 99` (default)
- `level_prefix = true` -> `Lv. 99 Mob`

## Credits

- Health Bars by Fuzss: https://github.com/Fuzss/healthbars
- Dynamic Difficulty by muon-rw: https://github.com/muon-rw/Dynamic-Difficulty
