/*
 * Copyright (C) 2017 MCME
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mcmiddleearth.perks.perks;

import com.mcmiddleearth.perks.PerksPlugin;
import com.mcmiddleearth.perks.commands.LightHandler;
import com.mcmiddleearth.perks.listeners.LightListener;
import com.mcmiddleearth.perks.permissions.PermissionData;
import com.mcmiddleearth.perks.permissions.Permissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

/**
 *
 * @author Eriol_Eandur
 */
public class WizardLightPerk extends Perk {
    
    @Setter
    private static double speedFactor = PerksPlugin.getPerkDouble("light", "speedFactor",0.1);
    
    @Setter
    private static double maxSpeed = PerksPlugin.getPerkDouble("light", "maxSpeed",1);
    
    private final static int startDistance = PerksPlugin.getPerkInt("light", "startDistance",6);

    @Getter
    private final static int maxIntensity = PerksPlugin.getPerkInt("light", "maxIntensity",1);
            
    private final static int maxDuration = PerksPlugin.getPerkInt("light", "maxDuration",12000); //ticks
    
    private final static int teleportDistance = PerksPlugin.getPerkInt("light", "teleportDistance", 50);
    
    private static final double goldenRatio = (Math.sqrt(5)+1)/2;
    
    @Getter
    private static final Material item = Material.valueOf(PerksPlugin.getPerkString("light","item","STICK"));
    
    @Getter
    private static final Map<Player, WizardLight> lights = new HashMap<>();
    
    private static BukkitTask lightController;
    
    public WizardLightPerk() {
        super("light");
        setCommandHandler(new LightHandler(this, Permissions.USER.getPermissionNode()),"light");
        setListener(new LightListener());
    }
    
    public static void summonLight(final Player player) {
        WizardLight light = new WizardLight(player.getEyeLocation().toVector()
                                                  .add(player.getLocation().getDirection())
                                            .toLocation(player.getWorld()));
        lights.put(player, light);
        new BukkitRunnable() {
            @Override
            public void run() {
                unsummonLight(player);
            }
        }.runTaskLater(PerksPlugin.getInstance(), maxDuration);
    }
    
    public static void unsummonLight(Player player) {
        WizardLight light = lights.get(player);
        if(light!=null) {
            light.unsummon();
            lights.remove(player);
        }
    }
    
    public static boolean hasLight(Player player) {
        return lights.containsKey(player);
    }
    
    public static void increaseDistance(Player player) {
        if(lights.containsKey(player)) {
            WizardLight light = lights.get(player);
            light.setDistance(light.getDistance()+1);
        }
    }
  
    public static void decreaseDistance(Player player) {
        if(lights.containsKey(player)) {
            WizardLight light = lights.get(player);
            light.setDistance(Math.max(light.getDistance()-1,2));
        }
    }
    
    public static void setIntensity(Player player, int intensity) {
        if(lights.containsKey(player)) {
            WizardLight light = lights.get(player);
            light.setIntensity(intensity);
        }
    }
  
    @Override
    public void disable() {
        for(Player player: lights.keySet()) {
            unsummonLight(player);
        }
        if(lightController!=null) {
            lightController.cancel();
        }
        lightController = null;
    }
    
