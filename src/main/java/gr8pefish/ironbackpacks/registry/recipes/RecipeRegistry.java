package gr8pefish.ironbackpacks.registry.recipes;

import gr8pefish.ironbackpacks.api.IronBackpacksAPI;
import gr8pefish.ironbackpacks.api.item.backpacks.interfaces.IBackpack;
import gr8pefish.ironbackpacks.api.item.backpacks.interfaces.ITieredBackpack;
import gr8pefish.ironbackpacks.api.item.backpacks.interfaces.IUpgradableBackpack;
import gr8pefish.ironbackpacks.api.register.ItemBackpackRegistry;
import gr8pefish.ironbackpacks.api.register.ItemCraftingRegistry;
import gr8pefish.ironbackpacks.api.register.ItemUpgradeRegistry;
import gr8pefish.ironbackpacks.config.ConfigHandler;
import gr8pefish.ironbackpacks.crafting.BackpackAddUpgradeRecipe;
import gr8pefish.ironbackpacks.crafting.BackpackIncreaseTierRecipe;
import gr8pefish.ironbackpacks.crafting.BackpackRemoveUpgradeRecipe;
import gr8pefish.ironbackpacks.items.backpacks.ItemBackpack;
import gr8pefish.ironbackpacks.items.craftingItems.ItemCrafting;
import gr8pefish.ironbackpacks.items.upgrades.UpgradeMethods;
import gr8pefish.ironbackpacks.registry.ItemRegistry;
import gr8pefish.ironbackpacks.util.Logger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Register all the recipes here.
 */
public class RecipeRegistry {

	/**
	 * Main method that registers all the recipes
	 */
	public static void registerAllRecipes() {

        //Basic Item Recipes
        ItemCraftingRecipes.registerItemCraftingRecipes(); //register the recipes to get the crafting items
        ItemUpgradeRecipes.registerItemUpgradeRecipes(); //register the recipes to get the the upgrades as items //broken (crashes) //TODO: figure this bope out
		ItemBackpackRecipes.registerItemBackpackRecipes(); //register all the recipes to get the backpacks directly

        //=============Fancy Iron Backpacks Recipes===========

        //Register as special recipes
        RecipeSorter.register("RemoveUpgrade", BackpackRemoveUpgradeRecipe.class, RecipeSorter.Category.SHAPELESS, ""); //register my special recipe
        RecipeSorter.register("BackpackUpgrade", BackpackAddUpgradeRecipe.class, RecipeSorter.Category.SHAPELESS, ""); //register my special recipe
        RecipeSorter.register("BackpackTier", BackpackIncreaseTierRecipe.class, RecipeSorter.Category.SHAPED, ""); //register my special recipe

        //Register the recipes themselves
        BackpackTierRecipes.registerBackpackTierRecipes(); //register the recipes to upgrade a backpack to the next tier
//		registerBackpackUpgradeRemovalRecipes(); //register the recipes to remove upgrades from backpacks
//      registerBackpackUpgradeAdditionRecipes(); //register the recipes to add upgrades from backpacks


	}

    public static void setAllRecipes(){
        setItemBackpackRecipes();
        setUpgradeRecipes();
    }

	//=================================================================================Helper Registers==========================================================

    private static void setUpgradeRecipes(){

    }

    private static void setItemBackpackRecipes(){
        ItemRegistry.basicBackpack.setItemRecipe(ItemBackpackRecipes.basicBackpackRecipe);
    }


    private static void registerBackpackUpgradeRemovalRecipes(){
        //IBAPI.getItem doesn't work without sub items, so no backpack, hence the breakage on these 2 methods
        for (int i = 0; i < ItemBackpackRegistry.getSize(); i++){
            IBackpack backpack = ItemBackpackRegistry.getBackpackAtIndex(i);
            if (backpack instanceof IUpgradableBackpack) {
                GameRegistry.addRecipe(new BackpackRemoveUpgradeRecipe(new ItemStack(IronBackpacksAPI.getItem(IronBackpacksAPI.ITEM_BACKPACK_BASE+backpack.getName(null))), new ItemStack(IronBackpacksAPI.getItem(IronBackpacksAPI.ITEM_BACKPACK_BASE+backpack.getName(null))))); //TODO, second one is unnecessary?
            }
        }
    }

    private static void registerBackpackUpgradeAdditionRecipes() {
        ArrayList<ItemStack> upgrades = new ArrayList<>();
        for (int i = 0; i < ItemUpgradeRegistry.getTotalSize(); i++)
            upgrades.add(new ItemStack(ItemRegistry.upgradeItem, 1, i));

        for (int i = 0; i < ItemBackpackRegistry.getSize(); i++){
            IBackpack backpack = ItemBackpackRegistry.getBackpackAtIndex(i);
            if (backpack instanceof IUpgradableBackpack) {
                for (ItemStack upgrade : upgrades){
                    GameRegistry.addRecipe(new BackpackAddUpgradeRecipe(new ItemStack(IronBackpacksAPI.getItem(IronBackpacksAPI.ITEM_BACKPACK_BASE+backpack.getName(null))), upgrade, new ItemStack(IronBackpacksAPI.getItem(IronBackpacksAPI.ITEM_BACKPACK_BASE+backpack.getName(null)))));
                }
            }
        }
    }
}
