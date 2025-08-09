# CommandButtons Icons

This folder contains the icon textures for the CommandButtons mod.

## 📐 Icon Specifications

### **Size Requirements:**
- **Dimensions**: 16x16 pixels (standard Minecraft icon size)
- **Format**: PNG with transparency support
- **Style**: Pixel art (to match Minecraft's aesthetic)

### **Required Icon Files:**

1. **`gamemode.png`** - Game mode toggle icon
   - Suggested design: Creative/Survival mode symbols or a player head
   
2. **`rain.png`** - Weather toggle icon
   - Suggested design: Rain drops or cloud with rain
   
3. **`heal.png`** - Healing icon
   - Suggested design: Heart symbol or health potion
   
4. **`morning.png`** - Set time to morning icon
   - Suggested design: Rising sun or bright sun
   
5. **`midnight.png`** - Set time to midnight icon
   - Suggested design: Moon and stars or dark sky
   
6. **`afternoon.png`** - Set time to afternoon icon
   - Suggested design: High sun or partly cloudy sky

## 🎨 Design Guidelines

### **Color Palette:**
- Use Minecraft's color palette for consistency
- Consider contrast for visibility on inventory backgrounds
- Use transparency for smoother edges

### **Style Tips:**
- Keep designs simple and recognizable at 16x16 pixels
- Use bold, clear shapes that are easily identifiable
- Consider the icon's readability at small sizes
- Test with both light and dark inventory backgrounds

### **File Naming:**
- Use lowercase filenames
- Match the button function names exactly
- Must be PNG format

## 🔧 Technical Notes

- Icons will be rendered at 16x16 pixels in-game
- The mod will automatically load these textures
- If an icon is missing, the mod will fall back to text labels
- Icons support full RGBA transparency

## 📂 Folder Structure

```
src/main/resources/assets/commandbuttons/textures/gui/buttons/
├── gamemode.png     (16x16 px)
├── rain.png         (16x16 px)
├── heal.png         (16x16 px)
├── morning.png      (16x16 px)
├── midnight.png     (16x16 px)
└── afternoon.png    (16x16 px)
```

Once you've created these icons, place them in this folder and the mod will automatically use them instead of text labels!
