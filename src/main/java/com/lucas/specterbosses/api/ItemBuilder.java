package com.tke.boss.api;

import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import com.mojang.authlib.*;
import com.tke.boss.controller.*;
import com.mojang.authlib.properties.*;
import java.lang.reflect.*;
import org.bukkit.enchantments.*;

public class ItemBuilder
{
    private ItemStack item;
    
    public ItemBuilder(final ItemStack item) {
        this.item = item;
    }
    
    public ItemBuilder() {
        this.item = new ItemStack(Material.AIR, 1);
    }
    
    public ItemBuilder(final Material material) {
        this.item = new ItemStack(material, 1);
    }
    
    public ItemStack getItem() {
        final ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        this.item.setItemMeta(meta);
        return this.item;
    }
    
    public ItemBuilder durability(final int durability) {
        this.item.setDurability((short)durability);
        return this;
    }
    
    public ItemBuilder type(final Material material) {
        this.item.setType(material);
        return this;
    }
    
    public ItemBuilder type(final Material material, final int damage) {
        this.item.setType(material);
        this.item.setDurability((short)damage);
        return this;
    }
    
    public ItemBuilder type(final int id) {
        this.item.setTypeId(id);
        return this;
    }
    
    public ItemBuilder type(final int id, final int damage) {
        this.item.setTypeId(id);
        this.item.setDurability((short)damage);
        return this;
    }
    
    public ItemBuilder amount(final int i) {
        this.item.setAmount(i);
        return this;
    }
    
    public ItemBuilder name(final String name) {
        final ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.item.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder lore() {
        final ItemMeta im = this.item.getItemMeta();
        im.setLore((List)new ArrayList());
        this.item.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder lore(final List<String> lore) {
        if (lore == null || lore.size() == 0) {
            return this;
        }
        final ItemMeta im = this.item.getItemMeta();
        final List<String> ls = new ArrayList<String>();
        for (String s : lore) {
            if (!s.contains("§")) {
                s = "§7" + s;
            }
            ls.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        im.setLore((List)ls);
        this.item.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder lore(final String... lore) {
        if (lore == null) {
            return this;
        }
        final ItemMeta im = this.item.getItemMeta();
        final List<String> ls = new ArrayList<String>();
        for (String s : lore) {
            if (!s.contains("§")) {
                s = "§7" + s;
            }
            ls.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        im.setLore((List)ls);
        this.item.setItemMeta(im);
        return this;
    }
    
    public ItemBuilder skull(final String entity) {
        this.item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if (entity.isEmpty()) {
            return this;
        }
        final SkullMeta itemMeta = (SkullMeta)this.item.getItemMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
        final String mob = BossTypeController.getTexture(entity);
        profile.getProperties().put((Object)"textures", (Object)new Property("textures", mob.equals("") ? entity : mob));
        Field profileField = null;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
        this.item.setItemMeta((ItemMeta)itemMeta);
        return this;
    }
    
    public ItemBuilder addEnchant(final Enchantment enc, final int level) {
        this.item.addUnsafeEnchantment(enc, level);
        return this;
    }
    
    public ItemBuilder owner(final String owner) {
        this.item.setType(Material.SKULL_ITEM);
        this.item.setDurability((short)3);
        final SkullMeta sm = (SkullMeta)this.item.getItemMeta();
        sm.setOwner(owner);
        this.item.setItemMeta((ItemMeta)sm);
        return this;
    }
}
