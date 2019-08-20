package com.planetgallium.kitpvp.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import com.planetgallium.kitpvp.api.PlayerAbilityEvent;
import com.planetgallium.kitpvp.game.Arena;
import com.planetgallium.kitpvp.kit.Ability;
import com.planetgallium.kitpvp.util.Config;
import com.planetgallium.kitpvp.util.Resources;
import com.planetgallium.kitpvp.util.Sounds;
import com.planetgallium.kitpvp.util.Toolkit;
import com.planetgallium.kitpvp.util.XMaterial;

public class AbilityListener implements Listener {
	
	private Arena arena;
	private Resources resources;
	
	public AbilityListener(Arena arena, Resources resources) {
		this.arena = arena;
		this.resources = resources;
	}
	
	@EventHandler
	public void onAbility(PlayerAbilityEvent e) {
		
		Player p = e.getPlayer();
		String kit = arena.getKits().getKit(p.getName());
		Ability ability = e.getAbility();
		
		if (p.hasPermission("kp.ability." + kit.toLowerCase())) {
			
			// Message
			if (resources.getKits(kit).getBoolean("Ability.Message.Enabled")) {
				p.sendMessage(Config.tr(ability.getMessage()));
			}
			
			// Sound
			if (resources.getKits(kit).getBoolean("Ability.Sound.Enabled")) {
				p.playSound(p.getLocation(), Sounds.valueOf(ability.getSoundName()).bukkitSound(), 3, ability.getSoundPitch());
			}
			
			// Potions
			if (resources.getKits(kit).contains("Ability.Potions")) {
				for (PotionEffect potions : ability.getPotions()) {
					p.addPotionEffect(potions);
				}
			}
			
			// Commands
			if (resources.getKits(kit).getBoolean("Ability.Commands.Enabled")) {
				Toolkit.runCommands(resources.getKits(arena.getKits().getKit(p.getName())), "Ability", p);
			}
			
			// Item handling
			Toolkit.getMainHandItem(p).setAmount(Toolkit.getMainHandItem(p).getAmount() - 1);
			
		} else {
			
			p.sendMessage(Config.tr(resources.getMessages().getString("Messages.General.Permission")));
			
		}
		
	}
	
}
