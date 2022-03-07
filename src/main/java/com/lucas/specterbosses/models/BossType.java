package com.tke.boss.models;

import java.util.*;
import org.bukkit.inventory.*;

public class BossType
{
    String id;
    String displayName;
    String name;
    String texture;
    double heath;
    double damage;
    List<String> commands;
    ItemStack[] items;
    List<String> message;
    
    public BossType(final String id, final String name, final String displayName, final String texture, final double heath, final double damage, final List<String> commands, final ItemStack[] items, final List<String> message) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.texture = texture;
        this.heath = heath;
        this.damage = damage;
        this.commands = commands;
        this.items = items;
        this.message = message;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getTexture() {
        return this.texture;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setTexture(final String texture) {
        this.texture = texture;
    }
    
    public double getHeath() {
        return this.heath;
    }
    
    public double getDamage() {
        return this.damage;
    }
    
    public void setHeath(final double heath) {
        this.heath = heath;
    }
    
    public void setDamage(final double damage) {
        this.damage = damage;
    }
    
    public List<String> getCommands() {
        return this.commands;
    }
    
    public void setCommands(final List<String> commands) {
        this.commands = commands;
    }
    
    public ItemStack[] getItems() {
        return this.items;
    }
    
    public void setItems(final ItemStack[] items) {
        this.items = items;
    }
    
    public List<String> getMessage() {
        return this.message;
    }
}
