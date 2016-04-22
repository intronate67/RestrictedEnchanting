/*This file is part of RestrictedEnchanting, licensed under the MIT License (MIT).
*
* Copyright (c) 2016 Hunter Sharpe
* Copyright (c) contributors

* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package net.huntersharpe.RestrictedEnchanting;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ChildRemove implements CommandExecutor{

    private RestrictedEnchanting plugin;

    public ChildRemove(RestrictedEnchanting plugin){
        this.plugin = plugin;
    }

    private RestrictionHandler handler = new RestrictionHandler(plugin);

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String item = args.<String>getOne("item").get();
        String enchant = args.<String>getOne("enchant").get();
        String[] enchants = {
                "AQUA_AFFINITY",
                "BANE_OF_ARTHROPODS",
                "BLAST_PROTECTION",
                "DEPTH_STRIDER",
                "EFFICIENCY",
                "FEATHER_FALLING",
                "FIRE_ASPECT",
                "FIRE_PROTECTION",
                "FLAME",
                "FORTUNE",
                "INFINITY",
                "KNOCKBACK",
                "LOOTING",
                "LUCK_OF_THE_SEA",
                "LURE",
                "POWER",
                "PROJECTILE_PROTECTION",
                "PROTECTION",
                "PUNCH",
                "RESPIRATION",
                "SHARPNESS",
                "SILK_TOUCH",
                "SMITE",
                "THORNS",
                "UNBREAKING"
        };
        if(handler.containsIgnoreCase(enchant.toUpperCase(), Arrays.asList(enchants))){
            handler.sendInvalid(src);
            return CommandResult.success();
        }else if(plugin.getConfig().getChildrenList().contains(item) ||
                plugin.getConfig().getNode(item).getChildrenList().contains(enchant)){
            src.sendMessage(Text.of(TextColors.RED, "Item and/or enchant for that item does not exist in config."));
            return CommandResult.success();
        }
        plugin.getConfig().getNode(item).removeChild(enchant.toUpperCase());
        try{
            plugin.getLoader().save(plugin.getConfig());
            handler.sendRemoval(src);
        }catch (IOException e){
            e.printStackTrace();
        }
        return CommandResult.success();
    }
}
