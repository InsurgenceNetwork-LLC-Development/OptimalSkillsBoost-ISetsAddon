package org.insurgencedev.optimalskillsboost;

import gg.optimalgames.skills.api.events.SkillExperienceGainEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.insurgencedev.insurgencesets.api.ISetsAPI;
import org.insurgencedev.insurgencesets.libs.fo.remain.nbt.NBTItem;
import org.insurgencedev.insurgencesets.models.armorset.ArmorSet;
import org.insurgencedev.insurgencesets.models.upgrade.Boost;
import org.insurgencedev.insurgencesets.models.upgrade.Upgrade;
import org.insurgencedev.insurgencesets.settings.ArmorSetData;
import org.insurgencedev.insurgencesets.settings.ISetsPlayerCache;

public class SkillExperienceGainListener implements Listener {

    @EventHandler
    public void onGain(SkillExperienceGainEvent event) {
        Player player = event.getSkillProfile().getPlayer();
        ISetsPlayerCache cache = ISetsPlayerCache.from(player);

        ItemStack[] armorContents = player.getInventory().getArmorContents();
        for (ItemStack item : armorContents) {
            if (item != null) {
                NBTItem nbtItem = new NBTItem(item);
                if (!nbtItem.hasTag("armorSet")) {
                    continue;
                }

                ArmorSet armorSet = ISetsAPI.getArmorSetManager().findArmorSet(nbtItem.getString("armorSet"));
                if (armorSet == null) {
                    continue;
                }

                String armorSetName = armorSet.getName();
                String itemType = item.getType().name().split("_")[1];
                ArmorSetData armorSetData = cache.getArmorSetData(armorSetName);
                if (armorSetData == null) {
                    continue;
                }

                Object levels = getLevelsFromType(itemType, armorSetData);
                if (levels instanceof Integer) {
                    Upgrade upgrade = armorSet.findPieceLevels(itemType, (Integer) levels);
                    if (upgrade == null) {
                        continue;
                    }

                    for (Boost boost : upgrade.getBoosts()) {
                        if ("MISC".equals(boost.getNamespace()) && boost.getType().equals("Skills")) {
                            double boostAmount = boost.getBOOST_SETTINGS().getDouble("Boost_Amount");
                            event.setExperience(calcAmountToGive(event.getExperience(), boost, boostAmount));
                        }
                    }
                }
            }
        }
    }

    private long calcAmountToGive(long amountFromEvent, Boost boost, double boostAmount) {
        if (boost.isPercent()) {
            return (long) (amountFromEvent * (1 + boostAmount / 100));
        } else {
            return (long) (amountFromEvent * (boostAmount < 1 ? 1 + boostAmount : boostAmount));
        }
    }

    private Object getLevelsFromType(String type, ArmorSetData armorSetData) {
        return switch (type) {
            case "HEAD", "HELMET" -> armorSetData.getHelmetLevels();
            case "CHESTPLATE" -> armorSetData.getChestplateLevels();
            case "LEGGINGS" -> armorSetData.getLeggingsLevels();
            case "BOOTS" -> armorSetData.getBootsLevels();
            default -> false;
        };
    }

}
