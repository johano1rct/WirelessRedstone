package net.licks92.WirelessRedstone.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * 
 * @author licks92
 * 
 * 
 * Allows to get the strings in order to use them in other classes.
 *
 */
@SerializableAs("WirelessStrings")
public class WirelessStrings implements ConfigurationSerializable
{	
	//Strings in alphabetical order.
	public String chatTag;
	public String backupDone;
	public String backupFailed;
	public String channelDoesNotExist;
	public String channelLocked;
	public String channelNameContainsInvalidCaracters;
	public String channelRemoved;
	public String channelRemovedCauseNoSign;
	public String channelUnlocked;
	public String commandDoesNotExist;
	public String commandForNextPage;
	public String customizedLanguageSuccessfullyLoaded;
	public String DBAboutToBeDeleted;
	public String DBDeleted;
	public String DBNotDeleted;
	public String forMoreInfosPerformWRInfo;
	public String listEmpty;
	public String newUpdateAvailable;
	public String ownersOfTheChannelAre;
	public String pageEmpty;
	public String pageNumberInferiorToZero;
	public String playerCannotCreateChannel;
	public String playerCannotCreateReceiverOnBlock;
	public String playerCannotCreateSign;
	public String playerCannotDestroyReceiverTorch;
	public String playerCannotDestroySign;
	public String playerCreatedChannel;
	public String playerDoesntHaveAccessToChannel;
	public String playerDoesntHavePermission;
	public String playerExtendedChannel;
	public String signDestroyed;
	public String subCommandDoesNotExist;
	public String thisChannelContains;
	public String tooFewArguments;
	
	/*
	 * These tags shouldn't be modified. Indeed, all the signs which all already created
	 * would not work.
	 */
	public List<String> tagsTransmitter = new ArrayList<String>();
	public List<String> tagsReceiver = new ArrayList<String>();
	public List<String> tagsScreen = new ArrayList<String>();
	//These tags corresponds to the receiver types.
	public List<String> tagsReceiverDefaultType = new ArrayList<String>();
	public List<String> tagsReceiverInverterType = new ArrayList<String>();
	public List<String> tagsReceiverDelayerType = new ArrayList<String>();
	
	public WirelessStrings(Map<String, Object> lang)
	{
		chatTag = "[" + lang.get("chatTag") + "] ";
		backupDone = ChatColor.GREEN + chatTag + lang.get("backupDone");
		backupFailed = ChatColor.RED + chatTag + lang.get("backupFailed");
		channelDoesNotExist = ChatColor.RED + chatTag + lang.get("channelDoesNotExist");
		channelLocked = ChatColor.GREEN + chatTag + lang.get("channelLocked");
		channelNameContainsInvalidCaracters = ChatColor.RED + chatTag + lang.get("channelNameContainsInvalidCaracters");
		channelRemoved = ChatColor.GREEN + chatTag + lang.get("channelRemoved");
		channelRemovedCauseNoSign = ChatColor.GREEN + chatTag + lang.get("channelRemovedCauseNoSign");
		channelUnlocked = ChatColor.GREEN + chatTag + lang.get("channelUnlocked");
		commandDoesNotExist = ChatColor.RED + chatTag + lang.get("commandDoesNotExist");
		commandForNextPage = ChatColor.GREEN + chatTag + lang.get("commandForNextPage");
		customizedLanguageSuccessfullyLoaded = ChatColor.GREEN + chatTag + lang.get("customizedLanguageSuccessfullyLoaded");
		DBAboutToBeDeleted = ChatColor.DARK_RED + chatTag + " /!\\ " + lang.get("DBAboutToBeDeleted");
		DBDeleted = ChatColor.GREEN + chatTag + lang.get("DBDeleted");
		DBNotDeleted = ChatColor.RED + chatTag + lang.get("DBNotDeleted");
		forMoreInfosPerformWRInfo = ChatColor.GREEN + chatTag + lang.get("forMoreInfosPerformWRInfo");
		listEmpty = ChatColor.RED + chatTag + lang.get("listEmpty");
		newUpdateAvailable = ChatColor.GREEN + chatTag + lang.get("newUpdateAvailable");
		ownersOfTheChannelAre = chatTag + lang.get("ownersOfTheChannelAre");
		pageEmpty = ChatColor.RED + chatTag + lang.get("pageEmpty");
		pageNumberInferiorToZero = ChatColor.RED + chatTag + lang.get("pageNumberInferiorToZero");
		playerCannotCreateChannel = ChatColor.RED + chatTag + lang.get("playerCannotCreateChannel");
		playerCannotCreateReceiverOnBlock = ChatColor.RED + chatTag + lang.get("playerCannotCreateReceiverOnBlock");
		playerCannotCreateSign = ChatColor.RED + chatTag + lang.get("playerCannotCreateSign");
		playerCannotDestroyReceiverTorch = ChatColor.RED + chatTag + lang.get("playerCannotDestroyReceiverTorch");
		playerCannotDestroySign = ChatColor.RED + chatTag + lang.get("playerCannotDestroySign");
		playerCreatedChannel = ChatColor.GREEN + chatTag + lang.get("playerCreatedChannel");
		playerDoesntHaveAccessToChannel = ChatColor.RED + chatTag + lang.get("playerDoesntHaveAccessToChannel");
		playerDoesntHavePermission = ChatColor.RED + chatTag + lang.get("playerDoesntHavePermission");
		playerExtendedChannel = ChatColor.GREEN + chatTag + lang.get("playerExtendedChannel");
		signDestroyed = ChatColor.GREEN + chatTag + lang.get("signDestroyed");
		subCommandDoesNotExist = ChatColor.RED + chatTag + lang.get("subCommandDoesNotExist");
		thisChannelContains = chatTag + lang.get("thisChannelContains");
		tooFewArguments = ChatColor.RED + chatTag + lang.get("thisChannelContains");
		
		//The signtags must be always the same
		tagsTransmitter.add("[transmitter]");
		tagsTransmitter.add("[wrt]");
		tagsReceiver.add("[receiver]");
		tagsReceiver.add("[wrr]");
		tagsScreen.add("[screen]");
		tagsScreen.add("[wrs]");
		tagsReceiverDefaultType.add("[default]");
		tagsReceiverDefaultType.add("[normal]");
		tagsReceiverInverterType.add("[inverter]");
		tagsReceiverInverterType.add("[inv]");
		tagsReceiverDelayerType.add("[delayer]");
		tagsReceiverDelayerType.add("[delay]");
	}
	
	@Override
	public Map<String, Object> serialize()
	{
		return null;
	}
}
