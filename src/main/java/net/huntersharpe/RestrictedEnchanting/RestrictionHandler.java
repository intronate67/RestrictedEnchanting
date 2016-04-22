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

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class RestrictionHandler {

    private RestrictedEnchanting plugin;

    public RestrictionHandler(RestrictedEnchanting plugin){
        this.plugin = plugin;
    }

    public void sendRestrictionMessage(Player player){
        player.sendMessage(Text.of(TextColors.RED, "Enchantmentment not allowed at that level. "));
    }

    public void sendInfo(CommandSource src){
        src.sendMessage(Text.of(TextColors.GRAY,
                "----------[",
                TextColors.BLUE,
                "RestrictedEnchanting",
                TextColors.GRAY,
                "]----------\n",
                TextColors.BLUE,
                "Author: ",
                TextColors.GRAY,
                "Hunter Sharpe (intronate67)\n")
        );
    }

    public void sendSuccess(CommandSource src){
        src.sendMessage(Text.of(TextColors.GREEN, "Restriction added to config."));
    }

    public void sendInvalid(CommandSource src){
        src.sendMessage(Text.of(TextColors.RED, "Invalid enchantment name."));
    }

    public void sendInvalidLevel(CommandSource src){
        src.sendMessage(Text.of(TextColors.RED, "Level is too high."));
    }

    public void sendExists(CommandSource src){
        src.sendMessage(Text.of(TextColors.RED, "Restriction already exists in the config for this level."));
    }

    public void sendRemoval(CommandSource src){
        src.sendMessage(Text.of(TextColors.GREEN, "Successfully removed restriction."));
    }

    public List<ItemEnchantment> exists(List<ItemEnchantment> enchants){
        List<ItemEnchantment> existing = null;
        for(int i = 0; i < enchants.size(); i++){
            String enchant = enchants.get(i).getEnchantment().getName();
            if(plugin.getConfig().getNode("restrictions", enchants.get(i).toContainer()).getChildrenList().contains(enchant)){
                existing.add(enchants.get(i));
            }
        }
        return existing;
    }

    public List<ItemEnchantment> exceedsLevel(List<ItemEnchantment> enchants){
        List<ItemEnchantment> exceeding = null;
        for(int i = 0; i < enchants.size(); i++){
            if(enchants.get(i).getLevel() > plugin.getConfig().getNode("restrictions", enchants.get(i).toContainer().getName(),
                    enchants.get(i).getEnchantment().getName(),
                    "level").getInt()){
                exceeding.add(enchants.get(i));
            }
        }
        return exceeding;
    }

}
