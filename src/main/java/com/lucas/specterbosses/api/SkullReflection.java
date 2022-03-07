package com.tke.boss.api;

import java.util.concurrent.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.*;
import java.lang.reflect.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class SkullReflection
{
    public static CopyOnWriteArrayList<SkullReflection> skulls;
    private static String version;
    public static Class<?> craftWorld;
    public static Class<?> entityClass;
    public static Class<?> nmsWorld;
    public static Class<?> armorStand;
    public static Class<?> entityLiving;
    public static Class<?> spawnEntityLiving;
    public static Class<?> removePacket;
    public static Class<?> equipPacket;
    public static Class<?> itemStack;
    public static Class<?> entityTeleport;
    public static Class<?> entityLook;
    public static Class<?> vector3f;
    public static Class<?> metadata;
    private Location location;
    private String lines;
    private int ids;
    public Object entity;
    public ArmorStand stand;
    private double offset;
    
    public SkullReflection(final Location location, final String text) {
        this.offset = 0.23;
        this.location = location;
        this.lines = text;
    }
    
    public SkullReflection(final Location location) {
        this.offset = 0.23;
        this.location = location;
    }
    
    public SkullReflection() {
        this.offset = 0.23;
    }
    
    public static String getVersion() {
        return SkullReflection.version;
    }
    
    public void setLines(final String text) {
        this.lines = text;
        this.update();
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(final Location location) {
        this.location = location;
        this.update();
    }
    
    public void teleport2(final Location loc) {
        this.update();
    }
    
    public void spawn() {
        final Location current = this.location.clone().add(0.0, this.offset, 0.0);
        current.add(0.0, 0.5, 0.0);
        this.spawnSkull(ChatColor.translateAlternateColorCodes('&', this.lines), current.subtract(0.0, this.offset, 0.0));
        SkullReflection.skulls.add(this);
    }
    
    public void spawnEntity() {
        final Location current = this.location.clone().add(0.0, this.offset, 0.0);
        current.add(0.0, 0.5, 0.0);
        this.spawnSkull(ChatColor.translateAlternateColorCodes('&', this.lines), current.subtract(0.0, this.offset, 0.0));
    }
    
    private void spawnSkull(final String text, final Location location) {
        try {
            final Object craftWorld = SkullReflection.craftWorld.cast(location.getWorld());
            final Object entityObject = SkullReflection.armorStand.getConstructor(SkullReflection.nmsWorld).newInstance(SkullReflection.craftWorld.getMethod("getHandle", (Class<?>[])new Class[0]).invoke(craftWorld, new Object[0]));
            this.configureSkull(entityObject, text, location);
            final Object ent = SkullReflection.craftWorld.getMethod("addEntity", SkullReflection.entityClass, CreatureSpawnEvent.SpawnReason.class).invoke(craftWorld, entityObject, CreatureSpawnEvent.SpawnReason.CUSTOM);
            this.ids = (int)entityObject.getClass().getMethod("getId", (Class<?>[])new Class[0]).invoke(entityObject, new Object[0]);
            this.entity = entityObject;
            this.stand = (ArmorStand)ent;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void despawn() {
        this.removeEntity(this.entity);
    }
    
    public void remove() {
        this.removeEntity(this.entity);
        SkullReflection.skulls.remove(this);
    }
    
    public void removeEntity(final Object entity) {
        try {
            final Object craftWorld = SkullReflection.craftWorld.cast(this.location.getWorld());
            SkullReflection.nmsWorld.getMethod("removeEntity", SkullReflection.entityClass).invoke(SkullReflection.craftWorld.getMethod("getHandle", (Class<?>[])new Class[0]).invoke(craftWorld, new Object[0]), entity);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void removeEntityPacket(final Object entity) {
        try {
            final Object id = entity.getClass().getMethod("getId", (Class<?>[])new Class[0]).invoke(entity, new Object[0]);
            final Object packet = this.getRemovePacket((int)id);
            if (packet != null) {
                this.sendPacket(packet);
            }
        }
        catch (Exception ex) {}
    }
    
    private byte toAngle(final float v) {
        return (byte)(v * 256.0f / 360.0f);
    }
    
    public void teleport(final Location loc) {
        try {
            final Object packet = SkullReflection.entityTeleport.newInstance();
            this.setValue(packet, "a", this.ids);
            this.setValue(packet, "b", (int)(loc.getX() * 32.0));
            this.setValue(packet, "c", (int)(loc.getY() * 32.0));
            this.setValue(packet, "d", (int)(loc.getZ() * 32.0));
            this.setValue(packet, "e", this.toAngle(loc.getYaw()));
            this.setValue(packet, "f", this.toAngle(loc.getPitch()));
            this.location = loc;
            this.sendPacket(packet);
        }
        catch (Exception ex) {}
    }
    
    public void updateTarget() {
        try {
            final Object packet = SkullReflection.metadata.newInstance();
            this.setValue(packet, "a", this.ids);
            this.setValue(packet, "b", this.getValue(this.entity, "datawatcher"));
            this.setValue(packet, "c", false);
            this.sendPacket(packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setHeadPose(final float yaw, final float pitch) {
        try {
            final Object vector = SkullReflection.vector3f.getConstructor(Float.TYPE, Float.TYPE, Float.TYPE).newInstance(pitch, 0.0f, 0.0f);
            this.entity.getClass().getMethod("setHeadPose", SkullReflection.vector3f).invoke(this.entity, vector);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setHelmet(final ItemStack item) {
        try {
            final Object packetEquip = SkullReflection.equipPacket.newInstance();
            this.setValue(packetEquip, "a", this.ids);
            this.setValue(packetEquip, "b", 4);
            this.setValue(packetEquip, "c", convertToNMS(item));
            this.sendPacket(packetEquip);
            final Method setCustomNameVisible = this.entity.getClass().getMethod("setCustomNameVisible", Boolean.TYPE);
            setCustomNameVisible.invoke(this.entity, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private Object getRemovePacket(final int id) {
        try {
            final Class<?> packet = Class.forName("net.minecraft.server." + SkullReflection.version + "PacketPlayOutEntityDestroy");
            return packet.getConstructor(int[].class).newInstance(new int[] { id });
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private void spawnteste() {
    }
    
    public void update() {
        try {
            final Location current = this.location.clone().add(0.0, this.offset, 0.0);
            final String text = ChatColor.translateAlternateColorCodes('&', this.lines);
            this.configureSkull(this.entity, text, current);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void configureSkull(final Object entityObject, final String text, final Location location) throws Exception {
        final Method setCustomName = entityObject.getClass().getMethod("setCustomName", String.class);
        final Method setCustomNameVisible = entityObject.getClass().getMethod("setCustomNameVisible", Boolean.TYPE);
        final Method setNoGravity = entityObject.getClass().getMethod("setGravity", Boolean.TYPE);
        final Method setLocation = entityObject.getClass().getMethod("setLocation", Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
        final Method setInvisible = entityObject.getClass().getMethod("setInvisible", Boolean.TYPE);
        final Method setSmall = entityObject.getClass().getMethod("setSmall", Boolean.TYPE);
        final Method setBasePlate = entityObject.getClass().getMethod("setBasePlate", Boolean.TYPE);
        setCustomName.invoke(entityObject, text);
        setCustomNameVisible.invoke(entityObject, true);
        setNoGravity.invoke(entityObject, true);
        setLocation.invoke(entityObject, location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f);
        setInvisible.invoke(entityObject, true);
        setSmall.invoke(entityObject, true);
        setBasePlate.invoke(entityObject, true);
    }
    
    private Object[] getCreatePacket(final Location location, final String text) {
        try {
            final Object entityObject = SkullReflection.armorStand.getConstructor(SkullReflection.nmsWorld).newInstance(SkullReflection.craftWorld.getMethod("getHandle", (Class<?>[])new Class[0]).invoke(SkullReflection.craftWorld.cast(location.getWorld()), new Object[0]));
            final Object id = entityObject.getClass().getMethod("getId", (Class<?>[])new Class[0]).invoke(entityObject, new Object[0]);
            this.configureSkull(entityObject, text, location);
            return new Object[] { SkullReflection.spawnEntityLiving.getConstructor(SkullReflection.entityLiving).newInstance(entityObject), id };
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Object convertToNMS(final ItemStack itemConvertido) {
        try {
            final Class<?> craftItemStackClass = getOBCClass("inventory.CraftItemStack");
            return craftItemStackClass.getMethod("asNMSCopy", Class.forName("org.bukkit.inventory.ItemStack")).invoke(null, itemConvertido);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    public void setValue(final Object obj, final String name, final Object value) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        }
        catch (Exception ex) {}
    }
    
    public Object getValue(final Object obj, final String name) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Class<?> getNMSClass(final String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + SkullReflection.version + name);
    }
    
    public static Class<?> getOBCClass(final String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + SkullReflection.version + name);
    }
    
    private void sendPacket(final Player player, final Object packet) {
        try {
            if (packet == null) {
                return;
            }
            final Object entityPlayer = player.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(player, new Object[0]);
            final Object conexao = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            conexao.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + SkullReflection.version + "Packet")).invoke(conexao, packet);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void sendPacket(final Object packet) {
        if (packet == null) {
            return;
        }
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        Object entityPlayer;
        Object conexao;
        Bukkit.getOnlinePlayers().forEach(p -> {
            try {
                entityPlayer = p.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(p, new Object[0]);
                conexao = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
                conexao.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + SkullReflection.version + "Packet")).invoke(conexao, packet);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    
    static {
        SkullReflection.skulls = new CopyOnWriteArrayList<SkullReflection>();
        SkullReflection.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        try {
            SkullReflection.craftWorld = Class.forName("org.bukkit.craftbukkit." + SkullReflection.version + "CraftWorld");
            SkullReflection.entityClass = Class.forName("net.minecraft.server." + SkullReflection.version + "Entity");
            SkullReflection.nmsWorld = Class.forName("net.minecraft.server." + SkullReflection.version + "World");
            SkullReflection.armorStand = Class.forName("net.minecraft.server." + SkullReflection.version + "EntityArmorStand");
            SkullReflection.entityLiving = Class.forName("net.minecraft.server." + SkullReflection.version + "EntityLiving");
            SkullReflection.spawnEntityLiving = Class.forName("net.minecraft.server." + SkullReflection.version + "PacketPlayOutSpawnEntityLiving");
            SkullReflection.removePacket = Class.forName("net.minecraft.server." + SkullReflection.version + "PacketPlayOutEntityDestroy");
            SkullReflection.itemStack = Class.forName("net.minecraft.server." + SkullReflection.version + "ItemStack");
            SkullReflection.equipPacket = Class.forName("net.minecraft.server." + SkullReflection.version + "PacketPlayOutEntityEquipment");
            SkullReflection.entityTeleport = Class.forName("net.minecraft.server." + SkullReflection.version + "PacketPlayOutEntityTeleport");
            SkullReflection.entityLook = Class.forName("net.minecraft.server." + SkullReflection.version + "PacketPlayOutEntity$PacketPlayOutEntityLook");
            SkullReflection.vector3f = Class.forName("net.minecraft.server." + SkullReflection.version + "Vector3f");
            SkullReflection.metadata = Class.forName("net.minecraft.server." + SkullReflection.version + "PacketPlayOutEntityMetadata");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
