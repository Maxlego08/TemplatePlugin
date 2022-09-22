package fr.maxlego08.timer;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bukkit.entity.Player;

import fr.maxlego08.timer.placeholder.LocalPlaceholder;
import fr.maxlego08.timer.zcore.utils.ZUtils;
import fr.maxlego08.timer.zcore.utils.builder.TimerBuilder;

public class TimerHelper extends ZUtils {

	public TimerHelper() {

		LocalPlaceholder localPlaceholder = LocalPlaceholder.getInstance();
		localPlaceholder.setPrefix("timer");

		localPlaceholder.register("week", (p, a) -> this.registerEveryMonday(p, a));
		localPlaceholder.register("daily", (p, a) -> this.registerEveryDay(p, a));
		localPlaceholder.register("monthly", (p, a) -> this.registerEveryMonthly(p, a));
		localPlaceholder.register("115", (p, a) -> this.registerEveryTwoWeeks(p, a));
	}

	private String registerEveryMonday(Player player, String args) {

		LocalDate now = LocalDate.now();
		LocalDate nextWeek = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

		LocalDateTime mondayMidnight = LocalDateTime.of(nextWeek, LocalTime.MIDNIGHT);

		Instant instant = mondayMidnight.atZone(ZoneId.systemDefault()).toInstant();
		Date date = Date.from(instant);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		long delay = calendar.getTimeInMillis() - System.currentTimeMillis();

		return TimerBuilder.getStringTime(delay / 1000);

	}
	
	private String registerEveryDay(Player player, String args) {
		
		LocalTime midnight = LocalTime.MIDNIGHT;
		LocalDate today = LocalDate.now(ZoneId.systemDefault());
		LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
		LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

		Instant instant = tomorrowMidnight.atZone(ZoneId.systemDefault()).toInstant();
		Date date = Date.from(instant);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
		
		return TimerBuilder.getStringTime(delay / 1000);
		
	}
	
	private String registerEveryMonthly(Player player, String args) {
		
		LocalTime midnight = LocalTime.MIDNIGHT;
		LocalDate today = LocalDate.now(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfNextMonth());
		LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
		
		Instant instant = todayMidnight.atZone(ZoneId.systemDefault()).toInstant();
		Date date = Date.from(instant);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
		
		return TimerBuilder.getStringTime(delay / 1000);
		
	}
	
	private String registerEveryTwoWeeks(Player player, String args) {
		
		Calendar calendar = Calendar.getInstance();
		
		if (calendar.get(Calendar.DAY_OF_MONTH) < 15) {
			
			Calendar middleOfMonth = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 15);
			long delay = middleOfMonth.getTimeInMillis() - System.currentTimeMillis();
			return TimerBuilder.getStringTime(delay / 1000);
			
		} else {
			
			return this.registerEveryMonthly(player, args);
			
		}
		
		
		
		
	}

}
