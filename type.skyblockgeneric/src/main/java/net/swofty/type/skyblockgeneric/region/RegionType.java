package net.swofty.type.skyblockgeneric.region;

import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.swofty.commons.Songs;
import net.swofty.type.skyblockgeneric.region.mining.configurations.*;
import net.swofty.type.skyblockgeneric.region.mining.configurations.deepmines.*;
import net.swofty.type.skyblockgeneric.region.mining.configurations.thepark.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Getter
public enum RegionType {
	PRIVATE_ISLAND("Your Island", NamedTextColor.GREEN),

	VILLAGE("Village", WheatAndFlowersConfiguration.class),
	BANK("Bank", NamedTextColor.GOLD),
	LIBRARY("Library", NamedTextColor.DARK_GREEN),
	AUCTION_HOUSE("Auction House", NamedTextColor.GOLD),
	SHENS_AUCTION("Shen's Auction", NamedTextColor.GOLD),
	FLOWER_HOUSE("Flower House"),
	BAZAAR_ALLEY("Bazaar Alley", NamedTextColor.YELLOW),
	COMMUNITY_CENTER("Community Center"),
	WIZARD_TOWER("Wizard Tower", NamedTextColor.LIGHT_PURPLE),
	BUILDERS_HOUSE("Builder's House"),
	THAUMATURGIST("Thaumaturgist", NamedTextColor.GOLD),
	TRADE_CENTER("Trade Center"),
	ELECTION_ROOM("Election Room"),
	MOUNTAIN("Mountain"),
	WILDERNESS("Wilderness", NamedTextColor.DARK_GREEN, Songs.ABSTRACT_RINGING),
	PLAYER_MUSEUM("%s's Museum", NamedTextColor.DARK_AQUA),
	RUINS("Ruins"),
	RABBIT_HOUSE("Rabbit House", NamedTextColor.YELLOW),
	HEXATORUM("Hexatorum", NamedTextColor.LIGHT_PURPLE),
	UNINCORPORATED("Unincorporated", NamedTextColor.RED),
	COLOSSEUM("Colosseum"),
	COMBAT_SETTLEMENT("Combat Settlement", NamedTextColor.RED),
	MINING_DISTRICT("Mining District", NamedTextColor.GOLD),
	GRAVEYARD("Graveyard", NamedTextColor.RED),
	CRYPTS("Crypts", NamedTextColor.RED),
	COAL_MINE("Coal Mine", NamedTextColor.DARK_GRAY, MineCoalConfiguration.class),
	HIGH_LEVEL("High Level", NamedTextColor.DARK_RED),
	ARCHERY_RANGE("Archery Range", NamedTextColor.DARK_GREEN),
	BLACKSMITH("Blacksmith"),
	FARM("Farm", NamedTextColor.YELLOW, MineWheatConfiguration.class),
	DARK_AUCTION("Dark Auction", NamedTextColor.DARK_PURPLE),
	FISHING_OUTPOST("Fishing Outpost"),
	FISHERMANS_HUT("Fisherman's Hut"),
	ARTISTS_ABODE("Artist's Abode", NamedTextColor.YELLOW),
	FOREST("Forest", NamedTextColor.DARK_GREEN, MineLogsConfiguration.class),
	FORAGING_CAMP("Foraging Camp", NamedTextColor.DARK_GREEN, MineLogsConfiguration.class), // TODO: you can't break everything here though

	BIRCH_PARK("Birch Park", NamedTextColor.GREEN, BirchParkConfiguration.class),
	HOWLING_CAVE("Howling Cave"),
	SPRUCE_WOODS("Spruce Woods", NamedTextColor.GREEN, SpruceWoodsConfiguration.class),
	VIKING_LONGHOUSE("Viking Longhouse", NamedTextColor.AQUA, SpruceWoodsConfiguration.class),
	DARK_THICKET("Dark Thicket", NamedTextColor.GREEN, DarkOakConfiguration.class),
	TRIALS_OF_FIRE("Trials of Fire", NamedTextColor.RED),
	SAVANNA_WOODLAND("Savanna Woodland", NamedTextColor.GREEN, SavannaWoodlandConfiguration.class),
	MELODY_PLATEAU("Melody's Plateau", NamedTextColor.LIGHT_PURPLE, SavannaWoodlandConfiguration.class),
	JUNGLE_ISLAND("Jungle Island", NamedTextColor.GREEN, JungleIslandConfiguration.class),

