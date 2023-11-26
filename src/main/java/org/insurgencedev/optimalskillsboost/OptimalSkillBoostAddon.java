package org.insurgencedev.optimalskillsboost;

import org.bukkit.Bukkit;
import org.insurgencedev.insurgencesets.api.addon.ISetsAddon;
import org.insurgencedev.insurgencesets.api.addon.InsurgenceSetsAddon;

@ISetsAddon(name = "OptimalSkillsBoost", version = "1.0.0", author = "Insurgence Dev Team", description = "Boost the skill experience earned from OptimalSkills")
public class OptimalSkillBoostAddon extends InsurgenceSetsAddon {

    @Override
    public void onAddonStart() {
        if (isDependentEnabled()) {
            registerEvent(new SkillExperienceGainListener());
        }
    }

    private boolean isDependentEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("OptimalSkills");
    }
}
