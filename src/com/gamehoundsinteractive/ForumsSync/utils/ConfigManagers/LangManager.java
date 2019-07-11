package com.gamehoundsinteractive.ForumsSync.utils.ConfigManagers;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

import com.gamehoundsinteractive.LicenseRedeem;



public class LangManager
{
	public FileConfiguration lang;

	public LangManager(final LicenseRedeem pl) {
		pl.getLogger().log(Level.INFO, "Loading configuration...");
		if (!new File(pl.getDataFolder(), "lang.yml").exists()) {
			pl.saveDefaultConfig();
		}
		this.lang = pl.getConfig();
	}
}