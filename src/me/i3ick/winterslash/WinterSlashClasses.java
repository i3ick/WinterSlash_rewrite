package me.i3ick.winterslash;


import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R3.metadata.BlockMetadataStore;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Chest;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class WinterSlashClasses {
    
    public void giveTools(Player p){
        int newItemSlot = p.getInventory().firstEmpty();
        ItemStack revivor = new ItemStack(Material.BLAZE_ROD, 1);
        p.getInventory().setItem(newItemSlot, revivor);
    }
    
    public void setRunner(Player p){
        int newItemSlot = p.getInventory().firstEmpty();
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, 24000, 2); 
        PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.JUMP, 24000, 2); 
        ItemStack knife = new ItemStack(Material.GOLD_SWORD, 1);
        ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
        potionEffect.apply(p); 
        potionEffect2.apply(p); 
        p.getInventory().addItem(knife);
        p.getInventory().setChestplate(chest);
        p.updateInventory();
    }
    
    public void setHeavy(Player p){
        int newItemSlot = p.getInventory().firstEmpty();
        ItemStack knife = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        ItemStack legs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, 24000, 2); 
        potionEffect.apply(p);
        chest.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
        knife.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
        p.getInventory().setItem(newItemSlot, knife);
        p.getInventory().setChestplate(chest);
        p.getInventory().setLeggings(legs);
        p.updateInventory();
        
    }
    
    public void setArcher(Player p){
        int newItemSlot = p.getInventory().firstEmpty();
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 4);
        bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 4);
        ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        p.getInventory().setChestplate(chest);
        p.getInventory().addItem(arrow);
        p.getInventory().addItem(bow);
        p.updateInventory();
    }
    
    public void setDefault(Player p){
        int newItemSlot = p.getInventory().firstEmpty();
        ItemStack knife = new ItemStack(Material.IRON_SWORD, 1);
        ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1); 
        p.getInventory().addItem(knife);
        p.getInventory().setChestplate(chest);
        p.updateInventory();
    }

    public void redArmor(Player p) {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack nothing = new ItemStack(Material.AIR, 1);
        LeatherArmorMeta am = (LeatherArmorMeta) boots.getItemMeta();
        LeatherArmorMeta am2 = (LeatherArmorMeta) leggings.getItemMeta();
        am.setColor(Color.fromRGB(100, 0, 0));
        boots.setItemMeta(am);
        leggings.setItemMeta(am);
        p.getInventory().setBoots(boots);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setHelmet(nothing);

    }
    
    public void redFrozenArmor(Player p) {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack shirt = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack nothing = new ItemStack(Material.AIR, 1);
        LeatherArmorMeta am = (LeatherArmorMeta) boots.getItemMeta();
        LeatherArmorMeta am2 = (LeatherArmorMeta) leggings.getItemMeta();
        LeatherArmorMeta am3 = (LeatherArmorMeta) shirt.getItemMeta();
        am.setColor(Color.fromRGB(100, 0, 0));
        am2.setColor(Color.fromRGB(100, 0, 0));
        am3.setColor(Color.fromRGB(100, 0, 0));
        boots.setItemMeta(am);
        leggings.setItemMeta(am);
        shirt.setItemMeta(am);
        p.getInventory().setBoots(boots);
        p.getInventory().setChestplate(shirt);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setHelmet(nothing);

    }

    public void greenArmor(Player p) {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack nothing = new ItemStack(Material.AIR, 1);
        LeatherArmorMeta am = (LeatherArmorMeta) boots.getItemMeta();
        LeatherArmorMeta am2 = (LeatherArmorMeta) leggings.getItemMeta();
        am.setColor(Color.fromRGB(0, 100, 0));
        boots.setItemMeta(am);
        leggings.setItemMeta(am);
        p.getInventory().setBoots(boots);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setHelmet(nothing);

    }
    
    public void greenFrozenArmor(Player p) {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack shirt = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack nothing = new ItemStack(Material.AIR, 1);
        LeatherArmorMeta am = (LeatherArmorMeta) boots.getItemMeta();
        LeatherArmorMeta am2 = (LeatherArmorMeta) leggings.getItemMeta();
        LeatherArmorMeta am3 = (LeatherArmorMeta) shirt.getItemMeta();
        am.setColor(Color.fromRGB(0, 100, 0));
        boots.setItemMeta(am);
        leggings.setItemMeta(am);
        shirt.setItemMeta(am);
        p.getInventory().setBoots(boots);
        p.getInventory().setChestplate(shirt);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setHelmet(nothing);

    }
    
    public void carePackage(Player p){
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1);
        ItemMeta am = (ItemMeta)skull.getItemMeta();
        am.setDisplayName(ChatColor.RED + "CarePackage drop marker");
        skull.setItemMeta(am);    
        p.getInventory().addItem(skull); 
    }
    
    public void decoyPackage(Player p){
        ItemStack flower = new ItemStack(Material.SKULL_ITEM, 1);
        ItemMeta am = (ItemMeta)flower.getItemMeta();
        am.setDisplayName(ChatColor.RED + "Decoy drop marker");
        flower.setItemMeta(am);    
        p.getInventory().addItem(flower); 
    }
    
    public void signalCare(Player p, Location pos){
        
        Firework f = (Firework) p.getWorld().spawn(p.getWorld().getBlockAt(pos).getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(false)
                .trail(true)
                .with(Type.CREEPER)
                .withColor(Color.BLUE)
                .withFade(Color.GREEN)
                .build());
        fm.setPower(1);
        f.setFireworkMeta(fm);
    }
    
    public void getCare(Player p, Location pos){
          Location fall = new Location(p.getWorld(), pos.getX(), pos.getY() + 0.3, pos.getZ());
        p.getWorld().spawnFallingBlock(fall, Material.CHEST, (byte) 0);
       /* p.getWorld().getBlockAt(pos).setType(Material.CHEST); */
    }

    
    public void getDecoy(Player p, Location pos){
                p.getWorld().getBlockAt(pos).setType(Material.TRAPPED_CHEST); 
  }

    public void randomPotion(Player p){
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, 600, 2);
        PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.JUMP, 600, 2);
        PotionEffect potionEffect3 = new PotionEffect(PotionEffectType.HEALTH_BOOST, 600, 2);
        PotionEffect potionEffect4 = new PotionEffect(PotionEffectType.ABSORPTION, 600, 2);
        PotionEffect potionEffect5 = new PotionEffect(PotionEffectType.REGENERATION, 600, 2);
        PotionEffect potionEffect6 = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 2);
        PotionEffect potionEffect7 = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600, 2);



        Random rnd = new Random();
        int i;
        i = rnd.nextInt(7);


        switch (i){
            case 0:
                potionEffect.apply(p);
                break;
            case 1:
                potionEffect2.apply(p);
                break;
            case 2:
                potionEffect3.apply(p);
                potionEffect5.apply(p);
                break;
            case 3:
                potionEffect4.apply(p);
                break;
            case 4:
                potionEffect5.apply(p);
                break;
            case 5:
                potionEffect6.apply(p);
                break;
            case 6:
                potionEffect7.apply(p);
                break;
        }
    }

}
