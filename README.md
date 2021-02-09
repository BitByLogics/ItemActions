# Item Actions
Give your items a new purpose. The goal is this plugin is to allow
items to have useful actions. The plugin is currently in an early stage
so there's not as much you can do as I'd like currently.

## Commands
Command | Description | Permission
--- | --- | ---
/ia | View command help | itemactions.help
/ia reload |  Reload the configuration and loaded items. | itemactions.reload
/ia give <player> <id> | Give a player Action Item(s) | itemactions.give

## Configuration
Default Item Configuration can be found here:
```yaml
# General Configuration
   Items:
     SpeedPickaxe: # The name of the item action, can be anything
       Cooldown: 60 # The cooldown before they can run the action again, this is in seconds.
       Bypass-Permission: "itemactions.speedpickaxe.bypass" # The permission to bypass the cooldown.
       # Any information under this must be matched for the action to run from the item.
       # It is recommended to just use the 'data-key' field for items, because it's the safest.
       Item:
         # A list of materials, if the item matches any it will be valid, learn more here (https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
         materials:
           - "DIAMOND_PICKAXE"
         name: "&eSpeed Pickaxe" # The items name
         # A custom key that can be added to any item/entity, learn more here (https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/persistence/PersistentDataContainer.html)
         # MUST be formatted plugin-name:data-key.
         data-key: "itemactions:speed_pickaxe"
         # The data value type
         # Currently supports: INTEGER, STRING and DOUBLE
         data-type: "STRING"
         # The value of the data
         # Must match the data-type field, for example INTEGER must be an integer.
         # You still must make below value a string though, so even if it's an integer it would still be "1"
         data-value: "1"
       Requirements:
         # The actual actions required to activate this item action (https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/block/Action.html)
         action-types:
           - "RIGHT_CLICK_AIR"
         permission: "itemactions.speedpickaxe" # The permission required to use this action, can be removed if no permission is needed
       Actions:
         action-sound: "BLOCK_ANVIL_HIT" # Plays a sound to the player when the action is ran (https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html)
         player-commands: # Commands that will be ran by the player when the action is ran.
           - "say Must go FAST."
         console-commands: # Commands that will be ran by the console when the action is ran.
           - "effect give %player% minecraft:speed 30 1"```