    @Override
    public void enable() {
        lightController = new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player: lights.keySet()) {
                    WizardLight light = lights.get(player);
                    if(light.isValid()) {
                        light.refresh();
                        Vector aim = player.getEyeLocation().toVector()
                                           .add(player.getLocation().getDirection().multiply(light.getDistance()))
                                           .add(new Vector(0,1,0));
                        light.setAim(aim);
//Logger.getGlobal().info("aim "+aim.toString());                        
                        /*Vector vel = aim.subtract(light.getLocation().toVector());
                        //Vector vel = light.getVelocity()
                        //              .add((vector2.subtract(light.getVelocity())).multiply(a));
                        //light.setVelocity(vector2.multiply(0.1));
                        vel = vel.multiply(a);
                        light.setVelocity((vel.length()<Math.min(b, 1)?vel
                                                           :vel.normalize().multiply(Math.min(b, 1))));*/
                    } else {
                        unsummonLight(player);
                    }
                }
            }
        }.runTaskTimer(PerksPlugin.getInstance(), 1, 3);
    }
    
    @Override
    public void check() {
        for(Player player:lights.keySet()) {
            if(!PermissionData.isAllowed(player, this)) {
                unsummonLight(player);
            }
        }
    }
    
    @Override
    public void writeDefaultConfig(ConfigurationSection config) {
        config.set("startDistance", startDistance);
        config.set("teleportDistance", teleportDistance);
        config.set("maxDuration", maxDuration);
        config.set("maxIntensity", maxIntensity);
        config.set("maxSpeed", speedFactor);
        config.set("speedFactor", maxSpeed);
        config.set("item", item.name());
    }
    
    private static class WizardLight {
        
        @Getter
        private int distance = startDistance;
        
        private int intensity = 1;
        
        
        private Location aim;
        
        private final Map<Entity,Vector> entities;
        
        public WizardLight(Location loc) {
            //entity = loc.getWorld().spawn(loc, Item.class);
            entities = new HashMap<>();
            this.aim = loc;
            summonEntity(loc,0,0,0,Material.BEACON);
            //entity.setFireTicks(Integer.MAX_VALUE);
        }
        
        private void summonEntity(Location loc, double x, double y, double z, Material mat) {
            Vector relativeLoc = new Vector(x,y,z);
            Location locC = loc.clone();
            Item entity = loc.getWorld().dropItemNaturally(locC.add(relativeLoc), new ItemStack(mat));
            entity.setItemStack(new ItemStack(mat));
            entity.setPickupDelay(600*20);
            //setInvisible();
            entity.setSilent(true);
            entity.setGravity(false);
            entities.put(entity, relativeLoc);
        }
        
        public void unsummon() {
            for(Entity entity:entities.keySet()) {
                entity.remove();
            }
            entities.clear();
        }
        
        public void setAim(Vector aim) {
            this.aim = aim.toLocation(this.aim.getWorld());
            for(Entity entity: entities.keySet()) {
                Vector temp = aim.clone();
                double distance = temp.subtract(entity.getLocation().toVector()).length();
//Logger.getGlobal().info("tdist: "+teleportDistance+ "   dist: "+distance);                    
                if(distance>teleportDistance) {
//Logger.getGlobal().info("teleport");
                    entity.teleport(this.aim.toVector().add(entities.get(entity)).toLocation(this.aim.getWorld()));
                } else {
                    temp = aim.clone();
                    Vector vel = temp.add(entities.get(entity)).subtract(entity.getLocation().toVector());
                    vel = vel.multiply(speedFactor);
                    double length = vel.length();
                    //if(length>maxSpeed/50) {
                        entity.setVelocity((vel.length()<Math.min(maxSpeed, 1)?vel
                                                       :vel.normalize().multiply(Math.min(maxSpeed, 1))));
                }
//Logger.getGlobal().info("vel "+vel.toString()+ " maxSpeed: "+maxSpeed+"   speedFactor: "+speedFactor+"   aim: "+aim.toString());                        
                //entity.setVelocity(velocity);
            }
        }
        
        /*public Vector getVelocity() {
            return entity.getVelocity();
        }*/
        
        public void setDistance(int distance) {
            if(distance<0) {
                distance = 0;
            }
            if(distance>100) {
                distance = 100;
            }
            this.distance = distance;
        }
        
        public void setIntensity(int intensity) {
            if(intensity>maxIntensity) {
                return;
            }
            if(this.intensity!=intensity) {
                unsummon();
                intensity = (intensity<=10?intensity:10);
                this.intensity = intensity;
                //if(intensity==1) {
                summonEntity(aim,0,0,0,Material.BEACON);
                if(intensity>1) {
                    if(intensity<4){
                        createIkosaeder(intensity);
                    } else if(intensity<6){
                        //summonEntity(aim,0,0,0);
                        createIkosaeder(intensity);
                        createDodecaeder(intensity);
                    } else if(intensity<8){
                        createIkosaeder(intensity/2);
                        createIkosaeder(intensity);
                        createDodecaeder(intensity);
                    } else {
                        //summonEntity(aim,0,0,0);
                        createIkosaeder(intensity/2);
                        createIkosaeder(intensity);
                        createDodecaeder(intensity);
                    }
                }
                
            }
        }
        
        private void createDodecaeder(double size) {
            double f = 2.6;
            double a = size/2;
            double b = a*goldenRatio;
            double c = (a+2*b)/f;
            double d = b/f;
            double e = (a+b)/f;
            
            summonEntity(aim, 0,  c,  d,Material.SEA_LANTERN);
            summonEntity(aim, 0,  c, -d,Material.SEA_LANTERN);
            summonEntity(aim, 0, -c,  d,Material.SEA_LANTERN);
            summonEntity(aim, 0, -c, -d,Material.SEA_LANTERN);

            summonEntity(aim, -d,  0,  c,Material.SEA_LANTERN);
            summonEntity(aim,  d,  0,  c,Material.SEA_LANTERN);
            summonEntity(aim, -d,  0, -c,Material.SEA_LANTERN);
            summonEntity(aim,  d,  0, -c,Material.SEA_LANTERN);

            summonEntity(aim,  c, -d,  0,Material.SEA_LANTERN);
            summonEntity(aim,  c,  d,  0,Material.SEA_LANTERN);
            summonEntity(aim, -c, -d,  0,Material.SEA_LANTERN);
            summonEntity(aim, -c,  d,  0,Material.SEA_LANTERN);
            
            summonEntity(aim,  e,  e,  e,Material.SEA_LANTERN);
            summonEntity(aim,  e,  e, -e,Material.SEA_LANTERN);
            summonEntity(aim,  e, -e,  e,Material.SEA_LANTERN);
            summonEntity(aim, -e,  e,  e,Material.SEA_LANTERN);
            summonEntity(aim,  e, -e, -e,Material.SEA_LANTERN);
            summonEntity(aim, -e, -e,  e,Material.SEA_LANTERN);
            summonEntity(aim, -e,  e, -e,Material.SEA_LANTERN);
            summonEntity(aim, -e, -e, -e,Material.SEA_LANTERN);
        }
        
        private void createIkosaeder(double size) {
            double a = size/2;
            double b= a*goldenRatio;
            
            summonEntity(aim, -a, -b, 0,Material.SEA_LANTERN);
            summonEntity(aim,  a, -b, 0,Material.SEA_LANTERN);
            summonEntity(aim, -a,  b, 0,Material.SEA_LANTERN);
            summonEntity(aim,  a,  b, 0,Material.SEA_LANTERN);

            summonEntity(aim,  0, -a,  b,Material.SEA_LANTERN);
            summonEntity(aim,  0,  a,  b,Material.SEA_LANTERN);
            summonEntity(aim,  0, -a, -b,Material.SEA_LANTERN);
            summonEntity(aim,  0,  a, -b,Material.SEA_LANTERN);

            summonEntity(aim,  -b,  0, -a,Material.SEA_LANTERN);
            summonEntity(aim,  -b,  0,  a,Material.SEA_LANTERN);
            summonEntity(aim,   b,  0, -a,Material.SEA_LANTERN);
            summonEntity(aim,   b,  0,  a,Material.SEA_LANTERN);
        }
        /*public Location getLocation() {
            return entity.getLocation();
        }*/
        
        public boolean isValid() {
            for(Entity entity: entities.keySet()) {
                if(entity.isValid()) {
                    return true;
                }
            }
            return false;
                //return entity.isValid();
        }
        
        public void refresh() {
            List<Entity> notValid = new ArrayList<>();
            for(Entity entity: entities.keySet()) {
                if(entity.isValid()) {
                    entity.setTicksLived(1);
                } else {
                    notValid.add(entity);
                }
            }
            for(Entity entity: notValid) {
                entities.remove(entity);
            }
                
            //setInvisible();
            //entity.setFireTicks(Integer.MAX_VALUE);
        }
    
        /*public void setInvisible() {
            try {
        //Logger.getGlobal().info(""+entity.getClass().getSuperclass());
                Field nmsEntityField = entity.getClass().getSuperclass().getDeclaredField("entity");
                nmsEntityField.setAccessible(true);
                Object nmsEntity = nmsEntityField.get(entity);
        //ReflectionUtil.showDeclaredMethods(nmsEntity);
        //ReflectionUtil.showMethods(nmsEntity);
                nmsEntity.getClass().getMethod("setInvisible", boolean.class).invoke(nmsEntity, true);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                Logger.getLogger(WizardLightPerk.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,
                                                           Integer.MAX_VALUE, 5));
            ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                                                           Integer.MAX_VALUE, 1));
        }*/
    }
}
    