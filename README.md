# Template Plugin

Here is a simple project for the quick creation of minecraft plugin.
Works from version 1.7.10 to version 1.15.2

## Features

* Commands
* TabCompleter
* Inventories
* Json file
* Useful function (in the class ZUtils)
* ItemBuilder
* CooldownBuilder
* TimerBuilder
* Pagination
* Inventory button
* Custom Event
* YML Loader (itemstack and button)
* Scoreboard (https://github.com/MrMicky-FR/FastBoard)

## Command example:
Add a command<br>
You will create a command with the addCommand (command, class extant VCommand), this will save your command and add its executor in the class CommandManager <br>
To add a command with an argument you must pass in setting the parent class
```java
addCommand("test", new CommandTest());
register("test", new CommandTest());
```
You can directly add command from the main class:
```java
this.registerCommand("command", new CommandTest(), "myaliaisase");
```

* CommandTest
```java
public class CommandTest extends VCommand {

	public CommandTest() {
		this.addSubCommand(new CommanndTestSub());
	}
	
	@Override
	public CommandType perform(Template main) {
		
		ModuleTest.getInstance().test(sender);
		
		return CommandType.SUCCESS;
	}
}
```
* CommandTestSub
```java
public class CommanndTestSub extends VCommand {

	public CommanndTestSub() {
		this.addSubCommand("sub");
		this.addRequireArg("location");
	}

	@Override
	public CommandType perform(Template main) {

		Location location = argAsLocation(0);
		player.teleport(location);
		
		return CommandType.SUCCESS;
	}

}
```
* Add custom tab
```java
public class CommandTest extends VCommand {

	public CommandTest() {
		this.addSubCommand(new CommanndTestSub());
		this.setTabCompletor();
	}
	
	@Override
	public CommandType perform(Template main) {
		
		ModuleTest.getInstance().test(sender);
		
		return CommandType.SUCCESS;
	}
	
	@Override
	public List<String> toTab(Template plugin, CommandSender sender, String[] args) {

		if (args.length == 3) {

			String startWith = args[2];

			List<String> entities = new ArrayList<String>();
			for (EntityType type : EntityType.values()) {
				if (type.isAlive() && !type.equals(EntityType.PLAYER)) {
					if (startWith.length() == 0 || type.name().toLowerCase().startsWith(startWith))
						entities.add(name(type.name()));
				}
			}

			return entities;

		}

		return null;
	}
}
```

## Inventories
You can create inventories with the same principle as for commands.<br>
So, you will create your class that will be extended from VInventory and then add it to the InventoryManager class with a unique ID

```java
addInventory(Inventory.INVENTORY_TEST, new InventoryExample());
```
* InventoryExample <br>
So you have the three most common vents for menu use that will be called by the class

```java
public class InventoryExample extends VInventory {

	@Override
	public InventoryResult openInventory(Template main, Player player, int page, Object... args)
			throws InventoryOpenException {
		
		createInventory("myInventory", 54);
		
		ItemBuilder builder = new ItemBuilder(Material.DIAMOND);
		addItem(35, builder).setLeftClick(event -> {
			//When a player clicks left
		}).setRightClick(event -> {
			//When a player right clicks
		}).setMiddleClick(event -> {
			//When a player middle clicks
		}).setClick(event -> {
			//When the player clicks, whether it's left, right or middle
		});
		
		return InventoryResult.SUCCESS;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, Template plugin, Player player) {

	}

	@Override
	protected void onDrag(InventoryDragEvent event, Template plugin, Player player) {

	}

}
```
* InventoryPagination <br>
With this you will be able to create several pages based on a list, everything is managed automatically by the plugin.
In this example, there will be several pages depending on an itemstack
```java
public class InventoryExample extends PaginateInventory<ItemStack> {

	public InventoryExample() {
		super("My cystom name %p%/%mp%", InventorySize.FULL_INVENTORY);
	}

	@Override
	public ItemStack buildItem(ItemStack object) {
		return object;
	}

	@Override
	public void onClick(ItemStack object, ItemButton button) {
		message(player, "§eYou click on " + getItemName(object));
	}

	@Override
	public List<ItemStack> preOpenInventory() {
		//You must put your list here
		return Arrays.asList(new ItemStack(Material.DIAMOND), new ItemStack(Material.EMERALD));
	}

	@Override
	public void postOpenInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onClose(InventoryCloseEvent event, Template plugin, Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDrag(InventoryDragEvent event, Template plugin, Player player) {
		// TODO Auto-generated method stub
		
	}
	
	//To avoid problems directly make your own clone of the class
	@Override
	protected InventoryExample clone() {
		return new InventoryExample();
	}

}
```

Here is an example of use in the <a href="https://www.spigotmc.org/resources/69465/">zSpawner</a> plugin.
The inventory will create several pages based on a spawner list. 
```java
public class InventorySpawnerPaginate extends PaginateInventory<Spawner> {

	private PlayerSpawner playerSpawner;

	public InventorySpawnerPaginate() {
		super(Config.inventoryName, Config.inventorySize);
	}

	@Override
	public ItemStack buildItem(Spawner object) {
		return object.getItemStack();
	}

	@Override
	public void onClick(Spawner object, ItemButton button) {

		if (object.isPlace()) {

			object.delete(manager.getBoard());
			message(player, Message.REMOVE_SPAWNER);
			createInventory(player, Inventory.INVENTORY_SPAWNER_PAGINATE, getPage(), playerSpawner);

			return;
		}

		SpawnerPrePlaceEvent event = new SpawnerPrePlaceEvent(player, object, playerSpawner);
		event.callEvent();

		if (event.isCancelled())
			return;

		playerSpawner.setCurrentPlacingSpawner(object);
		player.closeInventory();
		message(player, Message.PLACE_SPAWNER_START);

	}

	@Override
	public List<Spawner> preOpenInventory() {
		//The list is retrieved according to an argument sent during the opening of the inventory
		playerSpawner = (PlayerSpawner) args[0];
		return playerSpawner.getShortSpawners();
	}

	@Override
	public void postOpenInventory() {

		if (Config.displayInformation) {
			Button button = Config.buttonInformation;
			int slot1 = button.getSlot() > inventorySize ? infoSlot : button.getSlot();
			addItem(slot1, button.toItemStack(playerSpawner)).setClick(event -> {
				playerSpawner.toggleShort();
				createInventory(player, Inventory.INVENTORY_SPAWNER_PAGINATE, getPage(), playerSpawner);
			});
		}
		
		if (Config.displayRemoveAllButton) {
			Button button = Config.buttonRemoveAll;
			int slot1 = button.getSlot() > inventorySize ? removeAllSlot : button.getSlot();
			addItem(slot1, button.toItemStack(playerSpawner)).setClick(event -> {
				playerSpawner.deleteAllSpawners(manager.getBoard());
				createInventory(player, Inventory.INVENTORY_SPAWNER_PAGINATE, getPage(), playerSpawner);
			});
		}

	}

	@Override
	protected void onClose(InventoryCloseEvent event, ZSpawnerPlugin plugin, Player player) {

	}

	@Override
	protected void onDrag(InventoryDragEvent event, ZSpawnerPlugin plugin, Player player) {

	}

	@Override
	protected InventorySpawnerPaginate clone() {
		return new InventorySpawnerPaginate();
	}

}
```

You can directly add inventory from the main class:
```java
this.registerInventory(Inventory.INVENTORY_TEST, new InventoryExample());
```

## Json Saver

You will be able to create class to save anything in json very simply
```java
public class Config implements Saveable {

	public static String version = "0.0.0.1";
	
	@Override
	public void save(Persist persist) {
		persist.save(this, "config");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, Config.class, "config");
	}
}
```
You must then add the class like this in the main class
```java
addSave(new ConfigExample());
```

## Pagination

You can easily make list or map pagination

* Create a pagination
```java
Pagination<T> pagination = new Pagination<T>();
```

* Simple pagination
```java
List<T> list = pagination.paginate(List<T> list, int size, int page)
List<T> list = pagination.paginate(Map<?, T> map, int size, int page)
```

* Reverse pagination
```java
List<T> list = pagination.paginateReverse(List<T> list, int size, int page)
List<T> list = pagination.paginateReverse(Map<?, T> map, int size, int page)
```

## YML Loader

* ItemStack
You will be able to recover and save an itemstack according to a YamlConfiguration and a path
```java
Loader<ItemStack> loader = new ItemStackYAMLoader();
ItemStack item = loader.load(configuration, path);
loader.save(item, configuration, path);
```

## Scoreboard (https://github.com/MrMicky-FR/FastBoard)
You will be able to manage scoreboards very simply with the ScoreBoardManager class. It is initialized directly in the main class but you will have to make additions to make the scoreboard work

* Update lines

You will be able to add a consumer which will update the lines according to a Player, you can also activate a task to manage the automatic update
```java
scoreboardManager.setLines(player -> {
	List<String> lines = new ArrayList<>();
	
	lines.add(""); // Empty line
	lines.add("Hey " + player.getName());
	lines.add(""); // Empty line
	lines.add("Online: " + getServer().getOnlinePlayers().size());
	
	return lines;
});
```
To start the task you have two choices, either the `` scoreboardManager.schedule (); `` or the `` scoreboardManager.setLines (player -> {return lines};``

* Update title

```java
scoreboardManager.updateTitle(player, title);
```

* Update line

You will be able to modify just one line according to its index
```java
scoreboardManager.updateLine(player, line, string);
```

* Delete scoreboard

```java
scoreboardManager.delete(player);
```
