package dejt.kaku.flasher.mobbehaviour;

import com.google.common.collect.ImmutableSet;
import dejt.kaku.flasher.LightFlasher;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

@Mod.EventBusSubscriber(modid = LightFlasher.MODID)
public class MobBehaviour {

    public final static List<Class<? extends EntityAIBase>> OVERRIDE_AI = Arrays.asList(EntityAINearestAttackableTarget.class, EntityAIHurtByTarget.class);
    public final static ImmutableSet<Class<? extends EntityMob>> MOBS;

    @SubscribeEvent
    public static void entityJoinsWorld(EntityJoinWorldEvent ev){
        World world = ev.getWorld();
        if(ev.getEntity() instanceof EntityLiving){
            replaceEntityAttr((EntityLiving)ev.getEntity(), world, ev);
        }
    }

    private static void replaceEntityAttr(EntityLiving entityIn, World worldIn, EntityJoinWorldEvent event){
        if(gatedMobs(entityIn)){
            Set<EntityAITasks.EntityAITaskEntry> targetTaskEntries = entityIn.targetTasks.taskEntries;
            for(EntityAITasks.EntityAITaskEntry entry : targetTaskEntries.toArray(new EntityAITasks.EntityAITaskEntry[0])){
                EntityAIBase behaviour = entry.action;
                if(gatedAI(behaviour)){
                    entityIn.targetTasks.removeTask(behaviour);
                }
            }
            if (LightFlasher.configuration.allowChasingWithoutHit){
                entityIn.targetTasks.addTask(1, new EntityAINearestAttackableTargetModified(entityIn, EntityPlayer.class));
            }
            entityIn.targetTasks.addTask(2, new EntityAIHurtByTargetModified(entityIn));
        }
    }

    private static boolean gatedAI(EntityAIBase behaviour){
        return OVERRIDE_AI.contains(behaviour.getClass());
    }

    private static boolean gatedMobs(EntityLiving entity){
        return MOBS.contains(entity.getClass());
    }

    private static List<Class<? extends EntityMob>> getMobList() throws ClassNotFoundException {
        List<String> mobStringID = LightFlasher.configuration.entityList_affected;
        List<Class<? extends EntityMob>> mobClass = new ArrayList<>();
        for(String mobName : mobStringID){
            switch(mobName.toLowerCase(Locale.ENGLISH)){ //change locale and case strings if you do not use English.
                case "creeper":
                    mobClass.add(EntityCreeper.class);
                    break;
                case "skeleton":
                    mobClass.add(EntitySkeleton.class);
                    break;
                case "spider":
                    mobClass.add(EntitySpider.class);
                    break;
                case "zombie":
                    mobClass.add(EntityZombie.class);
                    break;
                case "cave_spider":
                case "cavespider":
                case "spidercave":
                case "spider:cave":
                    mobClass.add(EntityCaveSpider.class);
                    break;
                case "blaze":
                    mobClass.add(EntityBlaze.class);
                    break;
                case "elder_guardian":
                case "elderguardian":
                case "guardian_elder":
                    mobClass.add(EntityElderGuardian.class);
                    break;
                case "enderman":
                case "endermen":
                    mobClass.add(EntityEnderman.class);
                    break;
                case "endermite":
                    mobClass.add(EntityEndermite.class);
                    break;
                case "evoker":
                case "evoker_illager":
                case "evokerillager":
                    mobClass.add(EntityEvoker.class);
                    break;
                case "giant":
                case "giantzombie":
                case "giant_zombie":
                case "zombie_giant":
                case "zombiegiant":
                    mobClass.add(EntityGiantZombie.class);
                    break;
                case "guardian":
                    mobClass.add(EntityGuardian.class);
                    break;
                case "husk":
                case "desert_zombie":
                    mobClass.add(EntityHusk.class);
                    break;
                case "illusion_illager":
                    mobClass.add(EntityIllusionIllager.class);
                    break;
                case "zombiepigman":
                case "pig_zombie":
                case "zombie_pigman":
                    mobClass.add(EntityPigZombie.class);
                    break;
                case "silverfish":
                    mobClass.add(EntitySilverfish.class);
                    break;
                case "spellcaster_illager":
                case "caster":
                    mobClass.add(EntitySpellcasterIllager.class);
                    break;
                case "stray":
                case "stray_skeleton":
                case "skeleton_stray":
                    mobClass.add(EntityStray.class);
                    break;
                case "vex":
                    mobClass.add(EntityVex.class);
                    break;
                case "vindicator":
                    mobClass.add(EntityVindicator.class);
                    break;
                case "witch":
                    mobClass.add(EntityWitch.class);
                    break;
                case "witherskeleton":
                case "wither_skeleton":
                case "skeleton_wither":
                    mobClass.add(EntityWitherSkeleton.class);
                    break;
                case "villager_zombie":
                case "zombie_villager":
                    mobClass.add(EntityZombieVillager.class);
                    break;
                    //common
                case "illagermob":
                    mobClass.add(EntityEvoker.class);
                    mobClass.add(EntityIllusionIllager.class);
                    mobClass.add(EntitySpellcasterIllager.class);
                    mobClass.add(EntityVindicator.class);
                    break;
                case "common_mob":
                case "mob":
                    mobClass.add(EntityCreeper.class);
                    mobClass.add(EntityZombie.class);
                    mobClass.add(EntitySkeleton.class);
                    mobClass.add(EntitySpider.class);
                    break;
                default:
                    throw new ClassNotFoundException("Major Error! Settings has errored entityList entry. Please check and rectify.");
            }
        }
        return mobClass;
    }

    static {
        try {
            MOBS = ImmutableSet.copyOf(getMobList());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
