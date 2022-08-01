package me.ryanhamshire.GriefPrevention.tasks;

import me.ryanhamshire.GriefPrevention.claim.Claim;
import me.ryanhamshire.GriefPrevention.data.SiegeData;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class SiegeBossBarTask implements Runnable
{

    private final SiegeData siegeData;
    private final String title;

    private final BossBar bossBar;
    private int time = 0;

    public SiegeBossBarTask(SiegeData siegeData, String title)
    {
        this.siegeData = siegeData;
        this.title = title;

        this.bossBar = Bukkit.createBossBar(title, BarColor.YELLOW, BarStyle.SOLID);
        this.bossBar.setTitle(title.replace("{time}", time + " minutes"));
        this.bossBar.addPlayer(siegeData.attacker);
        this.bossBar.addPlayer(siegeData.defender);
    }

    @Override
    public void run()
    {
        time++;
        if (time <= 60)
            this.bossBar.setTitle(title.replace("{time}", time + " seconds"));
        else
            this.bossBar.setTitle(title.replace("{time}", TimeUnit.SECONDS.toMinutes(time) + " minutes"));
    }

    public BossBar getBossBar()
    {
        return bossBar;
    }
}
