package fr.maxlego08.template.zcore.enums;

public enum Message {

	PREFIX("§7(§bTemplate§7)"),
	
	TELEPORT_MOVE("§cVous ne devez pas bouger !"),
	TELEPORT_MESSAGE("§7Téléportatio dans §3%s §7secondes !"),
	TELEPORT_ERROR("§cVous avez déjà une téléportation en cours !"),
	TELEPORT_SUCCESS("§7Téléportation effectué !"),
	
	INVENTORY_NULL("§cImpossible de trouver l'inventaire avec l'id §6%s§c."),
	INVENTORY_CLONE_NULL("§cLe clone de l'inventaire est null !"),
	INVENTORY_OPEN_ERROR("§cUne erreur est survenu avec l'ouverture de l'inventaire §6%s§c."),
	INVENTORY_BUTTON_PREVIOUS("§f» §7Page précédente"),
	INVENTORY_BUTTON_NEXT("§f» §7Page suivante"),
	
	TIME_DAY("%02d jour(s) %02d heure(s) %02d minute(s) %02d seconde(s)"),
	TIME_HOUR("%02d heure(s) %02d minute(s) %02d seconde(s)"),
	TIME_HOUR_SIMPLE("%02d:%02d:%02d"),
	TIME_MINUTE("%02d minute(s) %02d seconde(s)"),
	TIME_SECOND("%02d seconde(s)"),
	
	COMMAND_SYNTAXE_ERROR("§cVous devez exécuter la commande comme ceci§7: §a%s"),
	COMMAND_NO_PERMISSION("§cVous n'avez pas la permission d'exécuter cette commande."),
	COMMAND_NO_CONSOLE("§cSeul un joueur peut exécuter cette commande."),
	COMMAND_NO_ARG("§cImpossible de trouver la commande avec ses arguments."),
	COMMAND_SYNTAXE_HELP("§a%s §b» §7%s"),
	
	
	;

	private String message;

	private Message(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String toMsg() {
		return message;
	}

	public String msg() {
		return message;
	}

}

