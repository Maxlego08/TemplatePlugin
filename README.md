# Template Plugin

Here is a simple project for the quick creation of minecraft plugin.
Works from version 1.7.10 to version 1.14.2

## Features

* Commands
* Inventories
* Json file
* Useful function (in the class ZUtils)
* ItemBuilder
* CooldownBuilder
* TimerBuilder
* Pagination
* Inventory button
* Custom Event

## Commande example:
Add a command<br>
You will create a command with the addCommand (command, class extant VCommand), this will save your command and add its executor in the class CommandManager <br>
To add a command with an argument you must pass in setting the parent class
```java
addCommand("test", new CommandTest());
register("test", new CommandTest());
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
* CommanndTestSub
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

* ZCommand
```java
addCommand("test", new ZCommand()
	.createInventory(1, 0)
	.setDescription("open inventory")
	.setSyntaxe("/test")
	.setConsoleCanUse(false)
);
```

## Inventories
You can create inventories with the same principle as for commands.<br>
So, you will create your class that will be extended from VInventory and then add it to the InventoryManager class with a unique ID

```java
addInventory(1, new InventoryExample());
```
* InventoryExample <br>
So you have the three most common vents for menu use that will be called by the class

```java
public class InventoryExample extends VInventory {
	
	@Override
	public boolean openInventory(Template main, Player player, int page, Object... args) throws Exception {

		createInventory("§aVote", 27);
		
		return true;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, Template plugin, Player player) { }

	@Override
	protected void onDrag(InventoryDragEvent event, Template plugin, Player player) { }


}
```
* InventoryPagination <br>
You have a paging system, you can use it just like this:

```java
public class InventoryExample extends VInventory {

	@Override
	public boolean openInventory(SphaleriaFaction main, Player player, int page, Object... args) throws Exception {

		createInventory("§bInventoryName §7" + page + "§8/§7" + getMaxPage(your list));

		AtomicInteger slot = new AtomicInteger();

		Pagination<?> pagination = new Pagination<>();
		pagination.paginate(your list, 45, page).forEach(war -> {
			//ToDo
		});

		if (getPage() != 1)
			addItem(48, new ItemButton(ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Previous"))
					.setClick(event -> createInventory(1, player, getPage() - 1, args)));
		if (getPage() != getMaxPage(your list))
			addItem(50, new ItemButton(ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Next"))
					.setClick(event -> createInventory(1, player, getPage() + 1, args)));

		return true;
	}

	protected int getMaxPage(Collection<?> items) {
		return (items.size() / 45) + 1;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, SphaleriaFaction plugin, Player player) { }

	@Override
	protected void onDrag(InventoryDragEvent event, SphaleriaFaction plugin, Player player) { }

}
```

* Button <br>
You can simply create buttons for you inventory
```java
Button example = new Button("§eExample", 360);
Button example2 = new Button("§eExample 2", 360, Arrays.asList("line 1", "line 2", "line 3"));
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
