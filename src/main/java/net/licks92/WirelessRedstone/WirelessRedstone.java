package net.licks92.WirelessRedstone;

import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import net.licks92.WirelessRedstone.Channel.IWirelessPoint;
import net.licks92.WirelessRedstone.Channel.WirelessChannel;
import net.licks92.WirelessRedstone.Configuration.WirelessConfiguration;
import net.licks92.WirelessRedstone.Configuration.WirelessStringProvider;
import net.licks92.WirelessRedstone.Listeners.WirelessBlockListener;
import net.licks92.WirelessRedstone.Listeners.WirelessPlayerListener;
import net.licks92.WirelessRedstone.Listeners.WirelessWorldListener;
import net.licks92.WirelessRedstone.Permissions.WirelessPermissions;
import net.licks92.WirelessRedstone.Utils.Metrics;
import net.licks92.WirelessRedstone.Utils.Metrics.Graph;
import net.licks92.WirelessRedstone.Utils.Metrics.Plotter;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

public class WirelessRedstone extends JavaPlugin
{
	public static WirelessConfiguration config;
	public static WirelessStringProvider strings;
	private static WRLogger logger;
	public WireBox WireBox = new WireBox(this);
	public WirelessPermissions permissions;
	public static Permission perms;
	public WirelessWorldListener worldlistener;
	public WirelessBlockListener blocklistener;
	public WirelessPlayerListener playerlistener;
	private static Metrics metrics;
	public double currentversion;
	public double newversion;
	
	public static WRLogger getWRLogger()
	{
		return logger;
	}
	
	@Override
	public void onDisable()
	{
		config.close();
	}
	
