package me.yukisando.MagicArrow;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MagicArrowListener implements Listener, CommandExecutor {

    public MagicArrowMain plugin;

    public MagicArrowListener(MagicArrowMain plugin) {
        this.plugin = plugin;

    }



    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "In game only.");
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();

        if (plugin.enabledPlayers.contains(playerName)) {

            ItemStack bowItem = new ItemStack(Material.BOW);
            ItemStack arrowItem = new ItemStack(Material.ARROW);
            bowItem.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            player.getInventory().removeItem(bowItem);
            player.getInventory().removeItem(arrowItem);
            player.sendMessage(ChatColor.DARK_GREEN + "MagicArrow disabled!");
            plugin.enabledPlayers.remove(playerName);

        } else {

            plugin.enabledPlayers.add(playerName);
            player.sendMessage(ChatColor.GREEN + "MagicArrow enabled!");

            ItemStack bowItem = new ItemStack(Material.BOW);
            ItemStack arrowItem = new ItemStack(Material.ARROW);
            bowItem.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            player.getInventory().removeItem(bowItem);
            player.getInventory().removeItem(arrowItem);
            player.getInventory().addItem(bowItem);
            player.getInventory().addItem(arrowItem);

        }
        return true;
    }


    public Boolean Teleport;
    public Boolean RedArrow;
    public Boolean BlueArrow;
    public Boolean SandArrow;
    public Boolean DeathArrow;
    public Boolean GreenArrow;
    public Location ArrowLocPrev = null;
    public Player shooter;
    public Arrow arrow;
    public Material redStoneBlock;
    public Material gold;
    public Location PlayerLoc = null, ArrowLoc = null, blockUnderPlayer = null;
    public Vector vector = null;


    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {

        Entity entity = event.getEntity();

        if (entity instanceof Arrow) {

            arrow = (Arrow) entity;
            shooter = (Player) arrow.getShooter();
            redStoneBlock = Material.REDSTONE_BLOCK;
            gold = Material.GOLD_BLOCK;
            PlayerLoc = shooter.getLocation();
            ArrowLoc = arrow.getLocation();

            blockUnderPlayer = new Location(shooter.getWorld(),
                    shooter.getLocation().getX(),
                    shooter.getLocation().getY() - 1,
                    shooter.getLocation().getZ());

            RedArrow = (blockUnderPlayer.getBlock().getType() == redStoneBlock);
            SandArrow = (blockUnderPlayer.getBlock().getType() == Material.SANDSTONE);
            BlueArrow = (blockUnderPlayer.getBlock().getType() == Material.LAPIS_BLOCK);
            Teleport = (blockUnderPlayer.getBlock().getType() == gold);
            DeathArrow = (arrow.getFireTicks() > 0);

            if (plugin.enabledPlayers.contains(shooter.getName())) {

                double pitch = (((PlayerLoc.getPitch()) + 90) * Math.PI) / 180;
                double yaw = (((PlayerLoc.getYaw()) + 90) * Math.PI) / 180;

                double x = Math.sin(pitch) * Math.cos(yaw);
                double y = Math.sin(pitch) * Math.sin(yaw);
                double z = Math.cos(pitch);

                vector = new Vector(x, z, y);

                if (Teleport) {
                    shooter.teleport(ArrowLoc.setDirection(vector));
                }

                if (DeathArrow) {
                    shooter.setHealth(0.0);
                    ArrowLoc.getBlock().setType(Material.AIR);
                }

                if (RedArrow) {
                        if (!(ArrowLocPrev == null)) {
                            ArrowLocPrev.getBlock().setType(Material.AIR);
                        }
                        ArrowLoc.getBlock().setType(redStoneBlock);
                        ArrowLocPrev = ArrowLoc;
                }

                if (BlueArrow) {
                    ArrowLoc.getBlock().setType(Material.WATER);
                }
                if (SandArrow) {
                    ArrowLoc.getBlock().setType(Material.SAND);
                }
                if(GreenArrow){
                    //plugin.enabledPlayers.get(1).teleport(ArrowLoc);
                }
            }
        }
    }
}