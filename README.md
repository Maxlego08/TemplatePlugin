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
* Pagination
* Inventory button

## Commande example:
Add a command<br>
You will create a command with the addCommand (command, class extant VCommand), this will save your command and add its executor in the class CommandManager <br>
To add a command with an argument you must pass in setting the parent class
```java
VCommand command = addCommand("example", new CommandExample());
addCommand(new CommandExampleSub(command));
```
Or you can create a command directly like this:
```java
addCommand("sphaleria", new VCommand() {   
  @Override
  protected CommandType perform(Sphaleria main, CommandSender sender, String... args) {
    sendMessage(main.getPrefix() + " §eVersion§7: §c" + main.getDescription().getFullName());
    sendMessage(main.getPrefix() + " §eCréer par Maxlego08 pour §6sphaleria §e!");
    return CommandType.SUCCESS;
  }
  @Override
  public String getSyntax() {
     return null;
  }
  @Override
  public String getPermission() {
    return null;
  }
}.addSubCommand("sphaleria").setOneClass(true));
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

* ZCommand
```java
addCommand("test", new ZCommand()
                .setCommand(command -> command.sendMessage("Hello world !"))
                .setPermission("admin.command")
                .setDescription("Example command !")
                .setSyntaxe("/test")
                .addSubCommand("test", "test2", "test3")
                .setOneClass(true)
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
* InventoryPagination <br>
You have a paging system, you can use it just like this:

```java
public class InventoryPagination extends VInventory {

	@Override
	protected boolean openMenu(Template plguin, Player player, int page, Object... args) {
		Inventory inventory = createInventory("§ePagination " + page +" / " + getMaxPage(plguin));
		AtomicInteger slot = new AtomicInteger(0);
		Pagination<PaginationExample> pagination = new Pagination<>();
		pagination.paginate(plguin.getExamples(), 45, page).forEach(p -> {
			inventory.setItem(slot.getAndIncrement(), ItemBuilder.getCreatedItem(Material.STONE, 1, "Test " + p.getId()));
		});
		if (page < getMaxPage(plguin))
			inventory.setItem(50, plguin.getNext().getInitButton());
		if (page != 1 && page <= getMaxPage(plguin))
			inventory.setItem(48, plguin.getPrevious().getInitButton());
		openInventory(inventory);
		return true;
	}

	public int getMaxPage(Template plguin){
		return (plguin.getExamples().size() / 45) + 1;
	}
	
	@Override
	protected void onClick(InventoryClickEvent event, Template plguin, Player player) {
		if (getItem().isSimilar(plguin.getNext().getInitButton()) && getSlot() == 50) {
			int newPage = getPage() + 1;
			plguin.getInventoryManager().createMenu(2, player, true, newPage);
		} else if (getItem().isSimilar(plguin.getPrevious().getInitButton()) && getSlot() == 48) {
			int newPage = getPage() - 1;
			plguin.getInventoryManager().createMenu(2, player, true, newPage);
		}
	}

	@Override
	protected void onClose(InventoryCloseEvent event, Template plugin, Player player) {
		
	}

	@Override
	protected void onDrag(InventoryDragEvent event, Template plugin, Player player) {

	}

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
