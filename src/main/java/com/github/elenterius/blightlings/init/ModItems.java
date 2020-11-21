package com.github.elenterius.blightlings.init;

import com.github.elenterius.blightlings.BlightlingsMod;
import com.github.elenterius.blightlings.item.BlightbringerAxeItem;
import com.github.elenterius.blightlings.item.GogglesArmorItem;
import com.github.elenterius.blightlings.item.ModItemTier;
import com.github.elenterius.blightlings.item.ModSpawnEggItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ModItems
{
    public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, BlightlingsMod.MOD_ID);

    private static Item.Properties createItemProperties() {
        return new Item.Properties().group(BlightlingsMod.ITEM_GROUP);
    }

    public static final RegistryObject<GogglesArmorItem> TRUE_SIGHT_GOGGLES = ITEM_REGISTRY.register("true_sight_goggles", () -> new GogglesArmorItem(ArmorMaterial.IRON, createItemProperties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BlightbringerAxeItem> BLIGHTBRINGER_AXE = ITEM_REGISTRY.register("blightbringer_axe",
            () -> new BlightbringerAxeItem(ModItemTier.BLIGHT_AMALGAM, 5F, -3.15F, createItemProperties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> BLIGHT_SHARD = ITEM_REGISTRY.register("blight_shard", () -> new Item(createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_SAC = ITEM_REGISTRY.register("blight_sac", () -> new Item(createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_GOO = ITEM_REGISTRY.register("blight_goo", () -> new Item(createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_STRING = ITEM_REGISTRY.register("blight_string", () -> new Item(createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_EYE = ITEM_REGISTRY.register("blight_eye", () -> new Item(createItemProperties()));

    public static final RegistryObject<ModSpawnEggItem> BLOBLING_SPAWN_EGG = ITEM_REGISTRY.register("blobling_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.BLOBLING, 0x764da2, 0xff40ff, createItemProperties()));
    public static final RegistryObject<ModSpawnEggItem> BROOD_MOTHER_SPAWN_EGG = ITEM_REGISTRY.register("brood_mother_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.BROOD_MOTHER, 0x49345e, 0xda70d6, createItemProperties()));

    public static final RegistryObject<Item> INFERTILE_SOIL_BLOCK_ITEM = ITEM_REGISTRY.register("infertile_soil", () -> new BlockItem(ModBlocks.INFERTILE_SOIL.get(), createItemProperties()));
    public static final RegistryObject<Item> LILYTREE_SAPLING_BLOCK_ITEM = ITEM_REGISTRY.register("lilytree_sapling", () -> new BlockItem(ModBlocks.LILY_TREE_SAPLING.get(), createItemProperties()));
    public static final RegistryObject<Item> LUMINOUS_SOIL_BLOCK_ITEM = ITEM_REGISTRY.register("luminous_soil", () -> new BlockItem(ModBlocks.LUMINOUS_SOIL.get(), createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_PUSTULE_BLOCK_ITEM = ITEM_REGISTRY.register("blight_pustule", () -> new BlockItem(ModBlocks.BLIGHT_PUSTULE.get(), createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_SPROUT_BLOCK_ITEM = ITEM_REGISTRY.register("blight_sprout", () -> new BlockItem(ModBlocks.BLIGHT_SPROUT.get(), createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_SPROUT_SMALL_BLOCK_ITEM = ITEM_REGISTRY.register("blight_sprout_small", () -> new BlockItem(ModBlocks.BLIGHT_SPROUT_SMALL.get(), createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_TENTACLE_0_BLOCK_ITEM = ITEM_REGISTRY.register("blight_tentacle_0", () -> new BlockItem(ModBlocks.BLIGHT_TENTACLE_0.get(), createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_TENTACLE_1_BLOCK_ITEM = ITEM_REGISTRY.register("blight_tentacle_1", () -> new BlockItem(ModBlocks.BLIGHT_TENTACLE_1.get(), createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_SHROOM_TALL_BLOCK_ITEM = ITEM_REGISTRY.register("blight_shroom_tall", () -> new BlockItem(ModBlocks.BLIGHT_SHROOM_TALL.get(), createItemProperties()));
    public static final RegistryObject<Item> BLIGHT_MOSS_SLAB_BLOCK_ITEM = ITEM_REGISTRY.register("blight_moss_slab", () -> new BlockItem(ModBlocks.BLIGHT_MOSS_SLAB.get(), createItemProperties()));
}