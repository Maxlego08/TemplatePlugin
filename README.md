# TempltePlugin

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

## Commande example:
Add a command<br>
You will create a command with the addCommand (command, class extant VCommand), this will save your command and add its executor in the class CommandManager <br>
To add a command with an argument you must pass in setting the parent class
```java
VCommand command = addCommand("example", new CommandExample());
addCommand(new CommandExampleSub(command));
```
* CommandExample
```java
public class CommandExample extends VCommand {

	public CommandExample() {
		super(null, true);
		addSubCommand("example");
	}
	
	@Override
	protected CommandType perform(Template main, CommandSender sender, String... args) {
		main.getInventoryManager().createMenu(1, getPlayer(), 1);
		sendMessage(ConfigExample.version);
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public String getSyntax() {
		return null;
	}

}
```
* CommandeExampleSub
```java
public class CommandExampleSub extends VCommand {

	public CommandExampleSub(VCommand parent) {
		super(parent);
		this.addSubCommand("test");
	}

	@Override
	protected CommandType perform(Template main, CommandSender sender, String... args) {

		sendMessage("Yessssssss everything works !");	
		
		return CommandType.SUCCESS;
	}

	@Override
	public String getPermission() {
		return "admin";
	}

	@Override
	public String getSyntax() {
		return "/example test";
	}
}
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
	protected boolean openMenu(Template plguin, Player player, int page) {

		Inventory inv = createInventory("§aExample", 9);
		
		openInventory(inv);
		
		return true;
	}

	@Override
	protected void onClick(InventoryClickEvent event, Template plguin, Player player) { }

	@Override
	protected void onClose(InventoryCloseEvent event, Template plguin, Player player) { }

	@Override
	protected void onDrag(InventoryDragEvent event, Template plugin, Player player) { }

}
```

## Json Saver

You will be able to create class to save anything in json very simply
```java
public class ConfigExample implements Saver {

	public static String version = "0.0.0.1";
	
	private static transient ConfigExample i = new ConfigExample();
	
	@Override
	public void save(Persist persist) {
		persist.save(i);
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, ConfigExample.class, getClass().getSimpleName().toLowerCase());
	}
}
```
You must then add the class like this in the main class
```java
addSave(new ConfigExample());
```