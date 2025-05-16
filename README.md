<div align="center">
<img src="https://i.imgur.com/YwMgeqv.jpeg" style="width: 20%;" alt="Icon">

## ItemActions
[![Build](https://img.shields.io/github/actions/workflow/status/BitByLogics/ItemActions/.github/workflows/maven.yml?branch=master)](https://github.com/BitByLogics/ItemActions/actions)
![Issues](https://img.shields.io/github/issues-raw/BitByLogics/ItemActions)
[![Stars](https://img.shields.io/github/stars/BitByLogics/ItemActions)](https://github.com/BitByLogics/ItemActions/stargazers)
[![Chat](https://img.shields.io/discord/1310486866272981002?logo=discord&logoColor=white)](https://discord.gg/syngw2UQUd)

<a href="#"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/compact/supported/spigot_46h.png" height="35"></a>
<a href="#"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/compact/supported/paper_46h.png" height="35"></a>
<a href="#"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/compact/supported/purpur_46h.png" height="35"></a>

**ItemActions** is a lightweight but powerful system for defining custom item behavior triggered by usage or armor equipment.
</div>

## ✨ Features
- Define custom item actions with simple YAML
- Trigger commands, sounds, messages, potion effects, and more
- Equip/unequip triggers for armor with full slot support
- Custom cooldowns, permissions, and namespaced keys
- Supports filtering, custom metadata, and tag checks
- Reload configs on the fly without restarting the server
- Well-documented with a full wiki

## 📂 Configuration Example
```yaml
  ZoomyBoots:
    Type: "ARMOR"
    Version: 1
    Item:
      Material: "LEATHER_BOOTS"
      Name: "&eZoomy Boots"
      Dye-Color: "#FF0000"
      Data-Key: "itemactions:zoomy_boots"
      Data-Type: "STRING"
      Data-Value: "1"
    Actions:
      - "ADD_POTION_EFFECT:speed|3|true|true"
      - "RUN_COMMAND:say Zoomy Boots Equipped."
      - "NEGATE_FALL_DAMAGE"
    Unequip-Actions:
      - "REMOVE_POTION_EFFECT:speed"
      - "RUN_COMMAND:say Zoomy Boots Removed."
```

## 📦 Commands
| Command | Description | Permission |
|--------|-------------|------------|
| `/itemactions` | View command help | `itemactions.help` |
| `/itemactions reload` | Reload configuration and item data | `itemactions.reload` |
| `/itemactions give <player> <item>` | Give a player a configured item | `itemactions.give` |

## 📚 Documentation
Still not sure how to use it?

📝 **[Read the Wiki](https://github.com/BitByLogics/ItemActions/wiki)**  
💬 **Contact me on Discord: @BitByLogic** for help, support, and feature requests!

## ⚠️ Notes
- Be sure to use a unique Data-Key (namespaced) to avoid item conflicts.
- Always match the Data-Type and Data-Value formats precisely.
- Armor actions only trigger when equipped in proper armor slots.

## 📥 Download
You can get ItemActions from the following platforms:

- [Modrinth](https://modrinth.com/plugin/item-actions)
- [SpigotMC](https://www.spigotmc.org/resources/item-actions.88840/)
