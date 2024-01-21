package org.insurgencedev.optimalskillsboost;

import org.bukkit.Bukkit;
import org.insurgencedev.insurgencesets.api.addon.ISetsAddon;
import org.insurgencedev.insurgencesets.api.addon.InsurgenceSetsAddon;
import org.insurgencedev.optimalskillsboost.listeners.SkillExperienceGainListener;

@ISetsAddon(name = "OptimalSkillsBoost", version = "1.0.1", author = "Insurgence Dev Team", description = "Boost the skill experience earned from OptimalSkills")
public class OptimalSkillBoostAddon extends InsurgenceSetsAddon {

    @Override
    public void onAddonReloadablesStart() {
        if (Bukkit.getPluginManager().isPluginEnabled("OptimalSkills")) {
            registerEvent(new SkillExperienceGainListener());
        }
    }

}
