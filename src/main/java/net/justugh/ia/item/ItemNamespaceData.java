package net.justugh.ia.item;

import lombok.AllArgsConstructor;
import org.bukkit.NamespacedKey;
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

    public void apply(PersistentDataContainer container) {
        if (type == null || value == null) {
            return;
        }

        switch (type.toLowerCase()) {
            case "double":
                container.set(getNamespacedKey(), PersistentDataType.DOUBLE, Double.parseDouble(value));
                return;
            case "integer":
                container.set(getNamespacedKey(), PersistentDataType.INTEGER, Integer.parseInt(value));
                return;
            case "string":
                container.set(getNamespacedKey(), PersistentDataType.STRING, value);
                return;
            default:
                break;
        }
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
