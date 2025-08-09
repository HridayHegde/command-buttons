# CommandButtons

⭐ **Please star this repository if you find this mod useful!** ⭐  
*Your stars help others discover this mod and motivate continued development.*

A convenient Minecraft Fabric mod that adds quick-access command buttons to your inventory screen for common administrative tasks.

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.1-green)
![Mod Loader](https://img.shields.io/badge/Mod%20Loader-Fabric-blue)
![Java Version](https://img.shields.io/badge/Java-17+-orange)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/HridayHegde/command-buttons?label=Latest%20Release)
![GitHub stars](https://img.shields.io/github/stars/HridayHegde/command-buttons?style=social)
![GitHub forks](https://img.shields.io/github/forks/HridayHegde/command-buttons?style=social)

## Features

CommandButtons adds 6 convenient square buttons horizontally across the top-left corner of your inventory screen:

- **🎮 GameMode** - Toggle between Creative and Survival modes
- **🌧️ Rain** - Toggle weather between clear and rainy
- **❤️ Heal** - Instantly heal and restore hunger
- **🌅 Morning** - Set time to morning (1000)
- **🌙 Midnight** - Set time to midnight (18000)
- **🌆 Afternoon** - Set time to afternoon (6000)

### Screenshots

The mod works seamlessly in both game modes with the new horizontal icon-based design:
- **Survival Mode**: 6 square buttons with custom icons arranged horizontally
- **Creative Mode**: Same clean icon buttons that integrate perfectly with the creative inventory

### Custom Icons & Tooltips

The buttons now support **16x16 pixel custom icons** with helpful tooltips:
- **Beautiful icons**: Custom pixel-art icons that match Minecraft's aesthetic
- **Helpful tooltips**: Hover over any button to see what it does
- **Minecraft-style**: Icons are rendered at the perfect size for the square button design
- **Horizontal layout**: Buttons are now arranged horizontally for better space usage
- **User-friendly**: Even with custom icons, tooltips make functionality clear

**Icon Requirements:**
- Format: PNG with transparency support
- Size: 16x16 pixels
- Style: Pixel art
- Location: `src/main/resources/assets/commandbuttons/textures/gui/buttons/`

## Installation

### Prerequisites
- Minecraft 1.20.1
- Fabric Loader 0.16.14+
- Fabric API 0.92.6+1.20.1
- Java 17 or higher

### Steps
1. **Download**: Get the latest release from the [**📥 Releases Page**](../../releases)
2. **Install**: Place the `.jar` file in your Minecraft `mods` folder
3. **Launch**: Start Minecraft with the Fabric profile
4. **Enjoy**: Open your inventory (E key) to see the 6 square buttons arranged horizontally in the top-left corner!

> 💡 **Tip**: Look for the latest release with the green "Latest" tag for the most stable version.

## Usage

Simply open your inventory and click the buttons in the top-left corner. **Hover over any button to see a tooltip** explaining what it does. The buttons work in both Survival and Creative modes:

- **Single Player**: Commands execute immediately
- **Multiplayer**: Requires operator permissions for most commands

### Commands Used
The mod executes these commands when buttons are clicked:
- GameMode: `/gamemode creative` or `/gamemode survival`
- Rain: `/weather rain` or `/weather clear`
- Heal: `/effect give @s minecraft:instant_health 1 10` + `/effect give @s minecraft:saturation 1 10`
- Morning: `/time set 1000`
- Midnight: `/time set 18000`
- Afternoon: `/time set 6000`

## Development

### Building from Source

#### Prerequisites
- JDK 17+
- Git

#### Steps
```bash
git clone <repository-url>
cd CommandButtons
./gradlew build
```

The built mod will be available in `build/libs/`.

### Project Structure
```
CommandButtons/
├── src/
│   ├── main/java/command/buttons/          # Main mod code
│   └── client/java/command/buttons/        # Client-side code
│       ├── mixin/client/                   # Mixins for UI integration
│       └── util/                           # Command execution utilities
├── src/main/resources/                     # Resources and mod metadata
├── build.gradle                           # Build configuration
└── gradle.properties                      # Project properties
```

### Technical Details

The mod uses two different approaches for maximum compatibility:

**Survival Mode:**
- Screen Events API for button rendering
- Invisible ButtonWidget objects for click handling

**Creative Mode:**
- Mixin injection into CreativeInventoryScreen for rendering
- Custom mouse click detection for interaction

This dual approach ensures the mod works reliably across different game modes without conflicts.

## Configuration

Currently, the mod does not include configuration options. The buttons are positioned at a fixed location (top-left corner) and cannot be moved or disabled.

## Compatibility

### Supported Versions
- Minecraft: 1.20.1
- Fabric Loader: 0.16.14+
- Fabric API: 0.92.6+1.20.1

### Known Compatible Mods
- Most inventory enhancement mods
- JEI (Just Enough Items)
- REI (Roughly Enough Items)

### Potential Conflicts
- Mods that heavily modify the inventory screen layout may cause visual overlap
- Other mods that add buttons to the inventory screen might conflict for space

## Troubleshooting

### Buttons Not Visible
1. Ensure you have the correct Minecraft version (1.20.1)
2. Verify Fabric API is installed
3. Check that you have operator permissions if on a server

### Buttons Not Working
1. Verify you have permission to use the commands
2. Check server settings if playing multiplayer
3. Look at the game logs for any error messages

### Performance Issues
The mod has minimal performance impact. If you experience issues:
1. Check for conflicts with other mods
2. Ensure you have sufficient memory allocated to Minecraft

## Contributing & Support

### 🌟 Show Your Support
**Love this mod? Please star this repository!** ⭐  

Stars help in several ways:
- **Visibility**: More stars = more people discover this mod
- **Motivation**: Stars encourage continued development and updates
- **Community**: Shows the project is actively used and appreciated
- **GitHub Algorithm**: Starred repos get better visibility in search results

### 🤝 Contributing
Contributions are welcome! Please feel free to:
- **🐛 Report bugs** by [opening an issue](../../issues/new)
- **💡 Suggest new features** or improvements
- **🔧 Submit pull requests** with enhancements
- **⭐ Star the repository** to show support

### Development Setup
1. Fork the repository
2. Clone your fork
3. Import the project into your IDE (IntelliJ IDEA recommended)
4. Make your changes
5. Test thoroughly in both Survival and Creative modes
6. Submit a pull request

### 📦 Releases
This project uses GitHub Actions for automated releases:

- **Automatic builds**: Every tagged version creates a release automatically
- **Download ready**: Pre-built `.jar` files available on the [Releases page](https://github.com/HridayHegde/command-buttons/releases)
- **Changelog**: Each release includes detailed notes about changes

To create a new release (maintainers only):
```bash
git tag v1.0.1
git push origin v1.0.1
```

## License

This project is licensed under the CC0-1.0 License - see the [LICENSE](LICENSE) file for details.

## Changelog

### Version 1.0.0
- Initial release
- Added 6 command buttons (GameMode, Rain, Heal, Morning, Midnight, Afternoon)
- Support for both Survival and Creative modes
- Clean integration with vanilla inventory UI

## Credits

- Built with [Fabric](https://fabricmc.net/)
- Uses [Fabric API](https://github.com/FabricMC/fabric)
- Developed using the [Fabric Example Mod](https://github.com/FabricMC/fabric-example-mod) template

## Support

If you encounter any issues or have questions:
1. Check the [Issues](../../issues) page for existing reports
2. Create a new issue if your problem isn't already reported
3. Include your Minecraft version, mod version, and any error logs

---

**Enjoy your enhanced Minecraft experience with CommandButtons!** 🎮
