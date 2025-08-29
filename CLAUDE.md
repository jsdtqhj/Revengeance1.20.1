# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Minecraft Forge 1.20.1 mod called "Revengeance" that implements a rage and adrenaline system similar to the Calamity mod. The mod adds custom attributes, effects, sounds, and GUI overlays to enhance combat gameplay.

## Build and Development Commands

### Building the Mod
```bash
# Build the mod JAR file
./gradlew build

# Clean build artifacts
./gradlew clean

# Generate Eclipse run configurations
./gradlew genEclipseRuns

# Generate IntelliJ IDEA run configurations  
./gradlew genIntellijRuns

# Refresh dependencies
./gradlew --refresh-dependencies
```

### Running the Mod
```bash
# Run client in development
./gradlew runClient

# Run server in development
./gradlew runServer

# Run data generation
./gradlew runData

# Run game tests
./gradlew runGameTestServer
```

## Code Architecture

### Core Systems

1. **Attribute System** (`attributes/RevengeanceModAttributes.java`)
   - `RAGE_LEVEL`: Stores player rage (0-100)
   - `ADRENALINE_LEVEL`: Stores player adrenaline (0-10000)

2. **Effect System** (`potion/RevengeanceModMobEffects.java`)
   - `RAGE_EFFECT`: Applied when rage reaches 100
   - `ADRENALINE_EFFECT`: Applied when adrenaline is activated

3. **Rage Mechanics** (`procedures/RageSystemHandler.java`)
   - Charges rage when hostile mobs are nearby (8-32 block radius)
   - Multiple hostiles stack charge rate
   - Rage decays after 20 seconds without hostiles
   - Consumes rage while effect is active (100 rage over 180 ticks)

4. **GUI Overlay** (`client/screens/RageBarsOverlay.java`)
   - Displays rage and adrenaline bars
   - Rage bar shakes when effect is active
   - Plays animation when rage reaches full

5. **Network Messages**
   - `RageButtonMessage`: Handles rage activation key press
   - `AdrenalineButtonMessage`: Handles adrenaline activation key press

### Resource Structure
- Textures: `src/main/resources/assets/revengeance/textures/`
- Sounds: `src/main/resources/assets/revengeance/sounds/`
- Tags: `src/main/resources/data/revengeance/tags/`
- Lang files: `src/main/resources/assets/revengeance/lang/`

### Important Configuration
- Mod ID: `revengeance`
- Minecraft: 1.20.1
- Forge: 47.4.6
- Mappings: Parchment 2023.06.26-1.20.1