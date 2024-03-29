package com.gamehoundsinteractive.ForumsSync.utils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.gamehoundsinteractive.ForumsMain;
import com.gamehoundsinteractive.ForumsSync.utils.configmanager.ConfigManager;

public class SQLManager
{
	public ConfigManager configman;
	private final ForumsMain pl;
	private final String host;
	private final String database;
	private final String username;
	private final String password;
	private final int port;
	private java.sql.Connection conn;

	public SQLManager(final ForumsMain pl, final SQLManager sQLMan) {
		this.pl = ForumsMain.getInstance();
		this.configman = pl.getConfigMan();
		this.host = configman.config.getString("sql.host");
		this.database = configman.config.getString("sql.database");
		this.username = configman.config.getString("sql.username");
		this.password = configman.config.getString("sql.password");
		this.port = configman.config.getInt("sql.port");
		try {
			this.connect();
		}
		catch (final ClassNotFoundException e2) {
			pl.getLogger().log(Level.SEVERE, "No MySQL driver found. Aborting!");
			Bukkit.getServer().getPluginManager().disablePlugin(pl);
		}
		catch (final SQLException e) {
			pl.getLogger().log(Level.SEVERE, "Put in the correct MySQL auth info.");
			e.printStackTrace();
		}
	}

	public ResultSet runSQL(final String sql, final boolean data) {
		try {
			this.connect();
			final java.sql.Statement statement = this.conn.createStatement();
			if (data) {
				statement.executeUpdate(sql);
				return null;
			}
			return statement.executeQuery(sql);
		}
		catch (final SQLException e) {
			this.pl.getLogger().log(Level.SEVERE, "MySQL error.");
			e.printStackTrace();
		}
		catch (final ClassNotFoundException e2) {
			this.pl.getLogger().log(Level.SEVERE, "No MySQL driver found. Aborting!");
			Bukkit.getServer().getPluginManager().disablePlugin(this.pl);
		}
		return null;
	}

	private void connect() throws SQLException, ClassNotFoundException {
		if (this.conn != null && !this.conn.isClosed()) {
			return;
		}
		synchronized (this) {
			if (this.conn != null && !this.conn.isClosed()) {
				// monitorexit(this)
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
		}
	}
}
