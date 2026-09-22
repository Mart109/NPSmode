package com.example.npcmod.entity;

import com.example.npcmod.npcdata.NpcData;
import com.example.npcmod.screen.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CustomNpcEntity extends PathAwareEntity implements GeoEntity, NamedScreenHandlerFactory {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private NpcData.Info npcInfo;
    private int npcIndex = -1;

    public CustomNpcEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(2, new LookAroundGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.6));
    }

    public void setNpcInfo(int index) {
        this.npcIndex = index;
        if (index >= 0 && index < 62) {   // ← ИСПРАВЛЕНО С 33 НА 62
            NpcData.registerNpc(this.getUuid(), index);
            this.npcInfo = NpcData.getInfo(this.getUuid());
            this.setCustomName(Text.literal(npcInfo.name + " " + npcInfo.type.displayName));
            this.setCustomNameVisible(true);
            // setupWaypoints(); ← УДАЛЕНО, метод больше не существует
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().then("animation.model.walk", Animation.LoopType.LOOP));
        } else {
            state.getController().setAnimation(RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient && player instanceof ServerPlayerEntity) {
            player.openHandledScreen(this);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Text getDisplayName() {
        if (npcInfo != null) return Text.literal(npcInfo.name);
        return Text.literal("§7NPC");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        if (npcInfo == null) return null;
        return switch (npcInfo.type) {
            case MERCHANT -> new MerchantScreenHandler(syncId, inv,
                    ScreenHandlerContext.create(this.getWorld(), this.getBlockPos()), this);
            case TRANSPORTER -> new TransporterScreenHandler(syncId, inv,
                    ScreenHandlerContext.create(this.getWorld(), this.getBlockPos()), this);
            default -> new DialogueScreenHandler(syncId, inv,
                    ScreenHandlerContext.create(this.getWorld(), this.getBlockPos()), this);
        };
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("NpcIndex", npcIndex);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("NpcIndex")) setNpcInfo(nbt.getInt("NpcIndex"));
    }

    public NpcData.Info getNpcInfo() { return npcInfo; }
}