package org.jismah.entidades;

import org.jismah.controladores.Core;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class carroCompras {
    private Map<UUID, Integer> items;

    public carroCompras() {
        this.items = new HashMap<>();
    }

    public void addItem(UUID id, int cant) {
        int currentCant = items.getOrDefault(id, 0);
        items.put(id, currentCant + cant);
    }

    public void reduceItem(UUID id, int cant) {
        int currentCant = items.getOrDefault(id, 0);
        int newCant = currentCant - cant;
        if (newCant <= 0) {
            items.remove(id);
        } else {
            items.put(id, newCant);
        }
    }

    public void removeItem(UUID id) {
        items.remove(id);
    }

    public Map<UUID, Integer> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.valueOf(0.00);
        Producto product;
        for (Map.Entry<UUID,Integer> item: items.entrySet()) {
            product = Core.getInstance().getProductById(item.getKey());
            total = total.add(product.getPrecio().multiply(BigDecimal.valueOf(item.getValue())));
        }
        return total;
    }

    public Integer getItemCount() {
        Integer total = 0;
        for (Integer count : items.values()) {
            total += count;
        }
        return  total;
    }

    public void clearCart() {
        items.clear();
    }
}