	JERRYS_WORKSHOP("Jerry's Workshop", NamedTextColor.RED),
	JERRY_POND("Jerry Pond", NamedTextColor.AQUA),
	SUNKEN_JERRY_POND("Sunken Jerry Pond", NamedTextColor.AQUA),
	TERRYS_SHACK("Terry's Shack", NamedTextColor.AQUA),
	MOUNT_JERRY("Mount Jerry", NamedTextColor.RED),
	HOT_SPRINGS("Hot Springs", NamedTextColor.DARK_RED),
	GLACIAL_CAVE("Glacial Cave", NamedTextColor.DARK_AQUA, GlacialCaveConfiguration.class),
	GARYS_SHACK("Gary's Shack", NamedTextColor.AQUA),
	SHERRYS_SHOWROOM("Sherry's Showroom", NamedTextColor.YELLOW),
	EINARYS_EMPORIUM("Einary's Emporium", NamedTextColor.GOLD),

	THE_BARN("The Barn", NamedTextColor.AQUA, BarnConfiguration.class),
	MUSHROOM_DESERT("Mushroom Desert"),
	DESERT_SETTLEMENT("Desert Settlement", NamedTextColor.YELLOW),
	OASIS("Oasis"),
	SHEPHERD_KEEP("Shepherd's Keep"),
	TRAPPERS_DEN("Trapper's Den"),
	JAKE_HOUSE("Jake's House"),
	MUSHROOM_GORGE("Mushroom Gorge"),
	OVERGROWN_MUSHROOM_CAVE("Overgrown Mushroom Cave", NamedTextColor.DARK_GREEN),
	GLOWING_MUSHROOM_CAVE("Glowing Mushroom Cave", NamedTextColor.DARK_AQUA),

	SPIDERS_DEN("Spider's Den", NamedTextColor.DARK_RED),
	SPIDERS_DEN_HIVE("Spider's Den", NamedTextColor.DARK_RED),
	BLAZING_FORTRESS("Blazing Fortress", NamedTextColor.DARK_RED),
	THE_END("The End", NamedTextColor.LIGHT_PURPLE),
	THE_END_NEST("The End", NamedTextColor.LIGHT_PURPLE),
	ARCHAEOLOGICAL_SITE("Archaeological Site", NamedTextColor.GREEN),
	BURNING_BRIDGE("Burning Bridge", NamedTextColor.DARK_RED),
	VOID_SEPULTURE("Void Sepulture", NamedTextColor.LIGHT_PURPLE),
	DRAGONS_NEST("Dragon's Nest", NamedTextColor.DARK_PURPLE),

	GOLD_MINE("Gold Mine", NamedTextColor.GOLD, GoldMineConfiguration.class),
	DEEP_CAVERNS("Deep Caverns", NamedTextColor.AQUA, GunpowderMinesConfiguration.class, null, Songs.AMBIENT_CAVES),
	GUNPOWDER_MINES("Gunpowder Mines", NamedTextColor.AQUA, GunpowderMinesConfiguration.class, null, Songs.AMBIENT_CAVES),
	LAPIS_QUARRY("Lapis Quarry", LapisQuarryConfiguration.class),
	PIGMENS_DEN("Pigmen's Den", PigmensDenConfiguration.class),
	SLIMEHILL("Slimehill", SlimehillConfiguration.class),
	DIAMOND_RESERVE("Diamond Reserve", DiamondReserveConfiguration.class),
	OBSIDIAN_SANCTUARY("Obsidian Sanctuary", ObsidianSanctuaryConfiguration.class),

	// Galatea
	TANGLEBURGS_PATH("Tangleburg's Path", GalateaForagingConfiguration.class),
	TANGLEBURG("Tangleburg", GalateaForagingConfiguration.class),
	TANGLEBURG_BANK("Tangleburg Bank", NamedTextColor.GOLD),
	EVERGREEN_PLATEAU("Evergreen Plateau", NamedTextColor.GREEN, GalateaForagingConfiguration.class),
	SOUTH_REACHES("South Reaches", NamedTextColor.GREEN, GalateaForagingConfiguration.class),
	MOONGLADES_EDGE("Moonglade's Edge", NamedTextColor.DARK_GREEN, GalateaForagingConfiguration.class),
	MOONGLADE_MARSH("Moonglade Marsh", NamedTextColor.DARK_GREEN, GalateaForagingConfiguration.class),
	MURKWATER_LOCH("Murkwater Loch", NamedTextColor.DARK_GREEN, GalateaForagingConfiguration.class),
	MURKWATER_SHALLOWS("Murkwater Shallows", NamedTextColor.DARK_AQUA, GalateaForagingConfiguration.class),
	NORTH_WETLANDS("North Wetlands", NamedTextColor.DARK_GREEN, GalateaForagingConfiguration.class),

