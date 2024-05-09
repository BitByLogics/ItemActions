package net.bitbylogic.itemactions.item.data;

import lombok.AllArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@AllArgsConstructor
public class ItemNamespaceData {

    private final String data;
    private final String type;
    private final String value;

    public boolean matches(PersistentDataContainer container) {
        if (data == null) {
            return true;
        }

        return container.has(getNamespacedKey(), getDataType());
    }

    public ItemMeta apply(ItemMeta meta) {
        if (type == null || value == null) {
            return meta;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        switch (type.toLowerCase()) {
            case "double":
                container.set(getNamespacedKey(), PersistentDataType.DOUBLE, Double.parseDouble(value));
                return meta;
            case "integer":
                container.set(getNamespacedKey(), PersistentDataType.INTEGER, Integer.parseInt(value));
                return meta;
            case "string":
                container.set(getNamespacedKey(), PersistentDataType.STRING, value);
                return meta;
            default:
                break;
        }

        return meta;
    }

    public NamespacedKey getNamespacedKey() {
        String[] splitData = data.split(":");
        return new NamespacedKey(splitData[0], splitData[1]);
    }

    public PersistentDataType<?, ?> getDataType() {
        switch (type.toLowerCase()) {
            case "double":
                return PersistentDataType.DOUBLE;
            case "integer":
                return PersistentDataType.INTEGER;
            case "string":
                return PersistentDataType.STRING;
            default:
                return null;
        }
    }

}
