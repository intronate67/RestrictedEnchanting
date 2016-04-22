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

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;

public class EnchantmentListener {

    private RestrictedEnchanting plugin;

    public EnchantmentListener(RestrictedEnchanting plugin){
        this.plugin = plugin;
    }

    private RestrictionHandler handler = new RestrictionHandler(plugin);
    //Probably only listens to changes by players
    @Listener
    public void onEnchant(ChangeDataHolderEvent e, @First Player player){
        if(!e.getTargetHolder().supports(EnchantmentData.class)) return;
        List<ItemEnchantment> enchants = e.getTargetHolder().get(Keys.ITEM_ENCHANTMENTS).get();
        ItemStack item = (ItemStack)e.getTargetHolder();
        if(handler.exists(enchants).size() == 0) return;
        List<ItemEnchantment> existing = handler.exists(enchants);
        if(handler.exceedsLevel(existing).size() == 0) return;
        List<ItemEnchantment> exceeds = handler.exceedsLevel(existing);
        for(int i = 0; i < exceeds.size(); i++){
            item.get(EnchantmentData.class).get().enchantments().remove(exceeds.get(i));
            handler.sendRestrictionMessage(player);
            e.setCancelled(true);
        }
    }

}
