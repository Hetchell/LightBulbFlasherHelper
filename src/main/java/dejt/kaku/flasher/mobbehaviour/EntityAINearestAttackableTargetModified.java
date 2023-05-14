package dejt.kaku.flasher.mobbehaviour;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntityAINearestAttackableTargetModified<T extends EntityLivingBase> extends EntityAITarget {

    private final Class<T> targetClass;
    private final int targetChance;
    private final EntityAINearestAttackableTargetModified.Sorter sorter;
    private final Predicate <? super T > targetEntitySelector;
    private T targetEntity;
    private final EntityLiving entity;
    private int targetUnseenTicks;

    public EntityAINearestAttackableTargetModified(EntityLiving entityIn, Class<T> target){
        super((EntityCreature) entityIn, true, true);
        this.entity = entityIn;
        this.targetClass = target;
        this.targetChance = 10;
        this.setMutexBits(1);
        this.sorter = new EntityAINearestAttackableTargetModified.Sorter(entityIn);
        this.targetEntitySelector = t -> {
            if (t == null)
            {
                return false;
            }
            else
            {
                return EntitySelectors.NOT_SPECTATING.apply(t) && EntityAINearestAttackableTargetModified.this.isSuitableTarget(t, true);
            }
        };
    }


    @SuppressWarnings("unchecked")
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class)
        {
            List<T> list = this.taskOwner.world.<T>getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

            if (list.isEmpty())
            {
                return false;
            }
            else
            {
                Collections.sort(list, this.sorter);
                this.targetEntity = list.get(0);
                return true;
            }
        }
        else
        {
            this.targetEntity = (T)this.taskOwner.world.getNearestAttackablePlayer(
                    this.taskOwner.posX,
                    this.taskOwner.posY + (double)this.taskOwner.getEyeHeight(),
                    this.taskOwner.posZ, this.getTargetDistance(),
                    this.getTargetDistance(),
                    p -> {
                        ItemStack itemstack = p.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                        if (itemstack.getItem() == Items.SKULL)
                        {
                            int i = itemstack.getItemDamage();
                            boolean flag = EntityAINearestAttackableTargetModified.this.taskOwner instanceof EntitySkeleton && i == 0;
                            boolean flag1 = EntityAINearestAttackableTargetModified.this.taskOwner instanceof EntityZombie && i == 2;
                            boolean flag2 = EntityAINearestAttackableTargetModified.this.taskOwner instanceof EntityCreeper && i == 4;

                            if (flag || flag1 || flag2)
                            {
                                return 0.5D;
                            }
                        }

                        return 1.0D;
                    },
                    (Predicate<EntityPlayer>)this.targetEntitySelector);
            this.targetEntity = (T)this.taskOwner.getEntityWorld().getClosestPlayer(
                    this.entity.posX, this.entity.posY, this.entity.posZ,
                    50.0D,
                    p -> true
            );
            return this.targetEntity != null;
            //This works very well.
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();

        if (entitylivingbase == null)
        {
            entitylivingbase = this.target;
        }

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else
        {
            Team team = this.taskOwner.getTeam();
            Team team1 = entitylivingbase.getTeam();

            if (team != null && team1 == team)
            {
                return false;
            }
            else
            {
                double d0 = this.getTargetDistance();

                if (this.taskOwner.getDistanceSq(entitylivingbase) > d0 * d0)
                {
                    return false;
                }
                else
                {
                    if (this.shouldCheckSight)
                    {
                        if (this.taskOwner.getEntitySenses().canSee(entitylivingbase))
                        {
                            this.targetUnseenTicks = 0;
                        }
                        else if (++this.targetUnseenTicks > this.unseenMemoryTicks)
                        {
                            return false;
                        }
                    }

                        this.taskOwner.setAttackTarget(entitylivingbase);
                        return true;
                }
            }
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance)
    {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        this.targetUnseenTicks = 0;
        super.startExecuting();
    }

    public static class Sorter implements Comparator<Entity>
    {
        private final Entity entity;

        public Sorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double d0 = this.entity.getDistanceSq(p_compare_1_);
            double d1 = this.entity.getDistanceSq(p_compare_2_);

            if (d0 < d1)
            {
                return -1;
            }
            else
            {
                return d0 > d1 ? 1 : 0;
            }
        }
    }
}
