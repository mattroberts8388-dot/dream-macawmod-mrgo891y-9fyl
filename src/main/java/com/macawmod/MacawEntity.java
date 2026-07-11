package com.macawmod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class MacawEntity extends TameableShoulderEntity {

    public MacawEntity(EntityType<? extends MacawEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(false);
    }

    public static DefaultAttributeContainer.Builder createMacawAttributes() {
        return AnimalEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4)
            .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.0, 5.0f, 1.0f, true));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!this.isTamed() && isSeed(stack)) {
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            if (!this.getWorld().isClient) {
                if (this.random.nextInt(3) == 0) {
                    this.setOwner(player);
                    this.setTamed(true);
                    this.getWorld().sendEntityStatus(this, (byte) 7);
                } else {
                    this.getWorld().sendEntityStatus(this, (byte) 6);
                }
            }
            return ActionResult.success(this.getWorld().isClient);
        }

        if (this.isTamed() && this.isOwner(player) && !this.getWorld().isClient) {
            if (isSeed(stack)) {
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
                this.heal(2.0f);
                return ActionResult.SUCCESS;
            }
            if (!this.isSitting() && !player.hasVehicle()) {
                boolean mounted = this.mountOnto(player);
                if (mounted) {
                    return ActionResult.SUCCESS;
                }
            }
            this.setSitting(!this.isSitting());
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    private static boolean isSeed(ItemStack stack) {
        return stack.isOf(Items.WHEAT_SEEDS)
            || stack.isOf(Items.MELON_SEEDS)
            || stack.isOf(Items.PUMPKIN_SEEDS)
            || stack.isOf(Items.BEETROOT_SEEDS);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient && this.isTamed() && this.getOwner() instanceof PlayerEntity owner) {
            if (owner.isAlive() && this.squaredDistanceTo(owner) < 100.0) {
                owner.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 220, 0, true, false, true));
            }
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return isSeed(stack);
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        MacawEntity child = MacawModEntities.MACAW.create(world);
        if (child != null && this.isTamed() && this.getOwnerUuid() != null) {
            child.setOwnerUuid(this.getOwnerUuid());
            child.setTamed(true);
        }
        return child;
    }

    @Override
    public boolean canFly() {
        return true;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, net.minecraft.entity.damage.DamageSource damageSource) {
        return false;
    }

    @Override
    public net.minecraft.sound.SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PARROT_AMBIENT;
    }

    @Override
    protected net.minecraft.sound.SoundEvent getHurtSound(net.minecraft.entity.damage.DamageSource source) {
        return SoundEvents.ENTITY_PARROT_HURT;
    }

    @Override
    protected net.minecraft.sound.SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PARROT_DEATH;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }
}