	// Backwater Bayou
	BACKWATER_BAYOU("Backwater Bayou", NamedTextColor.DARK_GREEN),
	CRIMSON_ISLE("Crimson Isle", NamedTextColor.RED),
	BLAZING_VOLCANO("Blazing Volcano", NamedTextColor.DARK_RED),
	DOJO("Dojo", NamedTextColor.GOLD),
	MYSTIC_MARSH("Mystic Marsh", NamedTextColor.DARK_GREEN),
	SCARLETON("Scarleton", NamedTextColor.RED),
	BURNING_DESERT("Burning Desert", NamedTextColor.GOLD),
	DRAGONTAIL("Dragontail", NamedTextColor.DARK_RED),
	STRONGHOLD("Stronghold", NamedTextColor.DARK_RED),

	DWARVEN_VILLAGE("Dwarven Village", DwarvenMinesConfiguration.class),
	DWARVEN_MINES("Dwarven Mines", NamedTextColor.DARK_GREEN, DwarvenMinesConfiguration.class),
	GOBLIN_BURROWS("Goblin Burrows", DwarvenMinesConfiguration.class),
	THE_MIST("The Mist", NamedTextColor.DARK_GRAY, DwarvenMinesConfiguration.class),
	GREAT_ICE_WALL("Great Ice Wall", DwarvenMinesConfiguration.class),
	GATES_TO_THE_MINES("Gates to the Mines", DwarvenMinesConfiguration.class),
	RAMPARTS_QUARRY("Rampart's Quarry", DwarvenMinesConfiguration.class),
	FORGE_BASIN("Forge Basin", DwarvenMinesConfiguration.class),
	THE_FORGE("The Forge", DwarvenMinesConfiguration.class),
	CLIFFSIDE_VEINS("Cliffside Veins", DwarvenMinesConfiguration.class),
	ROYAL_MINES("Royal Mines", DwarvenMinesConfiguration.class),
	UPPER_MINES("Upper Mines", DwarvenMinesConfiguration.class),
	LAVA_SPRINGS("Lava Springs", DwarvenMinesConfiguration.class),
	DIVANS_GATEWAY("Divan's Gateway", DwarvenMinesConfiguration.class),
	FAR_REACH("Far Reserve", DwarvenMinesConfiguration.class),
	PALACE_BRIDGE("Palace Bridge", DwarvenMinesConfiguration.class),
	ROYAL_PALACE("Royal Palace", DwarvenMinesConfiguration.class),
	ARISTOCRAT_PASSAGE("Aristocrat's Passage", DwarvenMinesConfiguration.class),
	FAR_RESERVE("Far Reserve", DwarvenMinesConfiguration.class);

	private final String name;
	private final TextColor color;
	private final SkyBlockRegenConfiguration miningHandler;
	private final SkyBlockBiomeConfiguration biomeHandler;
	private final List<Songs> songs;

	RegionType(String name, TextColor color, Class<? extends SkyBlockRegenConfiguration> miningHandler, Class<? extends SkyBlockBiomeConfiguration> biomeHandler, Songs... songs) {
		this.name = name;
		this.color = color;

		if (miningHandler != null) {
			try {
				this.miningHandler = miningHandler.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException |
			         NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		else
			this.miningHandler = null;

		if (biomeHandler != null) {
			try {
				this.biomeHandler = biomeHandler.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
			         IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		else
			this.biomeHandler = null;

		this.songs = new ArrayList<>(List.of(songs));
	}

	RegionType(String name, TextColor color, Class<? extends SkyBlockRegenConfiguration> miningHandler) {
		this(name, color, miningHandler, null, new Songs[0]);
	}

	RegionType(String name, Class<? extends SkyBlockRegenConfiguration> miningHandler) {
		this(name, NamedTextColor.AQUA, miningHandler);
	}


	RegionType(String name, Class<? extends SkyBlockRegenConfiguration> miningHandler, Class<? extends SkyBlockBiomeConfiguration> biomeHandler) {
		this(name, NamedTextColor.AQUA, miningHandler, biomeHandler);
	}

	RegionType(String name, TextColor color) {
		this(name, color, new Songs[0]);
	}

	RegionType(String name, TextColor color, Songs... songs) {
		this.name = name;
		this.color = color;
		this.miningHandler = null;
		this.songs = new ArrayList<>(List.of(songs));
		this.biomeHandler = null;
	}

	RegionType(String name) {
		this(name, NamedTextColor.AQUA, new Songs[0]);
	}

	public static RegionType getByID(int id) {
		return RegionType.values()[id];
	}

	@SneakyThrows
	public SkyBlockRegenConfiguration getMiningHandler() {
		return miningHandler;
	}

	@SneakyThrows
	public SkyBlockBiomeConfiguration getBiomeHandler() {
		return biomeHandler;
	}

	@Override
	public String toString() {
		return name;
	}
}
