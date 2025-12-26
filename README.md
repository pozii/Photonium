<div align="center">
  <img src="https://i.ibb.co/CKQFN5zr/Photonium-1.png" width="350" alt="Photonium Logo">
  <br><br>
  <strong>Seamless Gameplay, Zero Compromise</strong>
  <br><br>
  
  <a href="https://github.com/YourUsername/Photonium/releases">
    <img src="https://img.shields.io/github/v/release/pozii/Photonium?style=flat-square&color=orange" alt="Latest Release">
  </a>
  <img src="https://img.shields.io/badge/Loader-Fabric-bebebe?style=flat-square&logo=fabric" alt="Fabric">
  <img src="https://img.shields.io/badge/Minecraft-1.21.11-green?style=flat-square" alt="Minecraft Version">
  <img src="https://img.shields.io/badge/License-Apache 2.0-blue?style=flat-square" alt="License">
</div>

---

## 📖 About

**Photonium** is a next-generation, client-side optimization mod designed for modern Minecraft. Unlike traditional optimization mods that focus on a single aspect, Photonium implements a **Hybrid Engine** that unifies **CPU**, **GPU**, and **RAM** management.

It aims to deliver the highest possible frame rates (FPS) while eliminating micro-stutters and server-side lag (in singleplayer), all with **zero configuration**.

---

## 🚀 Technical Features

### 🧠 CPU & Logic Optimization
* **XP Orb Merging:** Intercepts `ExperienceOrbEntity` spawning logic to merge hundreds of orbs into single entities, reducing physics calculations by ~99% in mob farms.
* **Entity Logic Culling:** Implements a distance-based check (64 blocks) to freeze the AI goals and pathfinding of non-visible entities.
* **Fast Item Sleep:** Modifies `ItemEntity` behavior to aggressively stop physics calculations (`setVelocity`) once an item touches the ground.
* **World Join Fix:** Adjusts thread priorities during the `GameJoinS2CPacket` handling to prevent the client from freezing during world loading.

### 👁️ GPU & Rendering
* **Entity Culling:** Prevents the rendering of entities that are occluded (behind walls) or outside the camera frustum.
* **Particle Culling:** Reduces the render distance of heavy particle effects (Campfire smoke, explosions) based on distance.
* **Render Bypass:** Disables redundant OpenGL error checks and unnecessary state validations in the render loop.

### 🧹 Memory Management
* **Smart GC:** Triggers an aggressive Garbage Collection and resource cleanup during specific events:
    * Dimension Change (Nether/End)
    * Resource Pack Reload (F3+T)
    * Title Screen Transition

### 📝 Smart Batch Logging
Includes a custom logger utility that aggregates optimization events and prints a consolidated report to the console every 30 seconds to prevent log spam.

---

## 📥 Installation

1.  Download the latest `.jar` file from the [Releases](https://github.com/pozii/Photonium/releases) tab or [Modrinth](https://modrinth.com/mod/photonium).
2.  Install **Fabric Loader**.
3.  Place the mod file into your `.minecraft/mods` folder.
4.  Launch the game.

*No configuration is required. The mod works out of the box.*

---

## 🛠️ Building from Source

To build Photonium from the source code, you need **JDK 21**.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/pozii/Photonium.git](https://github.com/pozii/Photonium.git)
    cd Photonium
    ```

2.  **Build with Gradle:**
    * On Windows:
        ```powershell
        ./gradlew build
        ```
    * On Linux/macOS:
        ```bash
        ./gradlew build
        ```

3.  **Locate the Output:**
    The compiled `.jar` file will be located in `build/libs/`.

---

## 🐛 Bug Reports

If you encounter any issues or conflicts with other mods, please open an issue in the [Issues](https://github.com/pozii/Photonium/issues) tab. Please include:
* Minecraft Version
* Fabric Loader Version
* Log file (`latest.log`)

---

<div align="center">

## ☕ Support Development

If you like the project and want to support the development of new optimization techniques, consider buying me a coffee!

<a href="https://ko-fi.com/pozii" target="_blank">
  <img src="https://cdn.prod.website-files.com/5c14e387dab576fe667689cf/670f5a01cf2da94a032117b9_support_me_on_kofi_red.png" alt="Support me on Ko-Fi" width="180" />
</a>

</div>
