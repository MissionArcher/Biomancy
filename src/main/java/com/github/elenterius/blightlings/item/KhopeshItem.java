package com.github.elenterius.blightlings.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static net.minecraft.item.ItemStack.DECIMALFORMAT;

public class KhopeshItem extends AxeItem
{
    public static AttributeModifier ATTACK_DAMAGE_RIDING_MODIFIER = new AttributeModifier(UUID.fromString("CBD1DE77-3F1D-4E8B-839A-AA471A93D424"), "riding_attack_modifier", 4f, AttributeModifier.Operation.ADDITION);
    public static AttributeModifier REACH_RIDING_MODIFIER = new AttributeModifier(UUID.fromString("e488293e-0160-4be3-b7b2-35def9b8ab7e"), "riding_reach_modifier", 1f, AttributeModifier.Operation.ADDITION);

    public KhopeshItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(getTranslationKey(stack).replace("item", "tooltip")).setStyle(Style.EMPTY.applyFormatting(TextFormatting.GRAY)));
        tooltip.add(new StringTextComponent(" "));
        tooltip.add(new TranslationTextComponent("tooltip.blightlings.riding_bonus").setStyle(Style.EMPTY.applyFormatting(TextFormatting.GRAY)));
        tooltip.add((new StringTextComponent(" ")).append(new TranslationTextComponent("attribute.modifier.plus." + ATTACK_DAMAGE_RIDING_MODIFIER.getOperation().getId(), DECIMALFORMAT.format(ATTACK_DAMAGE_RIDING_MODIFIER.getAmount()), new TranslationTextComponent("attribute.name.generic.attack_damage"))).mergeStyle(TextFormatting.BLUE));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.isIn(Blocks.COBWEB) ? 15f : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean canHarvestBlock(BlockState blockIn) {
        return blockIn.isIn(Blocks.COBWEB) || super.canHarvestBlock(blockIn);
    }

    public static void removeSpecialAttributeModifiers(LivingEntity livingEntity) {
        ModifiableAttributeInstance modifiableAttributeInstance = livingEntity.getAttributeManager().createInstanceIfAbsent(Attributes.ATTACK_DAMAGE);
        if (modifiableAttributeInstance != null && modifiableAttributeInstance.hasModifier(ATTACK_DAMAGE_RIDING_MODIFIER)) {
            modifiableAttributeInstance.removeModifier(ATTACK_DAMAGE_RIDING_MODIFIER);
        }
        modifiableAttributeInstance = livingEntity.getAttributeManager().createInstanceIfAbsent(ForgeMod.REACH_DISTANCE.get());
        if (modifiableAttributeInstance != null && modifiableAttributeInstance.hasModifier(REACH_RIDING_MODIFIER)) {
            modifiableAttributeInstance.removeModifier(REACH_RIDING_MODIFIER);
        }
    }

    public static void applySpecialAttributeModifiers(LivingEntity livingEntity) {
        ModifiableAttributeInstance modifiableAttributeInstance = livingEntity.getAttributeManager().createInstanceIfAbsent(Attributes.ATTACK_DAMAGE);
        if (modifiableAttributeInstance != null && !modifiableAttributeInstance.hasModifier(ATTACK_DAMAGE_RIDING_MODIFIER)) {
            modifiableAttributeInstance.applyNonPersistentModifier(ATTACK_DAMAGE_RIDING_MODIFIER);
        }
        modifiableAttributeInstance = livingEntity.getAttributeManager().createInstanceIfAbsent(ForgeMod.REACH_DISTANCE.get());
        if (modifiableAttributeInstance != null && !modifiableAttributeInstance.hasModifier(REACH_RIDING_MODIFIER)) {
            modifiableAttributeInstance.applyNonPersistentModifier(REACH_RIDING_MODIFIER);
        }
    }
}
