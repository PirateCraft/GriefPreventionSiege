package me.ryanhamshire.GriefPrevention.tasks;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.data.DataStore;
import me.ryanhamshire.GriefPrevention.data.SiegeData;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SiegeBossBarTask implements Runnable
{

    private final SiegeData siegeData;
    private final BossBar attackerBossBar;
    private final BossBar defenderBossBar;
    private final BossBar attenderBossBar;

    private final String attackerTitle;
    private final String defenderTitle;
    private final String attenderTitle;

    private final String attackerName;
    private final String defenderName;

    private int time = GriefPrevention.instance.config_piratecraft_siege_duration;

    public SiegeBossBarTask(@NotNull SiegeData siegeData, String attackerName, String defenderName)
    {
        this.siegeData = siegeData;
        this.attackerName = attackerName;
        this.defenderName = defenderName;

        attackerTitle = GriefPrevention.instance.config_piratecraft_siege_attacker_bossbar_title.replace("{defender}", defenderName);
        defenderTitle = GriefPrevention.instance.config_piratecraft_siege_defender_bossbar_title.replace("{attacker}", attackerName);
        attenderTitle = GriefPrevention.instance.config_piratecraft_siege_attender_bossbar_title.replace("{defender}", defenderName);


        this.attackerBossBar = Bukkit.createBossBar(attackerTitle, BarColor.YELLOW, BarStyle.SOLID);
        this.defenderBossBar = Bukkit.createBossBar(defenderTitle, BarColor.YELLOW, BarStyle.SOLID);
        this.attenderBossBar = Bukkit.createBossBar(attenderTitle, BarColor.YELLOW, BarStyle.SOLID);

        this.attackerBossBar.addPlayer(siegeData.attacker);
        this.defenderBossBar.addPlayer(siegeData.defender);

        siegeData.getAllPermittedPlayers().forEach(this.attenderBossBar::addPlayer);
    }

    @Override
    public void run()
    {
        time--;
        this.attackerBossBar.setTitle(attackerTitle.replace("{time}", TimeUnit.SECONDS.toMinutes(time) + " minutes"));
        this.defenderBossBar.setTitle(defenderTitle.replace("{time}", TimeUnit.SECONDS.toMinutes(time) + " minutes"));
        this.attenderBossBar.setTitle(attenderTitle.replace("{time}", TimeUnit.SECONDS.toMinutes(time) + " minutes"));
        double progress = 1 - ((double) ((time * 100) / GriefPrevention.instance.config_piratecraft_siege_duration) / 10);
        attackerBossBar.setProgress(progress);
        defenderBossBar.setProgress(progress);
        attackerBossBar.setProgress(progress);
        if (time == 0)
        {
            DataStore dataStore = GriefPrevention.instance.dataStore;
            dataStore.endSiege(siegeData, attackerName, defenderName, null, true);
        }
    }

    public void removeBossBars()
    {
        attackerBossBar.removeAll();
        defenderBossBar.removeAll();
        attenderBossBar.removeAll();
    }
}
