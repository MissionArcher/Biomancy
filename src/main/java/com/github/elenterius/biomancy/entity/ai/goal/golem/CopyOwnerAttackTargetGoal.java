package com.github.elenterius.biomancy.entity.ai.goal.golem;

import com.github.elenterius.biomancy.entity.golem.IGolem;
import com.github.elenterius.biomancy.entity.golem.IOwnableCreature;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;
import java.util.Optional;

public class CopyOwnerAttackTargetGoal<T extends CreatureEntity & IOwnableCreature & IGolem> extends TargetGoal {

	private final T entity;
	private LivingEntity attacker;
	private int lastAttackTime;

	public CopyOwnerAttackTargetGoal(T goalOwner) {
		super(goalOwner, false);
		entity = goalOwner;
		setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		if (!entity.isGolemInactive() && entity.getGolemCommand() == IGolem.Command.DEFEND_OWNER) {
			Optional<PlayerEntity> entityOwner = entity.getOwner();
			if (entityOwner.isPresent()) {
				attacker = entityOwner.get().getLastHurtMob();
				int attackTimer = entityOwner.get().getLastHurtMobTimestamp();
				return attackTimer != lastAttackTime && canAttack(attacker, EntityPredicate.DEFAULT) && entity.shouldAttackEntity(attacker, entityOwner.get());
			}
		}
		return false;
	}

	@Override
	public void start() {
		mob.setTarget(attacker);
		Optional<PlayerEntity> optional = entity.getOwner();
		optional.ifPresent(playerEntity -> lastAttackTime = playerEntity.getLastHurtMobTimestamp());
		super.start();
	}
}