	@Override
	public void onEnable()
	{
		PluginDescriptionFile pdfFile = getDescription();
		
		config = new WirelessConfiguration(this);
		if(config.getDebugMode())
		{
			logger = new WRLogger("[WirelessRedstone]", true);
			logger.info("Debug Mode activated !");
			logger.info("Log level set to FINEST because of the debug mode");
			logger.setLogLevel(Level.FINEST);
		}
		else
		{
			logger = new WRLogger("[WirelessRedstone]", false);
			WirelessRedstone.logger.setLogLevel(config.getLogLevel());
		}
		config.init();
		
		WirelessRedstone.logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is loading...");
		
		currentversion = Double.valueOf(getDescription().getVersion().split("b")[0].replaceFirst("\\.", ""));
		
		/*
		 * Check for updates on the repository dev.bukkit
		 * 
		 * This code has been taken from the Vault plugin.
		 * 
		 * All credits to the developpers of Vault (http://dev.bukkit.org/server-mods/vault/);
		 */
		
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					newversion = updateCheck(currentversion);
					
					if(newversion > currentversion)
					{
						logger.info("A new update has been released ! You can download it at http://dev.bukkit.org/server-mods/wireless-redstone/");
					}
					else if(newversion < currentversion)
					{
						logger.info("You are using a version that is higher than the repository version! Did you download it on the github code repo?");
					}
					else //If it's the same version...
					{
						logger.debug("You are using the same version as the official repository.");
					}
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		}, 0, 24000);
		
		//Load config and strings
		strings = new WirelessStringProvider();
		
		//Load listeners
		worldlistener = new WirelessWorldListener(this);
		blocklistener = new WirelessBlockListener(this);
		playerlistener = new WirelessPlayerListener(this);
		
		WirelessRedstone.logger.fine("Loading Permissions...");
		
		permissions = new WirelessPermissions(this);
		config.save();

		this.WireBox.UpdateChacheNoThread();

		WirelessRedstone.logger.fine("Registering commands...");
		getCommand("wirelessredstone").setExecutor(new WirelessCommands(this));
		getCommand("wrhelp").setExecutor(new WirelessCommands(this));
		getCommand("wrr").setExecutor(new WirelessCommands(this));
		getCommand("wrt").setExecutor(new WirelessCommands(this));
		getCommand("wrs").setExecutor(new WirelessCommands(this));
		getCommand("wrremove").setExecutor(new WirelessCommands(this));
		getCommand("wra").setExecutor(new WirelessCommands(this));
		getCommand("wrlist").setExecutor(new WirelessCommands(this));
		getCommand("wri").setExecutor(new WirelessCommands(this));
		getCommand("wrlock").setExecutor(new WirelessCommands(this));

		WirelessRedstone.logger.fine("Loading Chunks");
		LoadChunks();
		
		//Metrics
		try
		{
			metrics = new Metrics(this);
			
			// Channel metrics
			final Graph channelGraph = metrics.createGraph("Channel metrics");
			channelGraph.addPlotter(new Plotter("Total channels")
			{
				@Override
				public int getValue()
				{
					return config.getAllChannels().size();
				}
			});
			channelGraph.addPlotter(new Plotter("Total signs")
			{
				@Override
				public int getValue()
				{
					return WireBox.getAllSigns().size();
				}
			});
			
			// Sign Metrics
			final Graph signGraph = metrics.createGraph("Sign metrics");
			signGraph.addPlotter(new Plotter("Transmitters")
			{
				@Override
				public int getValue()
				{
					int total = 0;
					for(WirelessChannel channel : config.getAllChannels())
					{
						total+=channel.getTransmitters().size();
					}
					return total;
				}
			});
			signGraph.addPlotter(new Plotter("Receivers")
			{
				@Override
				public int getValue()
				{
					int total = 0;
					for(WirelessChannel channel : config.getAllChannels())
					{
						total+=channel.getReceivers().size();
					}
					return total;
				}
			});
			signGraph.addPlotter(new Plotter("Screens")
			{
				@Override
				public int getValue()
				{
					int total = 0;
					for(WirelessChannel channel : config.getAllChannels())
					{
						total+=channel.getScreens().size();
					}
					return total;
				}
			});
			
			final Graph storageGraph = metrics.createGraph("Storage used");
			storageGraph.addPlotter(new Plotter("SQL")
			{
				@Override
				public int getValue()
				{
					if(config.getSQLUsage())
					{
						return 1;
					}
					else
					{
						return 0;
					}
				}
			});
			
			storageGraph.addPlotter(new Plotter("Yaml files")
			{
				@Override
				public int getValue()
				{
					if(!config.getSQLUsage())
					{
						return 1;
					}
					else
					{
						return 0;
					}
				}
			});
			metrics.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//Loading finished !
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}

	public void LoadChunks()
	{
		if (WirelessRedstone.config.isCancelChunkUnloads())
		{
			for (IWirelessPoint loc : this.WireBox.getAllSigns())
			{
				Location location = WireBox.getPointLocation(loc);
				if(location.getWorld() == null)
					continue; // world currently not loaded.
				
				Chunk center = location.getBlock().getChunk();
				World world = center.getWorld();
				int range = WirelessRedstone.config.getChunkUnloadRange();
				for (int dx = -(range); dx <= range; dx++) {
					for (int dz = -(range); dz <= range; dz++) {
						Chunk chunk = world.getChunkAt(center.getX() + dx,
								center.getZ() + dz);
						world.loadChunk(chunk);
					}
				}
			}
		}
	}
	
	public WirelessRedstone getPlugin()
	{
		return this;
	}
	
	public double updateCheck(double currentVersion) throws Exception {
        String pluginUrlString = "http://dev.bukkit.org/server-mods/wireless-redstone/files.rss";
        try {
            URL url = new URL(pluginUrlString);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("item");
            Node firstNode = nodes.item(0);
            if (firstNode.getNodeType() == 1) {
                Element firstElement = (Element)firstNode;
                NodeList firstElementTagName = firstElement.getElementsByTagName("title");
                Element firstNameElement = (Element) firstElementTagName.item(0);
                NodeList firstNodes = firstNameElement.getChildNodes();
                return Double.valueOf(firstNodes.item(0).getNodeValue().replace("Wireless Redstone ", "").split("b")[0].replaceFirst("\\.", "").trim());
            }
        }
        catch(SocketException ex)
        {
        	logger.warning("Could not get the informations about the latest version. Maybe your internet connection is broken?");
        }
        catch(UnknownHostException ex)
        {
        	logger.warning("Could not find the host dev.bukkit. Maybe your server can't connect to the internet.");
        }
        catch(SAXParseException ex)
        {
        	logger.warning("Could not connect to bukkitdev. It seems that something's blocking the plugin. Do you have an internet protection?");
        }
        return currentVersion;
    }
}
