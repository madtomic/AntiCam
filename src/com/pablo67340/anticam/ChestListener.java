package com.pablo67340.anticam;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener implements Listener{
	final HashMap<Player, Block> block = new HashMap<Player, Block>();
	public static HashMap<Integer, String> line0 = new HashMap<Integer, String>();
	public static HashMap<Integer, String> line1 = new HashMap<Integer, String>();
	public static HashMap<Integer, String> line2 = new HashMap<Integer, String>();
	public static HashMap<Integer, String> line3 = new HashMap<Integer, String>();
	Anticam plugin;
	Block b = null;
	Block a = null;
	Block prevblock = null;
	public ChestListener(Anticam instance){
		plugin = instance;
	}

	@EventHandler
	@SuppressWarnings("deprecation")
	public void onClick(PlayerInteractEvent e)
	{
		final Player p = e.getPlayer();
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(e.getClickedBlock().getType().equals(Material.CHEST)  || e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST))
			{
				final Block tb = e.getPlayer().getTargetBlock(null, 6);
				prevblock = tb;
				if(tb.getType() != Material.CHEST && tb.getType() != Material.TRAPPED_CHEST)
				{
					for(int i=0;i<6;i++){
						Block tbat = p.getTargetBlock(null, i);
						final Material type = tbat.getType();
						if(tbat.getType() != Material.CHEST && tbat.getType() != Material.TRAPPED_CHEST  && tbat.getType() != Material.AIR)
						{
							final Location loc = tbat.getLocation();
							if(loc.getBlock().getType() == Material.WALL_SIGN)
							{
								Sign s = (Sign)loc.getBlock().getState();
								line0.put(1, s.getLine(0));
								line1.put(2, s.getLine(1));
								line2.put(3, s.getLine(2));
								line3.put(4, s.getLine(3));
							}
							for(BlockFace bf : BlockFace.values())
							{
								Block attached = e.getClickedBlock().getRelative(bf);
								if(attached.getType() == Material.WALL_SIGN){
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
									{
										@Override
										public void run()
										{
											loc.getBlock().setType(type);
											if(loc.getBlock().getType() == Material.WALL_SIGN)
											{
												Sign s = (Sign) loc.getBlock().getState();
												s.setLine(0, line0.get(1));
												s.setLine(1, line1.get(2));
												s.setLine(2, line2.get(3));
												s.setLine(3, line3.get(4));
												s.update();
											}
										}
									}, 2L);
								}
							}
							loc.getBlock().setType(Material.AIR);
						}
					}
				}

				Block newtarget = e.getPlayer().getTargetBlock(null, 6);
				if(newtarget.getType()!= e.getClickedBlock().getType())
				{
					e.setCancelled(true);
					p.closeInventory();
					if(Cooldowns.tryCooldown(p, "camcool", 4000)) 
					{
						System.out.print("[ANTICAM] " + p.getName() + " attempted to freecam.");
						p.sendMessage("§7[§cANTICAM§r§7]§7 Freecam activity §flogged§7!");
						for(Player op : Bukkit.getServer().getOnlinePlayers())
						{
							if(op.isOp())
							{
								op.sendMessage("§7[§cANTICAM§r§7]§f " + p.getName() + " §7attempted to freecam.");
							}
						}
					}
				}
			}

		}
	}
}
